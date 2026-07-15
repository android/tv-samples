package com.example.android.tvleanback.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.example.android.tvleanback.R
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GuidedStepScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(1) }
    var selectedOption by remember { mutableStateOf("Option A") }

    val optionNames = listOf("Option A", "Option B", "Option C")
    val optionDescriptions = listOf(
        "Here's one thing you can do",
        "Here's another thing you can do",
        "Here's one more thing you can do"
    )

    TvLeanbackTheme {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Left Guidance Pane (40% width)
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(48.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_main_icon),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = when (currentStep) {
                            1 -> stringResource(id = R.string.guidedstep_first_breadcrumb)
                            2 -> stringResource(id = R.string.guidedstep_second_breadcrumb)
                            else -> stringResource(id = R.string.guidedstep_third_breadcrumb)
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (currentStep) {
                            1 -> stringResource(id = R.string.guidedstep_first_title)
                            2 -> stringResource(id = R.string.guidedstep_second_title)
                            else -> stringResource(id = R.string.guidedstep_third_title)
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = when (currentStep) {
                            1 -> stringResource(id = R.string.guidedstep_first_description)
                            2 -> stringResource(id = R.string.guidedstep_second_description)
                            else -> "${stringResource(id = R.string.guidedstep_third_command)} $selectedOption"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Right Actions Pane (60% width)
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .padding(48.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    label = "guided_step_actions"
                ) { step ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when (step) {
                            1 -> {
                                Button(
                                    onClick = { currentStep = 2 },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.guidedstep_continue),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                Button(
                                    onClick = onFinish,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.guidedstep_cancel),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                            2 -> {
                                optionNames.forEachIndexed { index, name ->
                                    ListItem(
                                        selected = selectedOption == name,
                                        onClick = {
                                            selectedOption = name
                                            currentStep = 3
                                        },
                                        headlineContent = { Text(text = name) },
                                        supportingContent = { Text(text = optionDescriptions[index]) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            3 -> {
                                Button(
                                    onClick = onFinish,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Done", modifier = Modifier.padding(8.dp))
                                }
                                Button(
                                    onClick = { currentStep = 2 },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = "Back", modifier = Modifier.padding(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
