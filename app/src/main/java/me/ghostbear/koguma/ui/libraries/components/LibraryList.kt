package me.ghostbear.koguma.ui.libraries.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.mikepenz.aboutlibraries.entity.Library

@Composable
fun LibraryList(
    libraries: List<Library>,
    contentValues: PaddingValues,
    onClickItem: (Library) -> Unit
) {
    LazyColumn(
        contentPadding = contentValues
    ) {
        items(libraries) { library ->
            LibraryItem(
                library = library,
                onClick = { onClickItem(library) }
            )
        }
    }
}
