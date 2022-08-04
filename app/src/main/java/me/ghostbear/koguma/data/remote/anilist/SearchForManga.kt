/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.remote.anilist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.put
import me.ghostbear.koguma.data.remote.graphql.Query

@Serializable
data class SearchForManga(
    @SerialName("Page")
    val page: Page? = null
)

fun SearchForMangaQuery(query: String): Query {
    return Query(
        operationName = "SearchForManga",
        query = """
            query SearchForManga(${'$'}query: String) {
              Page {
                media(search: ${'$'}query, type: MANGA) {
                  title {
                    romaji
                  }
                  staff {
                    edges {
                      role
                      node {
                        name {
                          full
                        }
                      }
                    }
                  } 
                  description
                  genres
                  status
                }
              }
            }
        """.trimIndent(),
        variables = {
            query.takeIf { it.isNotEmpty() }?.let {
                put("query", query)
            }
        }
    )
}
