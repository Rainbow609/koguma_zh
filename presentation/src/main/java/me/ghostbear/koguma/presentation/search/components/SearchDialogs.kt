package me.ghostbear.koguma.presentation.search.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.ghostbear.koguma.presentation.R

@Composable
fun OverrideDialog(
    onDismissRequest: () -> Unit,
    onOverride: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onOverride()
                onDismissRequest()
            }) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = android.R.string.cancel))
            }
        },
        title = {
            Text(text = stringResource(R.string.override))
        },
        text = {
            Text(text = stringResource(R.string.override_description))
        }
    )
}
