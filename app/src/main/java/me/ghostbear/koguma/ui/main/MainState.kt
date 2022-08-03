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

fun MainState(): MainState {
    return MainStateImpl()
}

class MainStateImpl : MainState {
    override var title: String? by mutableStateOf(null)
    override var author: String? by mutableStateOf(null)
    override var artist: String? by mutableStateOf(null)
    override var description: String? by mutableStateOf(null)
    override var genre: String? by mutableStateOf(null)
    override var status: Status? by mutableStateOf(null)
    override val isSavable: Boolean by derivedStateOf { listOf(title, author, artist, description, genre, status).any { it != null } }
}
