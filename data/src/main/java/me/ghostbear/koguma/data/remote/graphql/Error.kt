/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote.graphql

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val message: String? = null,
    val status: String? = null,
    val locations: List<Location>? = null
)
