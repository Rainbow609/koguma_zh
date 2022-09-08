package me.ghostbear.koguma.presentation.search.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.delay
import me.ghostbear.koguma.presentation.R
import me.ghostbear.koguma.presentation.SmallAppBar
import me.ghostbear.koguma.presentation.search.SearchState

@Composable
fun SearchToolbar(
    state: SearchState,
    onSearch: () -> Unit
) {
    when {
        state.searchQuery != null -> {
            SearchSearchToolbar(
                value = state.searchQuery!!,
                onValueChange = { state.searchQuery = it },
                onCancelClick = { state.searchQuery = null },
                onResetClick = { state.searchQuery = "" },
                onSearch = onSearch
            )
        }
        else -> {
            SearchRegularToolbar(
                onSearchClick = { state.searchQuery = "" }
            )
        }
    }
}

@Composable
fun SearchRegularToolbar(
    onSearchClick: () -> Unit
) {
    SmallAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = stringResource(R.string.search))
            }
        }
    )
}

@Composable
fun SearchSearchToolbar(
    value: String,
    onValueChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onResetClick: () -> Unit,
    onSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    SmallAppBar(
        navigationIcon = {
            IconButton(onClick = {
                focusRequester.freeFocus()
                onCancelClick()
            }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        title = {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearch()
                        focusRequester.freeFocus()
                    }
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground)
            )
        },
        actions = {
            IconButton(onClick = {
                onResetClick()
                focusRequester.requestFocus()
            }) {
                Icon(imageVector = Icons.Outlined.Clear, contentDescription = stringResource(R.string.clear))
            }
        }
    )
    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
    }
}
