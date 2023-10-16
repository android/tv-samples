package com.google.jetcatalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ComponentsGridCard(
    component: Component,
    modifier: Modifier = Modifier
) {
    val image = getComponentGridCardImage(component)
    val navHostController = LocalNavController.current

    StandardCardLayout(
        modifier = modifier,
        imageCard = {
            CardLayoutDefaults.ImageCard(
                onClick = { navHostController.navigate(component.routeValue) },
                interactionSource = it,
                colors = CardDefaults.colors(containerColor = Color.Transparent)
            ) {
                Image(painter = painterResource(id = image), contentDescription = null)
            }
        },
        title = {
            Text(
                text = component.title,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    )
}
