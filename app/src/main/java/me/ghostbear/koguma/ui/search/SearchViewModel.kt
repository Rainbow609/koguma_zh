/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import me.ghostbear.koguma.domain.interactor.SearchOnlineManga
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.ui.search.SearchViewModel.Dialog

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

interface SearchState {
    var searchQuery: String?
    val result: List<Manga>
    val isEmpty: Boolean
    var dialog: Dialog?
}

fun SearchState(): SearchState {
    return SearchStateImpl()
}

class SearchStateImpl : SearchState {
    override var searchQuery: String? by mutableStateOf(null)
    override var result: List<Manga> by mutableStateOf(emptyList())
    override val isEmpty: Boolean by derivedStateOf { result.isEmpty() }
    override var dialog: Dialog? by mutableStateOf(null)
}