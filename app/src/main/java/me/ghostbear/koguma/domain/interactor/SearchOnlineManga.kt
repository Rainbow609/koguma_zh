/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.interactor

import javax.inject.Inject
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.repository.AniListRepository

class SearchOnlineManga @Inject constructor(
    private val aniListRepository: AniListRepository
) {

    suspend fun await(query: String): List<Manga> {
        return aniListRepository.search(query)
    }

}