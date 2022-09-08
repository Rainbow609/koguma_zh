/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import me.ghostbear.koguma.presentation.R
import me.ghostbear.koguma.extensions.MimeType
import me.ghostbear.koguma.extensions.isScrollingUp
import me.ghostbear.koguma.extensions.toast
import me.ghostbear.koguma.presentation.KogumaContracts
import me.ghostbear.koguma.presentation.SmallAppBar
import me.ghostbear.koguma.presentation.main.MainViewModel.Event
import me.ghostbear.koguma.presentation.main.components.MainContent
import me.ghostbear.koguma.presentation.main.components.MainFloatingActionButton

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val openDocumentLauncher = rememberLauncherForActivityResult(KogumaContracts.OpenDocument, viewModel::load)
    val createDocumentLauncher = rememberLauncherForActivityResult(KogumaContracts.CreateJsonDocument, viewModel::save)

    val scrollState = rememberScrollState()
    val topAppBarState = enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarState.nestedScrollConnection),
        topBar = {
            SmallAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { openDocumentLauncher.launch(arrayOf(MimeType.Json)) }) {
                        Icon(Icons.Outlined.FileOpen, contentDescription = stringResource(R.string.content_description_open))
                    }
                },
                scrollBehavior = topAppBarState
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = viewModel.isSavable,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                MainFloatingActionButton(
                    expanded = scrollState.isScrollingUp(),
                    onClick = {
                        if (viewModel.currentUri != null) {
                            viewModel.save(viewModel.currentUri)
                        } else {
                            createDocumentLauncher.launch("details.json")
                        }
                    }
                )
            }
        },
    ) { paddingValues ->
        MainContent(
            state = viewModel,
            contentPadding = paddingValues,
            verticalScrollState = scrollState
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
