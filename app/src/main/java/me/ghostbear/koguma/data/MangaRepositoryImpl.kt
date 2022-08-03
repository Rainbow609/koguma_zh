/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.repository.MangaRepository

class MangaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : MangaRepository {

    override suspend fun read(uri: Uri): Manga = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: throw NullPointerException("The provider recently crashed")
            inputStream.use {
                json.decodeFromStream(inputStream)
            }
        } catch (e: NullPointerException) {
            Log.e("MangaRepositoryImpl", "", e)
            throw e
        } catch (e: FileNotFoundException) {
            Log.e("MangaRepositoryImpl", "File was not found", e)
            throw e
        } catch (e: IOException) {
            Log.e("MangaRepositoryImpl", "Couldn't read file", e)
            throw e
        } catch (e: SerializationException) {
            Log.e("MangaRepositoryImpl", "Couldn't decode given Uri", e)
            throw e
        } catch (e: IllegalArgumentException) {
            Log.e("MangaRepositoryImpl", "Couldn't decode input", e)
            throw e
        } catch (e: Exception) {
            Log.e("MangaRepositoryImpl", "Unknown error reading file", e)
            throw Exception(e)
        }
    }

    override suspend fun write(uri: Uri, manga: Manga) = withContext(Dispatchers.IO) {
        try {
            val outputStream = context.contentResolver.openOutputStream(uri) ?: throw NullPointerException("The provider recently crashed")
            outputStream.use {
                json.encodeToStream(manga, it)
            }
        } catch (e: NullPointerException) {
            Log.e("MangaRepositoryImpl", "", e)
            throw e
        } catch (e: FileNotFoundException) {
            Log.e("MangaRepositoryImpl", "File was not found", e)
            throw e
        } catch (e: IOException) {
            Log.e("MangaRepositoryImpl", "Couldn't write file", e)
            throw e
        } catch (e: SerializationException) {
            Log.e("MangaRepositoryImpl", "Couldn't encode given object", e)
            throw e
        } catch (e: Exception) {
            Log.e("MangaRepositoryImpl", "Unknown error writing file", e)
            throw Exception(e)
        }
    }
}