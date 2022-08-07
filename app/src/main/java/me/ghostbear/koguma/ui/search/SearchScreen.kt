/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.search

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Forest
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.ghostbear.koguma.R
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.ui.Route
import me.ghostbear.koguma.ui.main.MainViewModel
import me.ghostbear.koguma.ui.main.setManga
import me.ghostbear.koguma.ui.search.SearchViewModel.Dialog
import me.ghostbear.koguma.ui.search.SearchViewModel.Event
import me.ghostbear.koguma.util.plus
import me.ghostbear.koguma.util.toast

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val onSave = f@{ uri: Uri ->
        scope.launch {
            mainViewModel.save(uri)
        }
    }
    val createDocumentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) {
        it?.let(onSave)
    }
    Scaffold(
        topBar = {
            when {
                viewModel.searchQuery == null -> SmallTopAppBar(
                    modifier = Modifier
                        .statusBarsPadding(),
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    },
                    actions = {
                        IconButton(onClick = { viewModel.searchQuery = "" }) {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = "search")
                        }
                    }
                )
                else -> {
                    val focusRequester = remember { FocusRequester() }
                    SmallTopAppBar(
                        modifier = Modifier
                            .statusBarsPadding(),
                        navigationIcon = {
                            IconButton(onClick = {
                                focusRequester.freeFocus()
                                viewModel.searchQuery = null
                            }) {
                                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back")
                            }
                        },
                        title = {
                            BasicTextField(
                                value = viewModel.searchQuery!!,
                                onValueChange = { viewModel.searchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        viewModel.search()
                                        focusRequester.freeFocus()
                                    }
                                ),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
                            )
                        },
                        actions = {
                            IconButton(onClick = {
                                viewModel.searchQuery = ""
                                focusRequester.requestFocus()
                            }) {
                                Icon(imageVector = Icons.Outlined.Clear, contentDescription = "clear")
                            }
                        }
                    )
                    LaunchedEffect(Unit) {
                        delay(200)
                        focusRequester.requestFocus()
                    }
                }
            }
        },
    ) { paddingValues ->
        when {
            viewModel.isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            viewModel.isEmpty -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Forest,
                        contentDescription = "search",
                        modifier = Modifier
                            .size(128.dp)
                    )
                    Text(
                        text = "Result is empty.\n Start a search by clicking the search icon in the top right or if you already are try changing your query.",
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> LazyColumn(
                contentPadding = PaddingValues(8.dp) + paddingValues,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.result) { item ->
                    SearchItem(
                        manga = item,
                        onClickEdit = {
                            if (mainViewModel.isSavable) {
                                viewModel.dialog = Dialog.Override(item)
                            } else {
                                mainViewModel.setManga(item)
                                navController.popBackStack(
                                    route = Route.Home.route,
                                    inclusive = false,
                                    saveState = true
                                )
                            }
                        },
                        onClickSave = {
                            createDocumentLauncher.launch("details.json")
                        }
                    )
                }
            }
        }
    }
    val dialog = viewModel.dialog
    val onDismissRequest = { viewModel.dialog = null }
    if (dialog != null && dialog is Dialog.Override) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = {
                    mainViewModel.setManga(dialog.manga)
                    navController.popBackStack(
                        route = Route.Home.route,
                        inclusive = false,
                        saveState = true
                    )
                    onDismissRequest()
                }) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            },
            title = {
                Text(text = "Override")
            },
            text = {
                Text(text = "You are about to override the current form. Are you sure you want to override it?")
            }
        )
    }
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is Event.InternalError -> context.toast(event.error.message ?: context.getString(R.string.internal_error))
                is Event.LocalizedMessage -> context.toast(event.id)
            }
        }
    }
}

@Composable
fun SearchItem(
    manga: Manga,
    onClickEdit: () -> Unit,
    onClickSave: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(
        Modifier.clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        ) {
            if (manga.title != null)
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            Text(text = manga.authorWithArtist)
            if (manga.status != null)
                Text(
                    text = manga.status.name,
                    style = MaterialTheme.typography.bodySmall
                )

        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                if (manga.description != null)
                    Text(
                        text = manga.description,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                if (manga.genre != null)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(manga.genre) { genre ->
                            SuggestionChip(
                                onClick = { /*TODO*/ },
                                enabled = false,
                                label = {
                                    Text(text = genre)
                                }
                            )
                        }
                    }
            }
        }
        Row {
            IconButton(onClick = { expanded = expanded.not() }) {
                val icon = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore
                Icon(imageVector = icon, contentDescription = "expand")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClickEdit) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit")
            }
            IconButton(onClick = onClickSave) {
                Icon(imageVector = Icons.Outlined.Save, contentDescription = "save")
            }
        }
    }
}

@Preview
@Composable
fun SearchItemPreview() {
    SearchItem(
        manga = Manga(
            title = "Kuitsume Youhei no Gensou Kitan",
            author = "Mine",
            artist = "Area Ikemiya",
            description = "When seasoned mercenary Loren is the sole survivor of a disastrous battle that destroys the rest of his company, he must find a new way to survive in the world. With no friends or connections, he has no hope of joining an adventuring party–until enigmatic priestess Lapis offers to partner up with him. But there’s more to Lapis than meets the eye, and Loren soon finds himself bound to a fate stranger than he imagined.",
            genre = listOf(
                "Action",
                "Adventure",
                "Fantasy",
                "Romance"
            ),
            status = Status.Ongoing
        ),
        onClickEdit = {},
        onClickSave = {}
    )
}