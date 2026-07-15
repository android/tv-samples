package com.example.android.tvleanback.ui.screens

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.preference.PreferenceManager
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.example.android.tvleanback.R
import com.example.android.tvleanback.ui.OnboardingActivity.Companion.COMPLETED_ONBOARDING
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    val pageTitles = listOf(
        R.string.onboarding_title_welcome,
        R.string.onboarding_title_design,
        R.string.onboarding_title_simple,
        R.string.onboarding_title_project
    )
    val pageDescriptions = listOf(
        R.string.onboarding_description_welcome,
        R.string.onboarding_description_design,
        R.string.onboarding_description_simple,
        R.string.onboarding_description_project
    )
    val pageImages = listOf(
        R.drawable.tv_animation_a,
        R.drawable.tv_animation_b,
        R.drawable.tv_animation_c,
        R.drawable.tv_animation_d
    )

    val pagerState = rememberPagerState(pageCount = { pageTitles.size })

    LaunchedEffect(pagerState.currentPage) {
        focusRequester.requestFocus()
    }

    fun completeOnboarding() {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putBoolean(COMPLETED_ONBOARDING, true)
        editor.apply()
        onFinish()
    }

    TvLeanbackTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 24.dp, horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header / Logo
            Text(
                text = "Videos by Google",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AndroidView(
                        factory = { ctx ->
                            ImageView(ctx).apply {
                                scaleType = ImageView.ScaleType.CENTER_INSIDE
                                setPadding(0, 16, 0, 16)
                            }
                        },
                        update = { imageView ->
                            imageView.setImageResource(pageImages[page])
                            (imageView.drawable as? AnimationDrawable)?.start()
                        },
                        modifier = Modifier.size(180.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = pageTitles[page]),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = pageDescriptions[page]),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bottom section: Indicators and Action Buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    repeat(pageTitles.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }

                val isLastPage = pagerState.currentPage == pageTitles.size - 1
                Button(
                    onClick = {
                        if (isLastPage) {
                            completeOnboarding()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.focusRequester(focusRequester)
                ) {
                    Text(text = if (isLastPage) "GET STARTED" else "CONTINUE")
                }
            }
        }
    }
}
