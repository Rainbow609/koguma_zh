/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.data.local

import dagger.Lazy
import me.ghostbear.koguma.data.local.json.JsonMetadataManager
import me.ghostbear.koguma.data.local.xml.XmlMetadataManager
import javax.inject.Inject

class MetadataManagerFactory @Inject constructor(
    private val xmlMetadataManager: Lazy<XmlMetadataManager>,
    private val jsonMetadataManager: Lazy<JsonMetadataManager>
) {

    enum class Type {
        XML, JSON
    }

    fun create(type: Type): MetadataManager = when (type) {
        Type.XML -> xmlMetadataManager.get()
        Type.JSON -> jsonMetadataManager.get()
    }
}
