package me.ghostbear.koguma.extensions

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import dagger.hilt.android.EntryPointAccessors
import me.ghostbear.koguma.KogumaActivity
import me.ghostbear.koguma.presentation.main.MainStateImpl
import me.ghostbear.koguma.presentation.main.MainViewModel
import me.ghostbear.koguma.presentation.search.SearchStateImpl
import me.ghostbear.koguma.presentation.search.SearchViewModel

@Composable
fun mainViewModel(
    state: MainStateImpl,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): MainViewModel {
    val view = LocalView.current
    val factory = EntryPointAccessors.fromActivity(
        (view.context as Activity),
        KogumaActivity.ViewModelFactoryProvider::class.java
    ).mainViewModelFactory()
    return hiltViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        delegateFactory = MainViewModel.provideFactory(factory, state)
    )
}

@Composable
fun searchViewModel(
    state: SearchStateImpl,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): SearchViewModel {
    val view = LocalView.current
    val factory = EntryPointAccessors.fromActivity(
        (view.context as Activity),
        KogumaActivity.ViewModelFactoryProvider::class.java
    ).searchViewModelFactory()
    return hiltViewModel(
        viewModelStoreOwner,
        SearchViewModel.provideFactory(factory, state)
    )
}
