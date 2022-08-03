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
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.EntryPointAccessors
import me.ghostbear.koguma.KogumaActivity
import me.ghostbear.koguma.domain.model.Manga

@Composable
fun mainViewModel(state: MainStateImpl): MainViewModel {
    val view = LocalView.current
    val factory = EntryPointAccessors.fromActivity(
        (view.context as Activity),
        KogumaActivity.ViewModelFactoryProvider::class.java
    ).mainViewModelFactory()
    return viewModel(factory = MainViewModel.provideFactory(factory, state))
}

fun MainState.createManga(): Manga {
    return Manga(
        title,
        author,
        artist,
        description,
        genre?.split(",\\s*".toRegex())?.map { it.trim() },
        status
    )
}