package me.ghostbear.koguma.ui.libraries.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.entity.Library

@Composable
fun LibraryList(
    libraries: List<Library>,
    modifier: Modifier,
    contentValues: PaddingValues,
    onClickItem: (Library) -> Unit
) {
    LazyColumn(
        modifier = modifier,
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
