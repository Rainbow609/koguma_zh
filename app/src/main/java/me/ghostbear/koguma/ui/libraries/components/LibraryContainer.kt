package me.ghostbear.koguma.ui.libraries.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.withContext

@Composable
fun LibraryContainer(
    modifier: Modifier,
    contentPadding: PaddingValues,
    onOpenDialog: (Library) -> Unit
) {
    val libs = remember { mutableStateOf<Libs?>(null) }

    val context = LocalContext.current
    LaunchedEffect(libs.value) {
        libs.value = Libs.Builder().withContext(context).build()
    }

    val libraries = libs.value?.libraries
    if (libraries != null) {
        LibraryList(
            libraries = libraries,
            modifier = modifier,
            contentValues = contentPadding,
            onClickItem = onOpenDialog
        )
    }
}
