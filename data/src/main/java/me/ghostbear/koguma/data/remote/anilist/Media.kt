/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote.anilist

import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val title: Title? = null,
    val staff: Staff? = null,
    val description: String? = null,
    val genres: List<String>? = null,
    val status: String? = null
)
