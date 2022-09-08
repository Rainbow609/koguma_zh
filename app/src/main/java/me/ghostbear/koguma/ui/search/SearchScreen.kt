/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Forest
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.ghostbear.koguma.R
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.ui.KogumaContracts
import me.ghostbear.koguma.ui.Route
import me.ghostbear.koguma.ui.main.MainViewModel
import me.ghostbear.koguma.ui.main.setManga
import me.ghostbear.koguma.ui.search.SearchViewModel.Dialog
import me.ghostbear.koguma.ui.search.SearchViewModel.Event
import me.ghostbear.koguma.ui.search.components.OverrideDialog
import me.ghostbear.koguma.ui.search.components.SearchItem
import me.ghostbear.koguma.ui.search.components.SearchToolbar
import me.ghostbear.koguma.util.plus
import me.ghostbear.koguma.util.toast

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val createDocumentLauncher = rememberLauncherForActivityResult(KogumaContracts.CreateJsonDocument, mainViewModel::save)
    Scaffold(
        topBar = {
            SearchToolbar(
                state = viewModel,
                onSearch = viewModel::search
            )
        },
    ) { paddingValues ->
        when {
            viewModel.isLoading -> {
                LoadingScreen(contentPadding = paddingValues)
            }
            viewModel.isEmpty -> {
                SearchEmptyScreen()
            }
            else -> {
                SearchContent(
                    state = viewModel,
                    onEditClickItem = { manga ->
                        if (mainViewModel.isSavable) {
                            viewModel.dialog = Dialog.Override(manga)
                        } else {
                            mainViewModel.setManga(manga)
                            navController.popBackStack(
                                route = Route.Home.route,
                                inclusive = false,
                                saveState = true
                            )
                        }
                    },
                    onSaveClickItem = {
                        createDocumentLauncher.launch("details.json")
                    },
                    contentPadding = paddingValues
                )
            }
        }
    }
    val dialog = viewModel.dialog
    if (dialog != null && dialog is Dialog.Override) {
        OverrideDialog(
            onDismissRequest = { viewModel.dialog = null },
            onOverride = {
                mainViewModel.setManga(dialog.manga)
                navController.popBackStack(
                    route = Route.Home.route,
                    inclusive = false,
                    saveState = true
                )
            }
        )
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is Event.InternalError -> context.toast(event.error.message ?: context.getString(R.string.internal_error))
                is Event.LocalizedMessage -> context.toast(event.id)
            }
        }
    }
}

@Composable
fun SearchContent(
    state: SearchState,
    onEditClickItem: (Manga) -> Unit,
    onSaveClickItem: (Manga) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp) + contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.result) { item ->
            SearchItem(
                manga = item,
                onClickEdit = { onEditClickItem(item) },
                onClickSave = { onSaveClickItem(item) }
            )
        }
    }
}

@Composable
fun SearchEmptyScreen(
    contentPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Forest,
                contentDescription = stringResource(R.string.empty_result),
                modifier = Modifier
                    .size(128.dp)
            )
            Text(
                text = "Result is empty.\n Start a search by clicking the search icon in the top right or if you already are try changing your query.",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoadingScreen(
    contentPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
