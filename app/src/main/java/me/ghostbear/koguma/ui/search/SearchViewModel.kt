/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.ghostbear.koguma.R
import me.ghostbear.koguma.domain.interactor.SearchOnlineManga
import me.ghostbear.koguma.domain.model.Manga

class SearchViewModel @AssistedInject constructor(
    @Assisted val state: SearchStateImpl,
    val searchOnlineManga: SearchOnlineManga
) : ViewModel(), SearchState by state {

    private val _events = Channel<Event>(Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    fun search() {
        viewModelScope.launch {
            if (searchQuery == null) return@launch
            state.isLoading = true
            when (val result = searchOnlineManga.await(searchQuery ?: "")) {
                is SearchOnlineManga.Result.Error -> _events.send(Event.InternalError(result.error))
                SearchOnlineManga.Result.ConnectionTimeout -> _events.send(Event.LocalizedMessage(R.string.error_connection_timeout))
                SearchOnlineManga.Result.GraphQLQueryMalformed -> _events.send(Event.LocalizedMessage(R.string.error_query_malformed))
                SearchOnlineManga.Result.IllegalResponse -> _events.send(Event.LocalizedMessage(R.string.error_illegal_response))
                SearchOnlineManga.Result.NetworkError -> _events.send(Event.LocalizedMessage(R.string.error_network))
                is SearchOnlineManga.Result.Success -> state.result = result.list
            }
            state.isLoading = false
        }
    }

    sealed class Dialog {
        data class Override(val manga: Manga) : Dialog()
    }

    sealed class Event {
        data class LocalizedMessage(@StringRes val id: Int) : Event()
        data class InternalError(val error: Throwable) : Event()
    }

    @AssistedFactory
    interface Factory {
        fun create(state: SearchStateImpl): SearchViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            state: SearchStateImpl
        ): ViewModelProvider.Factory = object :ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(state) as T
            }
        }
    }
}

