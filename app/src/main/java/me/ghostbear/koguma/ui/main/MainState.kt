/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.main

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status

interface MainState {
    var title: String?
    var author: String?
    var artist: String?
    var description: String?
    var genre: String?
    var status: Status?
    val isSavable: Boolean
}

fun MainState(manga: Manga = Manga()): MainState {
    return MainStateImpl(manga)
}

class MainStateImpl(manga: Manga) : MainState {
    override var title: String? by mutableStateOf(manga.title)
    override var author: String? by mutableStateOf(manga.author)
    override var artist: String? by mutableStateOf(manga.artist)
    override var description: String? by mutableStateOf(manga.description)
    override var genre: String? by mutableStateOf(manga.genre?.joinToString())
    override var status: Status? by mutableStateOf(manga.status)
    override val isSavable: Boolean by derivedStateOf { listOf(title, author, artist, description, genre, status).any { it != null } }
}
