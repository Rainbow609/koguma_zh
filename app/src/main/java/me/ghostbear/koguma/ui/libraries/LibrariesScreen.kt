package me.ghostbear.koguma.ui.libraries

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.withContext
import me.ghostbear.koguma.ui.SmallAppBar
import me.ghostbear.koguma.ui.libraries.components.LibraryList
import me.ghostbear.koguma.ui.libraries.components.LicenseDialog
import me.ghostbear.koguma.util.plus

@Composable
fun LibrariesScreen(navController: NavHostController) {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val dialog = rememberSaveable { mutableStateOf<Library?>(null) }

    Scaffold(
        topBar = {
            SmallAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "back")
                    }
                },
                title = {
                    Text(text = "Licenses")
                },
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { paddingValues ->
        val libs = remember { mutableStateOf<Libs?>(null) }

        val context = LocalContext.current
        LaunchedEffect(Unit) {
            libs.value = Libs.Builder().withContext(context).build()
        }

        val libraries = libs.value?.libraries
        if (libraries != null) {
            LibraryList(
                libraries = libraries,
                contentValues = paddingValues + WindowInsets.navigationBars.asPaddingValues(),
                onClickItem = { library ->  dialog.value = library }
            )
        }
    }

    val library = dialog.value
    if (library != null) {
        LicenseDialog(
            library = library,
            onDismissRequest = { dialog.value = null }
        )
    }
}
