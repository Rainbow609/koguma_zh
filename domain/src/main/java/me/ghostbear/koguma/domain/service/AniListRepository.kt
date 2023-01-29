/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.service

import me.ghostbear.koguma.domain.model.Manga

interface AniListRepository {

    suspend fun search(query: String): List<Manga>
}
