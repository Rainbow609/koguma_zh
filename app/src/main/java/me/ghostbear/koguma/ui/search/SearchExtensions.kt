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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory
import me.ghostbear.koguma.KogumaActivity
import me.ghostbear.koguma.domain.model.Manga

@Composable
fun searchViewModel(
    state: SearchStateImpl,
    navBackStackEntry: NavBackStackEntry
): SearchViewModel {
    val view = LocalView.current
    val factory = EntryPointAccessors.fromActivity(
        (view.context as Activity),
        KogumaActivity.ViewModelFactoryProvider::class.java
    ).searchViewModelFactory()
    return viewModel(factory = HiltViewModelFactory.createInternal(
        (view.context as Activity),
        navBackStackEntry,
        navBackStackEntry.arguments,
        SearchViewModel.provideFactory(factory, state)
    ))
}

val Manga.authorWithArtist: String
    get() {
        return when {
            artist != null && artist != author -> "$author, $artist"
            author != null -> author
            else -> ""
        }
    }