/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import me.ghostbear.koguma.data.mangaRemoteLocalMapper
import me.ghostbear.koguma.data.remote.anilist.SearchForManga
import me.ghostbear.koguma.data.remote.anilist.SearchForMangaQuery
import me.ghostbear.koguma.data.remote.graphql.query
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.repository.AniListRepository
import java.io.IOException
import javax.inject.Inject

class AniListRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : AniListRepository {

    override suspend fun search(query: String): List<Manga> {
        val response = try {
            httpClient.query<SearchForManga>(
                urlString = "https://graphql.anilist.co",
                query = SearchForMangaQuery(query)
            )
        } catch (e: ConnectTimeoutException) {
            Log.e("AniListRepositoryImpl", "Connection timeout", e)
            throw e
        } catch (e: RedirectResponseException) {
            Log.e("AniListRepositoryImpl", "Client was redirect", e)
            throw e
        } catch (e: ClientRequestException) {
            Log.e("AniListRepositoryImpl", "Client didn't request properly", e)
            throw e
        } catch (e: ServerResponseException) {
            Log.e("AniListRepositoryImpl", "Server didn't respond properly", e)
            throw e
        } catch (e: SerializationException) {
            Log.e("AniListRepositoryImpl", "Couldn't decode response data", e)
            throw e
        } catch (e: IOException) {
            Log.e("AniListRepositoryImpl", "Unknown IO exception", e)
            throw e
        } catch (e: Exception) {
            Log.e("AniListRepositoryImpl", "Unknown exception", e)
            throw e
        }

        if (response.data == null) {
            throw IllegalArgumentException("Server response didn't include data or errors")
        }

        return response.data.page?.media
            ?.map(mangaRemoteLocalMapper) ?: emptyList()
    }
}
