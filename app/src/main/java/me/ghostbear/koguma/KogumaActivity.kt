/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ghostbear.koguma.extensions.mainViewModel
import me.ghostbear.koguma.extensions.searchViewModel
import me.ghostbear.koguma.extensions.toast
import me.ghostbear.koguma.presentation.BottomNavigationBar
import me.ghostbear.koguma.presentation.Route
import me.ghostbear.koguma.presentation.about.AboutScreen
import me.ghostbear.koguma.presentation.libraries.LibrariesScreen
import me.ghostbear.koguma.presentation.main.MainScreen
import me.ghostbear.koguma.presentation.main.MainState
import me.ghostbear.koguma.presentation.main.MainStateImpl
import me.ghostbear.koguma.presentation.main.MainViewModel
import me.ghostbear.koguma.presentation.search.SearchScreen
import me.ghostbear.koguma.presentation.search.SearchState
import me.ghostbear.koguma.presentation.search.SearchStateImpl
import me.ghostbear.koguma.presentation.search.SearchViewModel
import me.ghostbear.koguma.presentation.theme.KogumaTheme

@AndroidEntryPoint
class KogumaActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun mainViewModelFactory(): MainViewModel.Factory

        fun searchViewModelFactory(): SearchViewModel.Factory
    }

    private var isConfirming = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            KogumaTheme {
                val navController = rememberNavController()

                BackHandler(enabled = true) {
                    if (isConfirming) {
                        finish()
                    } else {
                        lifecycleScope.launch(Dispatchers.Main) {
                            isConfirming = true
                            val toast = toast("Press back to confirm exit")
                            delay(1500)
                            toast.cancel()
                            isConfirming = false
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            val visible = remember(navController.currentDestination) {
                                navController.currentDestination?.route !in Route.rootScreens.map(
                                    me.ghostbear.koguma.presentation.Route::route
                                )
                            }
                            AnimatedVisibility(
                                visible = visible,
                                enter = expandVertically(expandFrom = Alignment.Bottom),
                                exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
                            ) {
                                BottomNavigationBar(
                                    navController = navController,
                                )
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Route.Home.route,
                            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                        ) {
                            composable(Route.Home.route) {
                                MainScreen(
                                    viewModel = mainViewModel(
                                        MainState() as MainStateImpl
                                    )
                                )
                            }
                            composable(Route.Search.route) { currentBackStackEntry ->
                                val homeEntry = remember(currentBackStackEntry) {
                                    navController.getBackStackEntry(Route.Home.route)
                                }
                                SearchScreen(
                                    navController = navController,
                                    mainViewModel = mainViewModel(
                                        MainState() as MainStateImpl,
                                        homeEntry
                                    ),
                                    viewModel = searchViewModel(
                                        SearchState() as SearchStateImpl
                                    )
                                )
                            }
                            composable(Route.About.route) {
                                AboutScreen(navController)
                            }
                            composable(Route.Libraries.route) {
                                LibrariesScreen(
                                    navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
