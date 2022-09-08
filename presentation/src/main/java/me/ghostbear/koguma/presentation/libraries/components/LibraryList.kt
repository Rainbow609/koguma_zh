/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.libraries.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.entity.Library

@Composable
fun LibraryList(
    libraries: List<Library>,
    modifier: Modifier,
    contentValues: PaddingValues,
    onClickItem: (Library) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentValues
    ) {
        items(libraries) { library ->
            LibraryItem(
                library = library,
                onClick = { onClickItem(library) }
            )
        }
    }
}
