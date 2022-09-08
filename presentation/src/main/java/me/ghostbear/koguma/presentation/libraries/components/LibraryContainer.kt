/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.libraries.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.withContext

@Composable
fun LibraryContainer(
    modifier: Modifier,
    contentPadding: PaddingValues,
    onOpenDialog: (Library) -> Unit
) {
    val libs = remember { mutableStateOf<Libs?>(null) }

    val context = LocalContext.current
    LaunchedEffect(libs.value) {
        libs.value = Libs.Builder().withContext(context).build()
    }

    val libraries = libs.value?.libraries
    if (libraries != null) {
        LibraryList(
            libraries = libraries,
            modifier = modifier,
            contentValues = contentPadding,
            onClickItem = onOpenDialog
        )
    }
}
