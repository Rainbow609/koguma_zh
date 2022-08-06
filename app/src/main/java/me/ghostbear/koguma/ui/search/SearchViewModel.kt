/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import me.ghostbear.koguma.domain.interactor.SearchOnlineManga
import me.ghostbear.koguma.domain.model.Manga

class SearchViewModel @AssistedInject constructor(
    @Assisted val state: SearchStateImpl,
    val searchOnlineManga: SearchOnlineManga
) : ViewModel(), SearchState by state {

    fun search() {
        viewModelScope.launch {
            if (searchQuery == null) return@launch
            state.result = searchOnlineManga.await(searchQuery ?: "")
        }
    }

    sealed class Dialog {
        data class Override(val manga: Manga) : Dialog()
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

