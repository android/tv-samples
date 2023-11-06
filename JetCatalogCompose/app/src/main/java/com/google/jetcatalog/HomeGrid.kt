package com.google.jetcatalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.itemsIndexed
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeGrid() {
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .padding(start = 54.dp, top = 0.dp, end = 38.dp, bottom = 12.dp)
        ) {
            val firstChildFr = remember { FocusRequester() }

            TvLazyVerticalGrid(
                columns = TvGridCells.Fixed(4),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .focusRestorer { firstChildFr },
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item(span = { TvGridItemSpan(4) }) {
                    Text(text = "Foundations")
                }

                itemsIndexed(foundations) { index, item ->
                    FoundationsGridCard(
                        foundation = item,
                        modifier = Modifier.ifElse(
                            index == 0,
                            Modifier.focusRequester(firstChildFr)
                        )
                    )
                }

                item(span = { TvGridItemSpan(4) }) {
                    Text(text = "Components")
                }

                itemsIndexed(components) { index, item ->
                    ComponentsGridCard(component = item)
                }
            }
        }
    }
}