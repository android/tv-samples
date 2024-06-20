package com.google.jetfit.presentation.screens.training.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.google.jetfit.R
import com.google.jetfit.components.CustomFillButton
import com.google.jetfit.presentation.screens.training.TrainingFilterData

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FilterSideMenu(
    onDismissSideMenu: () -> Unit,
    filtrationFields: List<TrainingFilterData>
){
    TvLazyColumn(
            contentPadding = PaddingValues(vertical = 32.dp, horizontal = 20.dp)
    ) {

        item {
            Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                        text = stringResource(R.string.filters),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                )
                CustomFillButton(
                        text = stringResource(R.string.clear),
                        onClick = onDismissSideMenu
                )
            }

        }
        items(filtrationFields) { field ->
            ListItem(
                    selected = false,
                    onClick = {},
                    leadingContent={
                        Icon(
                            painter = painterResource(id = field.icon),
                            modifier = Modifier.size(ListItemDefaults.IconSize),
                            contentDescription = "field icon"
                        )
                    },
                    trailingContent = {
                        Icon(
                                Icons.Default.KeyboardArrowRight,
                                modifier = Modifier.size(ListItemDefaults.IconSize),
                                contentDescription = "back icon"
                        )
                    },
                    headlineContent = {
                        Text(
                                text = stringResource(id = field.title),
                                style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingContent = {
                        Text(
                                text =  field.description,
                                style = MaterialTheme.typography.bodySmall
                        )

                    },
                    colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedContentColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = ListItemDefaults.shape(shape = RoundedCornerShape(16.dp))
            )
        }
    }
}