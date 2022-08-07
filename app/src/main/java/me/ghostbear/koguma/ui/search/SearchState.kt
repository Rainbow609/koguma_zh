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