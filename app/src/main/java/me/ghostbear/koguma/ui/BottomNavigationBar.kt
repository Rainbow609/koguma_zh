/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import me.ghostbear.koguma.R

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val backgroundColor = NavigationBarDefaults.containerColor

    AnimatedVisibility(
        visible = currentDestination?.route in Route.rootScreens.map(Route::route),
        enter = expandVertically(expandFrom = Alignment.Bottom),
        exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        NavigationBar(
            modifier = Modifier
                .drawBehind { drawRect(backgroundColor) }
                .navigationBarsPadding()
        ) {
            Route.rootScreens.forEach { screen ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = screen.icon,
                    label = {
                        Text(text = stringResource(id = screen.textId))
                    }
                )
            }
        }
    }
}

sealed class Route(val route: String, @StringRes val textId: Int, val icon: @Composable () -> Unit) {
    object Home : Route(
        route = "home",
        textId = R.string.screen_home,
        icon = { Icon(Icons.Outlined.Home, contentDescription = "home") }
    )
    object Search : Route(
        route = "search",
        textId = R.string.screen_search,
        icon = { Icon(Icons.Outlined.Search, contentDescription = "search") }
    )
    object About : Route(
        route = "about",
        textId = R.string.screen_about,
        icon = { Icon(Icons.Outlined.Person, contentDescription = "about") }
    )
    object Libraries : Route(
        route = "libraries",
        textId = 0,
        icon = {}
    )
    companion object {
        val rootScreens = listOf(Home, Search, About)
    }
}