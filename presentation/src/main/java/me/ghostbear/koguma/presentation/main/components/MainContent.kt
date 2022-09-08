/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.main.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ghostbear.koguma.presentation.R
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.presentation.main.MainState
import me.ghostbear.koguma.presentation.main.visualName

@Composable
fun MainContent(
    state: MainState,
    contentPadding: PaddingValues = PaddingValues(),
    verticalScrollState: ScrollState = rememberScrollState()
) {
    Column(
        modifier = Modifier
            .verticalScroll(verticalScrollState)
            .padding(contentPadding)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        MainTextField(
            value = state.title.orEmpty(),
            onValueChange = { state.title = it },
            label = { Text(text = stringResource(R.string.title)) }
        )
        MainTextField(
            value = state.author.orEmpty(),
            onValueChange = { state.author = it },
            label = { Text(text = stringResource(R.string.author)) }
        )
        MainTextField(
            value = state.artist.orEmpty(),
            onValueChange = { state.artist = it },
            label = { Text(text = stringResource(R.string.artist)) }
        )
        MainTextField(
            value = state.description.orEmpty(),
            onValueChange = { state.description = it },
            label = { Text(text = stringResource(R.string.description)) }
        )
        MainTextField(
            value = state.genre.orEmpty(),
            onValueChange = { state.genre = it },
            label = { Text(text = stringResource(R.string.genre)) },
            visualTransformation = GenreVisualTransformation
        )
        val highlightColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
        Text(
            text = stringResource(id = R.string.genre_description).let {
                buildAnnotatedString {
                    val string = it.split(",")
                    append(string[0])
                    withStyle(style = SpanStyle(background = highlightColor, letterSpacing = 8.sp)) {
                        append(",")
                    }
                    append(string[1])
                }
            },
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = stringResource(R.string.status))
        Status.values.forEach {
            val onClick = { state.status = it }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = state.status == it, onClick = onClick)
                Text(text = it.visualName)
            }
        }
    }
}

val GenreVisualTransformation: VisualTransformation = VisualTransformation { annotatedString ->
    val transformed = buildAnnotatedString {
        val strings = annotatedString.split(",\\s*".toRegex())
        strings.forEachIndexed { index, string ->
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append(string)
            }
            if (index != strings.lastIndex) append(", ")
        }
    }
    TransformedText(
        transformed,
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = transformed.length

            override fun transformedToOriginal(offset: Int): Int = annotatedString.length
        }
    )
}
