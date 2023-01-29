/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data

import com.apollographql.apollo3.ApolloClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.ghostbear.koguma.data.local.MangaRepositoryImpl
import me.ghostbear.koguma.data.remote.AniListRepositoryImpl
import me.ghostbear.koguma.domain.service.AniListRepository
import me.ghostbear.koguma.domain.service.MangaRepository
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML
import javax.inject.Singleton

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
        fun provideApolloClient(): ApolloClient {
            return ApolloClient.Builder()
                .serverUrl("https://graphql.anilist.co/")
                .build()
        }

        @Provides
        @Singleton
        fun provideXml(): XML {
            return XML {
                unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
                autoPolymorphic = true
                xmlDeclMode = XmlDeclMode.Charset
                indent = 4
                xmlVersion = XmlVersion.XML10
            }
        }
    }
}
