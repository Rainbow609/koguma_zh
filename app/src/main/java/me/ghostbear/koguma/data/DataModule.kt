/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.ghostbear.koguma.data.local.MangaRepositoryImpl
import me.ghostbear.koguma.data.remote.AniListRepositoryImpl
import me.ghostbear.koguma.domain.repository.AniListRepository
import me.ghostbear.koguma.domain.repository.MangaRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMangaRepository(mangaRepositoryImpl: MangaRepositoryImpl): MangaRepository

    @Binds
    abstract fun bindAniListRepository(aniListRepositoryImpl: AniListRepositoryImpl): AniListRepository

}