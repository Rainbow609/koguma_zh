/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.main

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import dagger.hilt.android.EntryPointAccessors
import me.ghostbear.koguma.KogumaActivity
import me.ghostbear.koguma.R
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.util.hiltViewModel

@Composable
fun mainViewModel(
    state: MainStateImpl,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): MainViewModel {
    val view = LocalView.current
    val factory = EntryPointAccessors.fromActivity(
        (view.context as Activity),
        KogumaActivity.ViewModelFactoryProvider::class.java
    ).mainViewModelFactory()
    return hiltViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        delegateFactory = MainViewModel.provideFactory(factory, state)
    )
}

fun MainState.getManga(): Manga {
    return Manga(
        title,
        author,
        artist,
        description,
        genre?.split(",\\s*".toRegex())?.map { it.trim() },
        status
    )
}

fun MainState.setManga(manga: Manga) {
    title = manga.title
    author = manga.author
    artist = manga.artist
    description = manga.description
    genre = manga.genre?.joinToString()
    status = manga.status
}

val Status.visualName: String
    @Composable
    get() = when (this) {
        Status.Unknown -> stringResource(id = R.string.status_unknown)
        Status.Ongoing -> stringResource(id = R.string.status_ongoing)
        Status.Completed -> stringResource(id = R.string.status_completed)
        Status.Licensed -> stringResource(id = R.string.status_licensed)
        Status.PublishingFinished -> stringResource(id = R.string.status_publishing_finished)
        Status.Cancelled -> stringResource(id = R.string.status_cancelled)
        Status.OnHaitus -> stringResource(id = R.string.status_on_haitus)
    }
