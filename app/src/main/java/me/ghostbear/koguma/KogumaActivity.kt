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
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import me.ghostbear.koguma.ui.BottomNavigationBar
import me.ghostbear.koguma.ui.Route
import me.ghostbear.koguma.ui.main.MainScreen
import me.ghostbear.koguma.ui.main.MainState
import me.ghostbear.koguma.ui.main.MainStateImpl
import me.ghostbear.koguma.ui.main.MainViewModel
import me.ghostbear.koguma.ui.main.mainViewModel
import me.ghostbear.koguma.ui.search.SearchScreen
import me.ghostbear.koguma.ui.theme.KogumaTheme

@AndroidEntryPoint
class KogumaActivity : ComponentActivity() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun mainViewModelFactory(): MainViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            KogumaTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navController,
                            )
                        }
                    ) { paddingValues ->
                        NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(paddingValues)) {
                            composable(Route.Home.route) {
                                MainScreen(
                                    viewModel = mainViewModel(MainState() as MainStateImpl)
                                )
                            }
                            composable(Route.Search.route) {
                                SearchScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
