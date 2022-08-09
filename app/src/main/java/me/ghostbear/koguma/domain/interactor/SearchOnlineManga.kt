/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.interactor

import io.ktor.client.network.sockets.ConnectTimeoutException
import me.ghostbear.koguma.data.remote.graphql.GraphQLException
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.repository.AniListRepository
import java.io.IOException
import javax.inject.Inject

class SearchOnlineManga @Inject constructor(
    private val aniListRepository: AniListRepository
) {

    suspend fun await(query: String): Result {
        return try {
            val list = aniListRepository.search(query)
            Result.Success(list)
        } catch (e: ConnectTimeoutException) {
            Result.ConnectionTimeout
        } catch (e: IOException) {
            Result.NetworkError
        } catch (e: GraphQLException) {
            Result.GraphQLQueryMalformed
        } catch (e: IllegalArgumentException) {
            Result.IllegalResponse
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    sealed class Result {
        data class Error(val error: Throwable) : Result()
        object ConnectionTimeout : Result()
        object NetworkError : Result()
        object GraphQLQueryMalformed : Result()
        object IllegalResponse : Result()
        data class Success(val list: List<Manga>) : Result()
    }
}
