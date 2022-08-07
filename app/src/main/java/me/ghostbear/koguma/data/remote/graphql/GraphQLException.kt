/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote.graphql

import io.ktor.utils.io.errors.IOException

data class GraphQLException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : IOException(message, cause)