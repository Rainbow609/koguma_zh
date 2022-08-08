package me.ghostbear.koguma.ui.libraries.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mikepenz.aboutlibraries.entity.Library
import me.ghostbear.koguma.util.decodeHtml

@Composable
fun LicenseDialog(
    library: Library,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = library.licenses
                        .firstOrNull()
                        ?.licenseContent
                        ?.replace("\n", "<br />")
                        .orEmpty()
                        .decodeHtml()
                )
            }
        }
    )
}