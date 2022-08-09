/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.repository

import android.net.Uri
import me.ghostbear.koguma.domain.model.Manga

interface MangaRepository {

    suspend fun read(uri: Uri): Manga

    suspend fun write(uri: Uri, manga: Manga)
}
