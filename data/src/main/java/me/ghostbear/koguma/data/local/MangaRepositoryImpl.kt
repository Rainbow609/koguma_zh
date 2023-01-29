/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.local

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import me.ghostbear.koguma.data.local.MetadataManagerFactory.Type
import me.ghostbear.koguma.domain.service.MangaRepository
import javax.inject.Inject
import me.ghostbear.koguma.domain.model.Manga as DomainManga

class MangaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val metadataManagerFactory: MetadataManagerFactory
) : MangaRepository {

    override suspend fun read(uri: Uri): DomainManga {
        return metadataManager(getFileExtension(uri).toString()).read(uri)
    }

    override suspend fun write(uri: Uri, manga: DomainManga) {
        metadataManager(getFileExtension(uri).toString()).write(uri, manga)
    }

    fun getFileExtension(contentUri: Uri): String? {
        val query = context.contentResolver.query(contentUri, null, null, null, null, null)
        query?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return null
    }

    private fun metadataManager(displayName: String) = when {
        displayName.split(".").last().startsWith("json") -> metadataManagerFactory.create(Type.JSON)
        displayName.split(".").last().startsWith("xml") -> metadataManagerFactory.create(Type.XML)
        else -> throw Exception("Unsupported file extension! Only JSON and XML is supported")
    }
}
