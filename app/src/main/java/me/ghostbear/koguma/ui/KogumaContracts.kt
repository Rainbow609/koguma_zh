/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui

import androidx.activity.result.contract.ActivityResultContracts
import me.ghostbear.koguma.util.MimeType

object KogumaContracts {
    val OpenDocument = ActivityResultContracts.OpenDocument()
    val CreateJsonDocument = ActivityResultContracts.CreateDocument(MimeType.Json)
}
