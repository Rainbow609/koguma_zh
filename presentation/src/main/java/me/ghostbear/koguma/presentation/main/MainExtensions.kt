/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.presentation.R

fun MainState.getManga(): Manga {
    return Manga(
        title,
        author,
        artist,
        description,
        genre?.split(",\\s*".toRegex())?.map { it.trim() },
        status
    )
}

fun MainState.setManga(manga: Manga) {
    title = manga.title
    author = manga.author
    artist = manga.artist
    description = manga.description
    genre = manga.genre?.joinToString()
    status = manga.status
}

val Status.visualName: String
    @Composable
    get() = when (this) {
        Status.Unknown -> stringResource(id = R.string.status_unknown)
        Status.Ongoing -> stringResource(id = R.string.status_ongoing)
        Status.Completed -> stringResource(id = R.string.status_completed)
        Status.Licensed -> stringResource(id = R.string.status_licensed)
        Status.PublishingFinished -> stringResource(id = R.string.status_publishing_finished)
        Status.Cancelled -> stringResource(id = R.string.status_cancelled)
        Status.OnHaitus -> stringResource(id = R.string.status_on_haitus)
    }
