/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui.about.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AboutItem(
    icon: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit),
    action: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                text()
            }
            if (action != null) {
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    action()
                }
            }
        }
    }
}

@Composable
fun AboutItem(
    icon: ImageVector? = null,
    iconContentDescription: String? = null,
    text: (@Composable () -> Unit),
    action: ImageVector? = null,
    actionContentDescription: String? = null,
    onClick: () -> Unit
) {
    val icon: (@Composable () -> Unit)? = icon?.let { { Icon(imageVector = it, contentDescription = iconContentDescription) } }
    val action: (@Composable () -> Unit)? = action?.let { { Icon(imageVector = it, contentDescription = actionContentDescription) } }
    AboutItem(
        icon = icon,
        text = text,
        action = action,
        onClick = onClick,
    )
}
