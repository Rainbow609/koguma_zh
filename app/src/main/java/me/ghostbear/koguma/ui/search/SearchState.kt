/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.ghostbear.koguma.domain.model.Manga

@Stable
interface SearchState {
    var searchQuery: String?
    val result: List<Manga>
    val isEmpty: Boolean
    val isLoading: Boolean
    var dialog: SearchViewModel.Dialog?
}

fun SearchState(): SearchState {
    return SearchStateImpl()
}

class SearchStateImpl : SearchState {
    override var searchQuery: String? by mutableStateOf(null)
    override var result: List<Manga> by mutableStateOf(emptyList())
    override val isEmpty: Boolean by derivedStateOf { result.isEmpty() }
    override var isLoading: Boolean by mutableStateOf(false)
    override var dialog: SearchViewModel.Dialog? by mutableStateOf(null)
}
