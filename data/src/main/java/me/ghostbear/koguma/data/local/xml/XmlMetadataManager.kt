/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.local.xml

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import me.ghostbear.koguma.data.local.MetadataManager
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import nl.adaptivity.xmlutil.AndroidXmlReader
import nl.adaptivity.xmlutil.serialization.XML
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class XmlMetadataManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val xml: XML
) : MetadataManager {

    override suspend fun write(uri: Uri, manga: Manga) {
        val comicInfo = ComicInfo(
            title = manga.title?.let { ComicInfo.Title(it) },
            series = manga.title?.let { ComicInfo.Series(it) },
            web = null,
            summary = manga.description?.let { ComicInfo.Summary(it) },
            writer = manga.author?.let { ComicInfo.Writer(it) },
            penciller = manga.artist?.let { ComicInfo.Penciller(it) },
            translator = null,
            genre = manga.genre?.let { ComicInfo.Genre(it.joinToString()) },
            publishingStatus = when (manga.status) {
                Status.Ongoing -> "Ongoing"
                Status.Completed -> "Completed"
                Status.Licensed -> "Licensed"
                Status.PublishingFinished -> "Publishing finished"
                Status.Cancelled -> "Cancelled"
                Status.OnHaitus -> "On hiatus"
                else -> "Unknown"
            }.let { ComicInfo.PublishingStatusTachiyomi(it) },
            inker = null,
            colorist = null,
            letterer = null,
            coverArtist = null,
            tags = null,
        )

        context.contentResolver.openFileDescriptor(uri, "w")?.use {
            FileOutputStream(it.fileDescriptor).use {
                it.channel.truncate(0)
                it.write(xml.encodeToString(comicInfo).toByteArray())
            }
        }
    }

    override suspend fun read(uri: Uri): Manga {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: throw NullPointerException("The provider recently crashed")
            val comicInfo = AndroidXmlReader(inputStream, StandardCharsets.UTF_8.name()).use {
                xml.decodeFromReader<ComicInfo>(it)
            }
            Manga(
                title = comicInfo.series?.value,
                author = comicInfo.writer?.value,
                description = comicInfo.summary?.value,
                genre = listOfNotNull(
                    comicInfo.genre?.value,
                    comicInfo.tags?.value,
                )
                    .flatMap { it.split(", ") }
                    .distinct()
                    .takeIf { it.isNotEmpty() },
                artist = listOfNotNull(
                    comicInfo.penciller?.value,
                    comicInfo.inker?.value,
                    comicInfo.colorist?.value,
                    comicInfo.letterer?.value,
                    comicInfo.coverArtist?.value,
                )
                    .flatMap { it.split(", ") }
                    .distinct()
                    .joinToString(", ") { it.trim() }
                    .takeIf { it.isNotEmpty() },
                status = when (comicInfo.publishingStatus?.value) {
                    "Ongoing" -> Status.Ongoing
                    "Completed" -> Status.Completed
                    "Licensed" -> Status.Licensed
                    "Publishing finished" -> Status.PublishingFinished
                    "Cancelled" -> Status.Cancelled
                    "On hiatus" -> Status.OnHaitus
                    else -> Status.Unknown
                }
            )
        } catch (e: Exception) {
            throw e
        }
    }
}
