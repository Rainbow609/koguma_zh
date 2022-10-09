package me.ghostbear.koguma.presentation.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import me.ghostbear.koguma.domain.model.Manga
import me.ghostbear.koguma.domain.model.Status
import me.ghostbear.koguma.presentation.R
import me.ghostbear.koguma.presentation.search.authorWithArtist

@Composable
fun MangaCover(
    modifier: Modifier = Modifier,
    cover: String?
) {
    val modifier = modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .fillMaxWidth(1f / 3f)
        .aspectRatio(5f / 7f)

    if (LocalInspectionMode.current) {
        EmptyMangaCover(modifier)
        return
    }

    SubcomposeAsyncImage(model = cover, contentDescription = "") {
        val state = painter.state
        val rotationAnimation by animateFloatAsState(
            targetValue = if (state is AsyncImagePainter.State.Error) 540f else 0f,
            animationSpec = tween(
                durationMillis = 3750,
                easing = EaseInOutElastic
            )
        )
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                val infiniteTransition = rememberInfiniteTransition()
                val rotationAnimation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 2500,
                            easing = EaseInOutElastic
                        )
                    )
                )
                EmptyMangaCover(
                    modifier = modifier,
                    rotation = rotationAnimation
                )
            }
            is AsyncImagePainter.State.Error -> {
                EmptyMangaCover(
                    modifier = modifier,
                    rotation = rotationAnimation
                )
            }
            else -> {
                SubcomposeAsyncImageContent(
                    modifier = modifier,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun EmptyMangaCover(
    modifier: Modifier = Modifier,
    rotation: Float = 0f
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(rotation)
        )
    }
}

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
        Row {
            MangaCover(cover = manga.cover)
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
            ) {
                if (manga.title != null)
                    Text(
                        text = manga.title!!,
                        style = MaterialTheme.typography.bodyLarge
                    )
                if (manga.authorWithArtist.isNotBlank())
                    Text(
                        text = manga.authorWithArtist,
                        style = MaterialTheme.typography.bodyMedium
                    )
                if (manga.status != null)
                    Text(
                        text = manga.status!!.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                if (manga.description != null)
                    Text(
                        text = manga.description!!,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                if (manga.genre != null)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(manga.genre!!) { genre ->
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

@Preview(name = "All information")
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

@Preview(name = "No author")
@Composable
fun SearchItemPreviewNoAuthor() {
    SearchItem(
        manga = Manga(
            title = "Kuitsume Youhei no Gensou Kitan",
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

@Preview(name = "No artist")
@Composable
fun SearchItemPreviewNoArtist() {
    SearchItem(
        manga = Manga(
            title = "Kuitsume Youhei no Gensou Kitan",
            author = "Mine",
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

@Preview(name = "No artist")
@Composable
fun SearchItemPreviewBadData() {
    SearchItem(
        manga = Manga(
            title = "Kuitsume Youhei no Gensou Kitan",
            author = null,
            description = null,
            genre = null,
            status = null
        ),
        onClickEdit = {},
        onClickSave = {}
    )
}
