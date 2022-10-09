/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import me.ghostbear.koguma.data.SearchByTypeQuery
import me.ghostbear.koguma.data.mangaRemoteToDomain
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.repository.AniListRepository
import javax.inject.Inject

class AniListRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : AniListRepository {

    override suspend fun search(query: String): List<Manga> {
        val response = try {
            apolloClient.query(
                query = SearchByTypeQuery(query = Optional.present(query))
            ).execute()
        } catch (e: ApolloException) {
            Log.e("AniListRepositoryImpl", "Apollo client failed to fetch data", e)
            throw e
        } catch (e: Exception) {
            Log.e("AniListRepositoryImpl", "Unknown exception", e)
            throw e
        }

        if (response.data == null) {
            throw IllegalStateException("Server response didn't include data or errors")
        }

        return response.data!!.page?.media
            ?.mapNotNull { it?.let { mangaRemoteToDomain(it) } } ?: emptyList()
    }
}
