/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.libraries

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.mikepenz.aboutlibraries.entity.Library
import me.ghostbear.koguma.extensions.plus
import me.ghostbear.koguma.presentation.R
import me.ghostbear.koguma.presentation.SmallAppBar
import me.ghostbear.koguma.presentation.libraries.components.LibraryContainer
import me.ghostbear.koguma.presentation.libraries.components.LicenseDialog

@Composable
fun LibrariesScreen(navController: NavHostController) {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val dialog = rememberSaveable { mutableStateOf<Library?>(null) }

    Scaffold(
        topBar = {
            SmallAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "back")
                    }
                },
                title = {
                    Text(text = stringResource(R.string.licenses))
                },
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { paddingValues ->
        LibraryContainer(
            modifier = Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues + WindowInsets.navigationBars.asPaddingValues(),
            onOpenDialog = { library -> dialog.value = library }
        )
    }

    val library = dialog.value
    if (library != null) {
        LicenseDialog(
            library = library,
            onDismissRequest = { dialog.value = null }
        )
    }
}
