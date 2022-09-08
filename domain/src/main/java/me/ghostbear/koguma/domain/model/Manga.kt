/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Manga(
    @SerialName("title")
    val title: String? = null,
    @SerialName("author")
    val author: String? = null,
    @SerialName("artist")
    val artist: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("genre")
    val genre: List<String>? = null,
    @SerialName("status")
    val status: Status? = null
)
