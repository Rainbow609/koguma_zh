/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data

import me.ghostbear.koguma.data.remote.anilist.Media
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.extensions.decodeHtml

val mangaRemoteLocalMapper: (Media) -> Manga = {
    Manga(
        title = it.title?.romaji,
        artist = it.staff?.edges
            ?.filter {
                it.role == "Story & Art" || it.role == "Art"
            }
            ?.mapNotNull { it.node?.name?.full }
            ?.joinToString(),
        author = it.staff?.edges
            ?.filter {
                it.role == "Story & Art" || it.role == "Story"
            }
            ?.mapNotNull { it.node?.name?.full }
            ?.joinToString(),
        description = it.description?.decodeHtml(),
        genre = it.genres,
        status = when (it.status) {
            "FINISHED" -> Status.PublishingFinished
            "RELEASING" -> Status.Ongoing
            "NOT_YET_RELEASED" -> Status.Unknown
            "CANCELLED" -> Status.Cancelled
            "HIATUS" -> Status.OnHaitus
            else -> Status.Unknown
        }
    )
}
