/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.interactor

import android.net.Uri
import kotlinx.serialization.SerializationException
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.repository.MangaRepository
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

class ReadMangaFromFile @Inject constructor(
    private val mangaRepository: MangaRepository
) {

    suspend fun await(uri: Uri): Result {
        return try {
            val manga = mangaRepository.read(uri)
            Result.Success(manga)
        } catch (e: FileNotFoundException) {
            Result.FileNotFound
        } catch (e: IOException) {
            Result.CouldntReadFile
        } catch (e: SerializationException) {
            Result.CouldntDecodeFile
        } catch (e: IllegalArgumentException) {
            Result.FileMalformed
        } catch (e: Exception) {
            Result.InternalError(e)
        }
    }

    sealed class Result {
        data class InternalError(val error: Throwable) : Result()
        object CouldntDecodeFile : Result()
        object CouldntReadFile : Result()
        object FileMalformed : Result()
        object FileNotFound : Result()
        data class Success(val manga: Manga) : Result()
    }
}
