package me.ghostbear.koguma.ui.libraries

import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library

val Library.licenseContentAsHtml: String
    get() = licenses.firstOrNull()?.licenseContent?.replace("\n", "<br />").orEmpty()

val Library.creator: String
    get() = developers.takeIf { it.isNotEmpty() }?.map(Developer::name)?.joinToString() ?: organization?.name ?: ""
