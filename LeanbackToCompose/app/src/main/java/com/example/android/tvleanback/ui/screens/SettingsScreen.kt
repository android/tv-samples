package com.example.android.tvleanback.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Switch
import androidx.tv.material3.Text
import com.example.android.tvleanback.R
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val recKey = stringResource(id = R.string.pref_key_recommendations)

    val autoplayPreviewsKey = stringResource(id = R.string.pref_key_autoplay_previews)

    var recommendationsEnabled by remember {
        mutableStateOf(prefs.getBoolean(recKey, true))
    }
    var autoplayPreviewsEnabled by remember {
        mutableStateOf(prefs.getBoolean(autoplayPreviewsKey, true))
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 64.dp, vertical = 48.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.65f)
            ) {
                Text(
                    text = stringResource(id = R.string.pref_title_settings),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Manage your application preferences and account status",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(36.dp))

                // Autoplay Next Video Switch Item
                ListItem(
                    selected = false,
                    onClick = {
                        val newValue = !recommendationsEnabled
                        recommendationsEnabled = newValue
                        prefs.edit().putBoolean(recKey, newValue).apply()
                    },
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.pref_title_recommendations),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Automatically play the next video in the queue when playback ends",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = recommendationsEnabled,
                            onCheckedChange = null // Handled by ListItem onClick
                        )
                    },
                    scale = ListItemDefaults.scale(focusedScale = 1.02f),
                    border = ListItemDefaults.border(
                        focusedBorder = androidx.tv.material3.Border(
                            border = BorderStroke(2.dp, Color.White)
                        )
                    ),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Autoplay Video Previews Switch Item
                ListItem(
                    selected = false,
                    onClick = {
                        val newValue = !autoplayPreviewsEnabled
                        autoplayPreviewsEnabled = newValue
                        prefs.edit().putBoolean(autoplayPreviewsKey, newValue).apply()
                    },
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.pref_title_autoplay_previews),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Play silent video previews when focusing on cards",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = autoplayPreviewsEnabled,
                            onCheckedChange = null // Handled by ListItem onClick
                        )
                    },
                    scale = ListItemDefaults.scale(focusedScale = 1.02f),
                    border = ListItemDefaults.border(
                        focusedBorder = androidx.tv.material3.Border(
                            border = BorderStroke(2.dp, Color.White)
                        )
                    ),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Login Item
                ListItem(
                    selected = false,
                    onClick = onLoginClick,
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.pref_title_login),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = stringResource(id = R.string.pref_title_login_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    scale = ListItemDefaults.scale(focusedScale = 1.02f),
                    border = ListItemDefaults.border(
                        focusedBorder = androidx.tv.material3.Border(
                            border = BorderStroke(2.dp, Color.White)
                        )
                    ),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
