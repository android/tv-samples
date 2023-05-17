/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetstream.R
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.tvmaterial.StandardDialog

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AccountsSectionDeleteDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dialogWidth = remember(screenWidth) { screenWidth.div(2.25f) }

    StandardDialog(
        showDialog = showDialog,
        modifier = Modifier.width(dialogWidth),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            AccountsSectionDialogButton(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.yes_delete_account),
                shouldRequestFocus = true,
                onClick = onDismissRequest
            )
        },
        dismissButton = {
            AccountsSectionDialogButton(
                modifier = Modifier.padding(end = 8.dp),
                text = stringResource(R.string.no_keep_it),
                shouldRequestFocus = false,
                onClick = onDismissRequest
            )
        },
        title = {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.delete_account_dialog_title),
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.delete_account_dialog_text),
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        containerColor = MaterialTheme.colorScheme.onSurface,
        shape = JetStreamCardShape
    )
}
