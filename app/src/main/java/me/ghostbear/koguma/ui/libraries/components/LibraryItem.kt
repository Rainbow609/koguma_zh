package me.ghostbear.koguma.ui.libraries.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import me.ghostbear.koguma.ui.libraries.creator

@Composable
fun LibraryItem(
    library: Library,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = library.name,
                modifier = Modifier.weight(0.1f),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val version = library.artifactVersion
            if (version != null) {
                Text(text = version)
            }
        }
        val creator = library.creator
        if (creator.isNotEmpty()) {
            Text(text = creator)
        }
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            library.licenses.forEach {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
