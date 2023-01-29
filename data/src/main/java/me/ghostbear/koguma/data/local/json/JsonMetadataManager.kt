/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.local.json

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import me.ghostbear.koguma.data.local.MetadataManager
import me.ghostbear.koguma.data.mangaDataToDomain
import me.ghostbear.koguma.data.mangaDomainToData
import me.ghostbear.koguma.domain.model.Manga
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class JsonMetadataManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : MetadataManager {

    override suspend fun write(uri: Uri, manga: Manga) {
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use {
                        it.channel.truncate(0)
                        json.encodeToStream(mangaDomainToData.invoke(manga), it)
                    }
                }
            } catch (e: NullPointerException) {
                Log.e("JsonMetadataManager", "", e)
                throw e
            } catch (e: FileNotFoundException) {
                Log.e("JsonMetadataManager", "File was not found", e)
                throw e
            } catch (e: IOException) {
                Log.e("JsonMetadataManager", "Couldn't write file", e)
                throw e
            } catch (e: SerializationException) {
                Log.e("JsonMetadataManager", "Couldn't encode given object", e)
                throw e
            } catch (e: Exception) {
                Log.e("JsonMetadataManager", "Unknown error writing file", e)
                throw Exception(e)
            }
        }
    }

    override suspend fun read(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: throw NullPointerException("The provider recently crashed")
            val manga = inputStream.use {
                json.decodeFromStream<LocalManga>(inputStream)
            }
            mangaDataToDomain.invoke(manga)
        } catch (e: NullPointerException) {
            Log.e("JsonMetadataManager", "", e)
            throw e
        } catch (e: FileNotFoundException) {
            Log.e("JsonMetadataManager", "File was not found", e)
            throw e
        } catch (e: IOException) {
            Log.e("JsonMetadataManager", "Couldn't read file", e)
            throw e
        } catch (e: SerializationException) {
            Log.e("JsonMetadataManager", "Couldn't decode given Uri", e)
            throw e
        } catch (e: IllegalArgumentException) {
            Log.e("JsonMetadataManager", "Couldn't decode input", e)
            throw e
        } catch (e: Exception) {
            Log.e("JsonMetadataManager", "Unknown error reading file", e)
            throw Exception(e)
        }
    }
}
