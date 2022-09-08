/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.presentation.libraries

import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library

val Library.licenseContentAsHtml: String
    get() = licenses.firstOrNull()?.licenseContent?.replace("\n", "<br />").orEmpty()

val Library.creator: String
    get() = developers.takeIf { it.isNotEmpty() }?.map(Developer::name)?.joinToString() ?: organization?.name ?: ""
