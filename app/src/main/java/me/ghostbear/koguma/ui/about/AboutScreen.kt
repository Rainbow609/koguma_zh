package me.ghostbear.koguma.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Support
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import me.ghostbear.koguma.BuildConfig
import me.ghostbear.koguma.ui.Route
import me.ghostbear.koguma.ui.SmallAppBar

@Composable
fun AboutScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            SmallAppBar(
                title = {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        val uriHandler = LocalUriHandler.current

        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.displaySmall) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                AboutItem(
                    icon = Icons.Outlined.Code,
                    iconContentDescription = "repository",
                    text = {
                        Text(text = "GitHub")
                    },
                    action = Icons.Outlined.ChevronRight,
                    actionContentDescription = "external_link",
                    onClick = {
                        uriHandler.openUri("https://github.com/ghostbear/koguma")
                    }
                )
                AboutItem(
                    icon = Icons.Outlined.Support,
                    iconContentDescription = "donate",
                    text = {
                        Text(text = "Donate")
                    },
                    action = Icons.Outlined.ChevronRight,
                    actionContentDescription = "external_link",
                    onClick = {
                        uriHandler.openUri("https://github.com/sponsors/ghostbear")
                    }
                )
                Text(
                    text = "Support",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                AboutItem(
                    icon = Icons.Outlined.Help,
                    iconContentDescription = "faq",
                    text = {
                        Text(text = "FAQ")
                    },
                    action = Icons.Outlined.ChevronRight,
                    actionContentDescription = "external_link",
                    onClick = {
                        uriHandler.openUri("https://github.com/ghostbear/koguma/wiki")
                    }
                )
                AboutItem(
                    icon = Icons.Outlined.BugReport,
                    iconContentDescription = "bug_report",
                    text = {
                        Text(text = "Bug report")
                    },
                    action = Icons.Outlined.ChevronRight,
                    actionContentDescription = "external_link",
                    onClick = {
                        uriHandler.openUri("https://github.com/ghostbear/koguma/issues")
                    }
                )
                Text(
                    text = "Legal",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                AboutItem(
                    icon = Icons.Outlined.MenuBook,
                    iconContentDescription = "privacy_policy",
                    text = {
                        Text(text = "Privacy Policy")
                    },
                    action = Icons.Outlined.ChevronRight,
                    actionContentDescription = "external_link",
                    onClick = {
                        uriHandler.openUri("https://ghostbear.me/koguma/privacy-policy")
                    }
                )
                AboutItem(
                    icon = Icons.Outlined.Copyright,
                    iconContentDescription = "licenses",
                    text = {
                        Text(text = "Licenses")
                    },
                    action = Icons.Outlined.ChevronRight,
                    actionContentDescription = "external_link",
                    onClick = {
                        navController.navigate(Route.Libraries.route)
                    }
                )
                Text(
                    text = "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
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