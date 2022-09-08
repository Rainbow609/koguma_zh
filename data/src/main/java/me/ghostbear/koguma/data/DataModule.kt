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
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json
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

    companion object {

        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
            }
        }

        @Provides
        @Singleton
        fun provideHttpClient(json: Json): HttpClient {
            return HttpClient(CIO) {
                expectSuccess = true
                install(ContentNegotiation) {
                    json(json)
                }
                install(Logging) {
                    level = LogLevel.BODY
                }
            }
        }
    }
}
