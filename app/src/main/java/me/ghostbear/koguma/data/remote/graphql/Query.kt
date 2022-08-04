/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote.graphql

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject

@Serializable
data class Query(
    val operationName: String,
    val query: String,
    val variables: JsonObject
) {
    constructor(
        operationName: String,
        query: String,
        variables: JsonObjectBuilder.() -> Unit
    ) : this(operationName, query, buildJsonObject(variables))
}
