/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.enterAlwaysScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.ghostbear.koguma.R
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.ui.SmallAppBar
import me.ghostbear.koguma.ui.main.MainViewModel.Event
import me.ghostbear.koguma.util.toast

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val onSave = f@{ uri: Uri ->
        scope.launch {
            viewModel.save(uri)
        }
    }
    val openDocumentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        scope.launch {
            it?.let { it ->
                viewModel.currentUri = it
                viewModel.load(it)
            }
        }
    }
    val createDocumentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) {
        it?.let(onSave)
    }

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
                    IconButton(onClick = { openDocumentLauncher.launch(arrayOf("application/json")) }) {
                        Icon(Icons.Outlined.FileOpen, contentDescription = "open_file")
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
                ExtendedFloatingActionButton(
                    text = { Text(text = "Save") },
                    icon = { Icon(Icons.Outlined.Save, contentDescription = "save_file") },
                    onClick = {
                        if (viewModel.currentUri != null) {
                            onSave(viewModel.currentUri!!)
                        } else {
                            createDocumentLauncher.launch("details.json")
                        }
                    },
                    expanded = scrollState.isScrollingUp()
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title.orEmpty(),
                onValueChange = { viewModel.title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Title") }
            )
            OutlinedTextField(
                value = viewModel.author.orEmpty(),
                onValueChange = { viewModel.author = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Author") }
            )
            OutlinedTextField(
                value = viewModel.artist.orEmpty(),
                onValueChange = { viewModel.artist = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Artist") }
            )
            OutlinedTextField(
                value = viewModel.description.orEmpty(),
                onValueChange = { viewModel.description = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Description") }
            )
            OutlinedTextField(
                value = viewModel.genre.orEmpty(),
                onValueChange = { viewModel.genre = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Genre") },
                visualTransformation = {
                    val transformed = buildAnnotatedString {
                        val strings = it.split(",\\s*".toRegex())
                        strings.forEachIndexed { index, string ->
                            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                append(string)
                            }
                            if (index != strings.lastIndex) append(", ")
                        }
                    }
                    val offsetMapping = object : OffsetMapping {
                        override fun originalToTransformed(offset: Int): Int = transformed.length

                        override fun transformedToOriginal(offset: Int): Int = it.length
                    }
                    TransformedText(
                        transformed,
                        offsetMapping
                    )
                }
            )
            val highlightColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
            Text(
                text = buildAnnotatedString {
                    append("Use a comma (")
                    withStyle(style = SpanStyle(background = highlightColor, letterSpacing = 8.sp)) {
                        append(",")
                    }
                    append(") to separate the genres")
                },
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Status")
            Status.values.forEach {
                val onClick = { viewModel.status = it }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = viewModel.status == it, onClick = onClick)
                    Text(text = it.visualName)
                }
            }
        }
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
fun ScrollState.isScrollingUp(): Boolean {
    var previousOffset by remember { mutableStateOf(value) }
    return remember {
        derivedStateOf {
            (previousOffset >= value).also {
                previousOffset = value
            }
        }
    }.value
}
