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

package com.google.jetstream.presentation.screens.movies

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClassicCard
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieCast
import com.google.jetstream.presentation.screens.dashboard.rememberChildPadding
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.utils.FocusGroup
import com.google.jetstream.presentation.utils.ourColors

@Composable
fun CastAndCrewList(castAndCrew: List<MovieCast>) {
    val childPadding = rememberChildPadding()

    Column(
        modifier = Modifier.padding(top = childPadding.top)
    ) {
        Text(
            text = stringResource(R.string.cast_and_crew),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(start = childPadding.start)
        )
        FocusGroup {
            TvLazyRow(
                modifier = Modifier.padding(top = 16.dp),
                pivotOffsets = PivotOffsets(
                    parentFraction = 0.07f
                )
            ) {
                item { Spacer(modifier = Modifier.padding(start = childPadding.start)) }

                items(castAndCrew.size) { index ->
                    val castMember = remember(index) { castAndCrew[index] }
                    key(castMember.id) {
                        CastAndCrewItem(
                            modifier = Modifier.restorableFocus(),
                            castMember = castMember
                        )
                    }
                }
            }
        }
    }
}

@Composable

private fun CastAndCrewItem(
    castMember: MovieCast,
    modifier: Modifier = Modifier,
) {
    val cardWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.15f)
    ClassicCard(
        modifier = modifier
            .width(cardWidth)
            .padding(end = 20.dp, bottom = 16.dp)
            .aspectRatio(1 / 1.8f),
        shape = CardDefaults.shape(shape = JetStreamCardShape),
        scale = CardDefaults.scale(focusedScale = 1f),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = JetStreamBorderWidth,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                shape = JetStreamCardShape
            )
        ),
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .padding(horizontal = 12.dp),
                text = castMember.realName,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis
            )
        },
        subtitle = {
            Text(
                text = castMember.characterName,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alpha(0.75f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                overflow = TextOverflow.Ellipsis
            )
        },
        image = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.725f)
                    .background(ourColors.random())
            )
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(castMember.avatarUrl)
//                    .build(),
//                contentDescription = StringConstants
//                    .Composable
//                    .ContentDescription
//                    .image(castMember.realName),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.725f),
//                contentScale = ContentScale.Crop
//            )
        },
        onClick = {}
    )
}
