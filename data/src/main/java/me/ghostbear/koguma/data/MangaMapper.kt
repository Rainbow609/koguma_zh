/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data

import me.ghostbear.koguma.data.local.Manga
import me.ghostbear.koguma.data.local.Status
import me.ghostbear.koguma.data.type.MediaStatus
import me.ghostbear.koguma.extensions.decodeHtml
import me.ghostbear.koguma.domain.model.Manga as DomainManga
import me.ghostbear.koguma.domain.model.Status as DomainStatus

val mangaRemoteToDomain: (SearchByTypeQuery.Medium) -> DomainManga = {
    DomainManga(
        title = it.title?.romaji,
        artist = it.staff?.edges
            ?.filter {
                it?.role == "Story & Art" || it?.role == "Art"
            }
            ?.mapNotNull { it?.node?.name?.full }
            ?.joinToString(),
        author = it.staff?.edges
            ?.filter {
                it?.role == "Story & Art" || it?.role == "Story"
            }
            ?.mapNotNull { it?.node?.name?.full }
            ?.joinToString(),
        description = it.description?.decodeHtml(),
        genre = it.genres?.filterNotNull(),
        status = when (it.status) {
            MediaStatus.FINISHED -> DomainStatus.PublishingFinished
            MediaStatus.RELEASING -> DomainStatus.Ongoing
            MediaStatus.NOT_YET_RELEASED -> DomainStatus.Unknown
            MediaStatus.CANCELLED -> DomainStatus.Cancelled
            MediaStatus.HIATUS -> DomainStatus.OnHaitus
            else -> DomainStatus.Unknown
        },
        cover = it.coverImage?.run {
            extraLarge ?: large ?: medium
        }
    )
}

val mangaDataToDomain: (Manga) -> DomainManga = {
    DomainManga(
        title = it.title,
        artist = it.artist,
        author = it.author,
        description = it.description,
        genre = it.genre,
        status = statusDataToDomain.invoke(it.status)
    )
}

val statusDataToDomain: (Status?) -> DomainStatus = {
    when (it) {
        Status.Ongoing -> DomainStatus.Ongoing
        Status.Completed -> DomainStatus.Completed
        Status.Licensed -> DomainStatus.Licensed
        Status.PublishingFinished -> DomainStatus.PublishingFinished
        Status.Cancelled -> DomainStatus.Cancelled
        Status.OnHaitus -> DomainStatus.OnHaitus
        else -> DomainStatus.Unknown
    }
}

val mangaDomainToData: (DomainManga) -> Manga = {
    Manga(
        title = it.title,
        artist = it.artist,
        author = it.author,
        description = it.description,
        genre = it.genre,
        status = statusDomainToData.invoke(it.status)
    )
}

val statusDomainToData: (DomainStatus?) -> Status = {
    when (it) {
        DomainStatus.Ongoing -> Status.Ongoing
        DomainStatus.Completed -> Status.Completed
        DomainStatus.Licensed -> Status.Licensed
        DomainStatus.PublishingFinished -> Status.PublishingFinished
        DomainStatus.Cancelled -> Status.Cancelled
        DomainStatus.OnHaitus -> Status.OnHaitus
        else -> Status.Unknown
    }
}
