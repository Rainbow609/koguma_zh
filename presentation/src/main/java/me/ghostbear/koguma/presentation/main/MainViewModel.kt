/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.main

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.ghostbear.koguma.domain.interactor.ReadMangaFromFile
import me.ghostbear.koguma.domain.interactor.WriteMangaToFile
import me.ghostbear.koguma.presentation.R

class MainViewModel @AssistedInject constructor(
    @Assisted private val state: MainStateImpl,
    private val readMangaFromFile: ReadMangaFromFile,
    private val writeMangaToFile: WriteMangaToFile
) : ViewModel(), MainState by state {

    private val _events = Channel<Event>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    var currentUri: Uri? by mutableStateOf(null)

    fun load(uri: Uri?, remember: Boolean = true) {
        currentUri = null
        viewModelScope.launch {
            if (uri != null) {
                if (remember) currentUri = uri
                internalLoad(uri)
            } else {
                _events.send(Event.LocalizedMessage(R.string.error_uri_not_provided))
            }
        }
    }

    private fun internalLoad(uri: Uri) {
        viewModelScope.launch {
            when (val result = readMangaFromFile.await(uri)) {
                is ReadMangaFromFile.Result.InternalError -> _events.trySend(
                    Event.InternalError(
                        result.error
                    )
                )
                ReadMangaFromFile.Result.CouldntDecodeFile -> _events.trySend(
                    Event.LocalizedMessage(
                        R.string.error_decode_file
                    )
                )
                ReadMangaFromFile.Result.CouldntReadFile -> _events.trySend(Event.LocalizedMessage(R.string.error_reading_file))
                ReadMangaFromFile.Result.FileMalformed -> _events.trySend(Event.LocalizedMessage(R.string.error_file_malformed))
                ReadMangaFromFile.Result.FileNotFound -> _events.trySend(Event.LocalizedMessage(R.string.error_file_not_found))
                is ReadMangaFromFile.Result.Success -> {
                    val manga = result.manga
                    title = manga.title
                    author = manga.author
                    artist = manga.artist
                    description = manga.description
                    genre = manga.genre?.joinToString()
                    status = manga.status
                    _events.trySend(Event.LocalizedMessage(R.string.success_reading_file))
                }
            }
        }
    }

    fun save(uri: Uri?) {
        viewModelScope.launch {
            if (uri != null) {
                internalSave(uri)
            } else {
                _events.send(Event.LocalizedMessage(R.string.error_uri_not_provided))
            }
        }
    }

    private fun internalSave(uri: Uri) {
        viewModelScope.launch {
            when (val result = writeMangaToFile.await(uri, getManga())) {
                is WriteMangaToFile.Result.InternalError -> _events.trySend(
                    Event.InternalError(
                        result.error
                    )
                )
                WriteMangaToFile.Result.CouldntEncodeFile -> _events.trySend(
                    Event.LocalizedMessage(
                        R.string.error_encode_file
                    )
                )
                WriteMangaToFile.Result.CouldntWriteFile -> _events.trySend(Event.LocalizedMessage(R.string.error_writing_file))
                WriteMangaToFile.Result.FileNotFound -> _events.trySend(Event.LocalizedMessage(R.string.error_file_not_found))
                WriteMangaToFile.Result.Success -> _events.trySend(Event.LocalizedMessage(R.string.success_writing_file))
            }
        }
    }

    sealed class Event {
        data class LocalizedMessage(@StringRes val id: Int) : Event()
        data class InternalError(val error: Throwable) : Event()
    }

    @AssistedFactory
    interface Factory {
        fun create(state: MainStateImpl): MainViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            state: MainStateImpl
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(state) as T
            }
        }
    }
}
