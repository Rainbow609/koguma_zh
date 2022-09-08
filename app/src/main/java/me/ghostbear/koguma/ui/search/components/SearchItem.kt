package me.ghostbear.koguma.ui.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.ui.search.authorWithArtist

@Composable
fun SearchItem(
    manga: Manga,
    onClickEdit: () -> Unit,
    onClickSave: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(
        Modifier.clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        ) {
            if (manga.title != null)
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            Text(text = manga.authorWithArtist)
            if (manga.status != null)
                Text(
                    text = manga.status.name,
                    style = MaterialTheme.typography.bodySmall
                )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                if (manga.description != null)
                    Text(
                        text = manga.description,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                if (manga.genre != null)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(manga.genre) { genre ->
                            SuggestionChip(
                                onClick = { /*TODO*/ },
                                enabled = false,
                                label = {
                                    Text(text = genre)
                                }
                            )
                        }
                    }
            }
        }
        Row {
            IconButton(onClick = { expanded = expanded.not() }) {
                val icon = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore
                Icon(imageVector = icon, contentDescription = "expand")
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClickEdit) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit")
            }
            IconButton(onClick = onClickSave) {
                Icon(imageVector = Icons.Outlined.Save, contentDescription = "save")
            }
        }
    }
}

@Preview
@Composable
fun SearchItemPreview() {
    SearchItem(
        manga = Manga(
            title = "Kuitsume Youhei no Gensou Kitan",
            author = "Mine",
            artist = "Area Ikemiya",
            description = "When seasoned mercenary Loren is the sole survivor of a disastrous battle that destroys the rest of his company, he must find a new way to survive in the world. With no friends or connections, he has no hope of joining an adventuring party–until enigmatic priestess Lapis offers to partner up with him. But there’s more to Lapis than meets the eye, and Loren soon finds himself bound to a fate stranger than he imagined.",
            genre = listOf(
                "Action",
                "Adventure",
                "Fantasy",
                "Romance"
            ),
            status = Status.Ongoing
        ),
        onClickEdit = {},
        onClickSave = {}
    )
}
