package me.ghostbear.koguma.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind

@Composable
fun SmallAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val scrollFraction = scrollBehavior?.state?.overlappedFraction ?: 0f
    val backgroundColor by TopAppBarDefaults.smallTopAppBarColors().containerColor(scrollFraction)
    SmallTopAppBar(
        modifier = modifier
            .drawBehind { drawRect(backgroundColor) }
            .statusBarsPadding(),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}