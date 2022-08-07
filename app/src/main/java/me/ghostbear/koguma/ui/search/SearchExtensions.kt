/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import dagger.hilt.android.EntryPointAccessors
import me.ghostbear.koguma.KogumaActivity
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.util.hiltViewModel

@Composable
fun searchViewModel(
    state: SearchStateImpl,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): SearchViewModel {
    val view = LocalView.current
    val factory = EntryPointAccessors.fromActivity(
        (view.context as Activity),
        KogumaActivity.ViewModelFactoryProvider::class.java
    ).searchViewModelFactory()
    return hiltViewModel(
        viewModelStoreOwner,
        SearchViewModel.provideFactory(factory, state)
    )
}

val Manga.authorWithArtist: String
    get() {
        return when {
            artist != null && artist != author -> "$author, $artist"
            author != null -> author
            else -> ""
        }
    }