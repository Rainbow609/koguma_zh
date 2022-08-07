/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

@Composable
inline fun <reified VM : ViewModel> hiltViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    delegateFactory: ViewModelProvider.Factory? = null
): VM {
    val factory = createHiltViewModelFactory(viewModelStoreOwner, delegateFactory)
    return viewModel(viewModelStoreOwner, factory = factory)
}

@Composable
fun createHiltViewModelFactory(
    viewModelStoreOwner: ViewModelStoreOwner,
    delegateFactory: ViewModelProvider.Factory?
): ViewModelProvider.Factory? = if (viewModelStoreOwner is NavBackStackEntry) {
    HiltViewModelFactory.createInternal(
        (LocalContext.current as Activity),
        viewModelStoreOwner,
        viewModelStoreOwner.arguments,
        delegateFactory ?: viewModelStoreOwner.defaultViewModelProviderFactory
    )
} else {
    null
}