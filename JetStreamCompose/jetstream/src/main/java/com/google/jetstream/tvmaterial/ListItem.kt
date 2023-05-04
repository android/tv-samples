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

package com.google.jetstream.tvmaterial

import androidx.annotation.FloatRange
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ProvideTextStyle
import androidx.tv.material3.Surface

/**
 * Material Design list item
 *
 * Lists are continuous, vertical indexes of text or images.
 *
 * This component can be used to achieve the list item templates existing in the spec. One-line list
 * items have a singular line of headline text. Two-line list items additionally have either
 * supporting or overline text. Three-line list items have either both supporting and overline text,
 * or extended (two-line) supporting text.
 *
 * @param isSelected whether or not this ListItem is selected
 * @param onSelectionChanged callback to be called when this ListItem is clicked. Exposes updated
 * [isSelected] value.
 * @param headlineText the headline text of the list item
 * @param modifier [Modifier] to be applied to the list item
 * @param enabled controls the enabled state of this list item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param icon the leading supporting visual of the list item
 * @param overlineText the text composable displayed above the headline text
 * @param subtitleText the supporting text composable of the list item
 * @param trailingContent the trailing meta text, icon, switch or checkbox
 * @param tonalElevation the tonal elevation of this list item
 * @param shape Defines this list item's shape.
 * @param color Color to be used on background of this list item
 * @param contentColor The preferred content color provided by this list item to its children.
 * @param scale Defines size of the list item relative to its original size.
 * @param border Defines a border around the list item.
 * @param glow Shadow to be shown behind the list item.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this component. You can create and pass in your own [remember]ed instance
 * to observe [Interaction]s and customize the appearance / behavior of this list item in different
 * states.
 */
@ExperimentalTvMaterial3Api
@NonRestartableComposable
@Composable
fun ListItem(
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    headlineText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable BoxScope.() -> Unit)? = null,
    overlineText: (@Composable () -> Unit)? = null,
    subtitleText: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    tonalElevation: Dp = ListItemDefaults.ListItemElevation,
    shape: ListItemShape = ListItemDefaults.shape(),
    color: ListItemColor = ListItemDefaults.color(),
    contentColor: ListItemColor = ListItemDefaults.contentColor(),
    trailingContentColor: ListItemColor = ListItemDefaults.trailingContentColor(),
    scale: ListItemScale = ListItemDefaults.scale(),
    border: ListItemBorder = ListItemDefaults.border(),
    glow: ListItemGlow = ListItemDefaults.glow(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    BaseListItem(
        headlineText = headlineText,
        modifier = modifier,
        onSelectionChanged = onSelectionChanged,
        dense = false,
        contentPadding = ListItemDefaults.ContentPadding,
        isSelected = isSelected,
        enabled = enabled,
        icon = icon,
        overlineText = overlineText,
        subtitleText = subtitleText,
        trailingContent = trailingContent,
        tonalElevation = tonalElevation,
        shape = shape,
        color = color,
        contentColor = contentColor,
        trailingContentColor = trailingContentColor,
        scale = scale,
        border = border,
        glow = glow,
        interactionSource = interactionSource
    )
}

/**
 * A smaller/denser version of the Material Design list item
 *
 * Lists are continuous, vertical indexes of text or images.
 *
 * This component can be used to achieve the list item templates existing in the spec. One-line list
 * items have a singular line of headline text. Two-line list items additionally have either
 * supporting or overline text. Three-line list items have either both supporting and overline text,
 * or extended (two-line) supporting text.
 *
 * @param isSelected whether or not this ListItem is selected
 * @param onSelectionChanged callback to be called when this ListItem is clicked. Exposes updated
 * [isSelected] value.
 * @param headlineText the headline text of the list item
 * @param modifier [Modifier] to be applied to the list item
 * @param enabled controls the enabled state of this list item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param icon the leading supporting visual of the list item
 * @param overlineText the text composable displayed above the headline text
 * @param subtitleText the supporting text composable of the list item
 * @param trailingContent the trailing meta text, icon, switch or checkbox
 * @param tonalElevation the tonal elevation of this list item
 * @param shape Defines this list item's shape.
 * @param color Color to be used on background of this list item
 * @param contentColor The preferred content color provided by this list item to its children.
 * @param scale Defines size of the list item relative to its original size.
 * @param border Defines a border around the list item.
 * @param glow Shadow to be shown behind the list item.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this component. You can create and pass in your own [remember]ed instance
 * to observe [Interaction]s and customize the appearance / behavior of this list item in different
 * states.
 */
@ExperimentalTvMaterial3Api
@NonRestartableComposable
@Composable
fun DenseListItem(
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    headlineText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable BoxScope.() -> Unit)? = null,
    overlineText: (@Composable () -> Unit)? = null,
    subtitleText: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    tonalElevation: Dp = ListItemDefaults.ListItemElevation,
    shape: ListItemShape = ListItemDefaults.shape(),
    color: ListItemColor = ListItemDefaults.color(),
    contentColor: ListItemColor = ListItemDefaults.contentColor(),
    trailingContentColor: ListItemColor = ListItemDefaults.trailingContentColor(),
    scale: ListItemScale = ListItemDefaults.scale(),
    border: ListItemBorder = ListItemDefaults.border(),
    glow: ListItemGlow = ListItemDefaults.glow(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    BaseListItem(
        headlineText = headlineText,
        modifier = modifier,
        onSelectionChanged = onSelectionChanged,
        dense = true,
        contentPadding = ListItemDefaults.ContentPaddingDense,
        isSelected = isSelected,
        enabled = enabled,
        icon = icon,
        overlineText = overlineText,
        subtitleText = subtitleText,
        trailingContent = trailingContent,
        tonalElevation = tonalElevation,
        shape = shape,
        color = color,
        contentColor = contentColor,
        trailingContentColor = trailingContentColor,
        scale = scale,
        border = border,
        glow = glow,
        interactionSource = interactionSource
    )
}

/**
 * Base composable for [ListItem] and [DenseListItem].
 *
 * @param isSelected whether or not this ListItem is selected
 * @param onSelectionChanged callback to be called when this ListItem is clicked. Exposes updated
 * [isSelected] value.
 * @param dense whether the current list item is dense or not.
 * @param contentPadding declares the padding between the content and the edges of the component
 * @param headlineText the headline text of the list item
 * @param modifier [Modifier] to be applied to the list item
 * @param enabled controls the enabled state of this list item. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param icon the leading supporting visual of the list item
 * @param overlineText the text composable displayed above the headline text
 * @param subtitleText the supporting text composable of the list item
 * @param trailingContent the trailing meta text, icon, switch or checkbox
 * @param tonalElevation the tonal elevation of this list item
 * @param shape Defines this list item's shape.
 * @param color Color to be used on background of this list item
 * @param contentColor The preferred content color provided by this list item to its children.
 * @param scale Defines size of the list item relative to its original size.
 * @param border Defines a border around the list item.
 * @param glow Shadow to be shown behind the list item.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this component. You can create and pass in your own [remember]ed instance
 * to observe [Interaction]s and customize the appearance / behavior of this list item in different
 * states.
 */
@ExperimentalTvMaterial3Api
@Composable
private fun BaseListItem(
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    dense: Boolean,
    contentPadding: PaddingValues,
    headlineText: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: (@Composable BoxScope.() -> Unit)?,
    overlineText: (@Composable () -> Unit)?,
    subtitleText: (@Composable () -> Unit)?,
    trailingContent: (@Composable () -> Unit)?,
    tonalElevation: Dp,
    shape: ListItemShape,
    color: ListItemColor,
    contentColor: ListItemColor,
    trailingContentColor: ListItemColor,
    scale: ListItemScale,
    border: ListItemBorder,
    glow: ListItemGlow,
    interactionSource: MutableInteractionSource
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val statefulTrailingContentColor = ListItemDefaults.trailingContentColor(
        enabled = enabled,
        focused = isFocused,
        pressed = isPressed,
        selected = isSelected,
        listItemColor = trailingContentColor
    )
    val minIconSize = remember(dense) {
        if (dense) ListItemDefaults.IconSizeDense else ListItemDefaults.IconSize
    }

    Surface(
        checked = isSelected,
        onCheckedChange = onSelectionChanged,
        modifier = modifier.semantics { role = Role.Checkbox },
        enabled = enabled,
        tonalElevation = tonalElevation,
        shape = shape.toToggleableSurfaceShape(),
        color = color.toToggleableSurfaceColor(),
        contentColor = contentColor.toToggleableSurfaceColor(),
        scale = scale.toToggleableSurfaceScale(),
        border = border.toToggleableSurfaceBorder(),
        glow = glow.toToggleableSurfaceGlow(),
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(
                    minHeight = listItemMinHeight(
                        hasIcon = icon != null,
                        hasSubtitle = subtitleText != null,
                        hasOverlineText = overlineText != null,
                        dense = dense
                    )
                )
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { nnIcon ->
                Box(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = minIconSize,
                            minHeight = minIconSize
                        )
                        .graphicsLayer {
                            alpha = ListItemDefaults.IconOpacity
                        },
                    contentAlignment = Alignment.Center,
                    content = nnIcon
                )
                Spacer(
                    modifier = Modifier.padding(end = ListItemDefaults.LeadingComposableEndPadding)
                )
            }

            Box(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Column {
                    overlineText?.let { nnOverlineText ->
                        CompositionLocalProvider(
                            LocalContentColor provides LocalContentColor.current.copy(
                                alpha = ListItemDefaults.OverlineTextOpacity
                            )
                        ) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.labelSmall,
                                content = nnOverlineText
                            )
                        }
                    }
                    ProvideTextStyle(
                        value = if (dense) MaterialTheme.typography.titleSmall
                        else MaterialTheme.typography.titleMedium,
                        content = headlineText
                    )

                    subtitleText?.let { nnSubtitleText ->
                        CompositionLocalProvider(
                            LocalContentColor provides LocalContentColor.current.copy(
                                alpha = ListItemDefaults.SubtitleTextOpacity
                            )
                        ) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodySmall,
                                content = nnSubtitleText
                            )
                        }
                    }
                }
            }

            trailingContent?.let {
                Box(
                    modifier = Modifier
                        .padding(start = ListItemDefaults.TrailingComposableStartPadding)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides statefulTrailingContentColor
                    ) {
                        ProvideTextStyle(
                            value = if (dense) MaterialTheme.typography.labelSmall
                            else MaterialTheme.typography.labelLarge,
                            content = trailingContent
                        )
                    }
                }
            }
        }
    }
}

/**
 * Calculates the minimum container height for both [ListItem] and [DenseListItem] based on whether
 * the list items are One-Line, Two-Line or Three-Line list items.
 * @param hasIcon if the leading supporting visual of the list item exists.
 * @param hasSubtitle if the supporting text composable of the list item exists.
 * @param hasOverlineText if the text composable displayed above the headline text exists.
 * @param dense whether the current list item is dense or not.
 * @return The minimum container height for the given list item (to be used with
 * [Modifier.defaultMinSize]).
 */
@ExperimentalTvMaterial3Api
private fun listItemMinHeight(
    hasIcon: Boolean,
    hasSubtitle: Boolean,
    hasOverlineText: Boolean,
    dense: Boolean
): Dp {
    return when {
        hasSubtitle && hasOverlineText -> if (dense) ListItemDefaults.MinHeightThreeLineDense
        else ListItemDefaults.MinHeightThreeLine

        hasSubtitle || hasOverlineText -> if (dense) ListItemDefaults.MinHeightTwoLineDense
        else ListItemDefaults.MinHeightTwoLine

        hasIcon -> if (dense) ListItemDefaults.MinHeightIconDense
        else ListItemDefaults.MinHeightIcon

        else -> if (dense) ListItemDefaults.MinHeightDense else ListItemDefaults.MinHeight
    }
}

/**
 * Contains the default values used by list items.
 */
@ExperimentalTvMaterial3Api
object ListItemDefaults {
    private val ListItemShape = RoundedCornerShape(8.dp)
    private val DefaultBorder
        @ReadOnlyComposable
        @Composable get() = Border(
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.border
            ),
            shape = ListItemShape
        )
    internal const val IconOpacity = 0.8f
    internal const val OverlineTextOpacity = 0.6f
    internal const val SubtitleTextOpacity = 0.8f

    internal val LeadingComposableEndPadding = 8.dp
    internal val TrailingComposableStartPadding = 8.dp

    private val ListItemHorizontalPadding = 16.dp
    private val ListItemVerticalPadding = 12.dp
    private val ListItemDensePadding = 12.dp

    /**
     * The default content padding used by [ListItem]
     */
    val ContentPadding = PaddingValues(
        horizontal = ListItemHorizontalPadding,
        vertical = ListItemVerticalPadding
    )

    /**
     * The default content padding used by [DenseListItem]
     */
    val ContentPaddingDense = PaddingValues(all = ListItemDensePadding)

    /**
     * The Icon size used by [ListItem].
     */
    val IconSize = 32.dp

    /**
     * The Icon size used by [DenseListItem].
     */
    val IconSizeDense = 18.dp

    internal val MinHeightDense = 40.dp
    internal val MinHeight = 48.dp

    internal val MinHeightIconDense = 40.dp
    internal val MinHeightIcon = 56.dp

    internal val MinHeightTwoLineDense = 56.dp
    internal val MinHeightTwoLine = 64.dp

    internal val MinHeightThreeLineDense = 72.dp
    internal val MinHeightThreeLine = 80.dp

    val ListItemElevation = Elevation.Level0

    /**
     * Creates a [ListItemShape] that represents the default container shapes used in a ListItem
     *
     * @param shape the shape used when the ListItem is enabled, and has no other
     * [Interaction]s.
     * @param focusedShape the shape used when the ListItem is enabled and focused
     * @param pressedShape the shape used when the ListItem is enabled and pressed
     * @param selectedShape the shape used when the ListItem is enabled and selected
     * @param disabledShape the shape used when the ListItem is not enabled
     * @param focusedSelectedShape the shape used when the ListItem is enabled, focused and selected
     * @param focusedDisabledShape the shape used when the ListItem is not enabled and focused
     * @param pressedSelectedShape the shape used when the ListItem is enabled, pressed and selected
     * @param selectedDisabledShape the shape used when the ListItem is not enabled and selected
     * @param focusedSelectedDisabledShape the shape used when the ListItem is not enabled, focused
     * and selected.
     */
    fun shape(
        shape: Shape = ListItemShape,
        focusedShape: Shape = shape,
        pressedShape: Shape = shape,
        selectedShape: Shape = shape,
        disabledShape: Shape = shape,
        focusedSelectedShape: Shape = shape,
        focusedDisabledShape: Shape = disabledShape,
        pressedSelectedShape: Shape = shape,
        selectedDisabledShape: Shape = disabledShape,
        focusedSelectedDisabledShape: Shape = disabledShape
    ) = ListItemShape(
        shape = shape,
        focusedShape = focusedShape,
        pressedShape = pressedShape,
        selectedShape = selectedShape,
        disabledShape = disabledShape,
        focusedSelectedShape = focusedSelectedShape,
        focusedDisabledShape = focusedDisabledShape,
        pressedSelectedShape = pressedSelectedShape,
        selectedDisabledShape = selectedDisabledShape,
        focusedSelectedDisabledShape = focusedSelectedDisabledShape
    )

    /**
     * Creates a [ListItemColor] that represents the default container colors used in a ListItem
     *
     * @param color the color used when the ListItem is enabled, and has no other [Interaction]s
     * @param focusedColor the color used when the ListItem is enabled and focused
     * @param pressedColor the color used when the ListItem is enabled and pressed
     * @param selectedColor the color used when the ListItem is enabled and selected
     * @param disabledColor the color used when the ListItem is not enabled
     * @param focusedSelectedColor the color used when the ListItem is enabled, focused and selected
     * @param pressedSelectedColor the color used when the ListItem is enabled, pressed and selected
     */
    @ReadOnlyComposable
    @Composable
    fun color(
        color: Color = Color.Transparent,
        focusedColor: Color = MaterialTheme.colorScheme.onSurface,
        pressedColor: Color = MaterialTheme.colorScheme.onSurface,
        selectedColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
        disabledColor: Color = Color.Transparent,
        focusedSelectedColor: Color = focusedColor,
        pressedSelectedColor: Color = pressedColor
    ) = ListItemColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Creates a [ListItemColor] that represents the default content colors used in a ListItem
     *
     * @param color the color used when the ListItem is enabled, and has no other [Interaction]s
     * @param focusedColor the color used when the ListItem is enabled and focused
     * @param pressedColor the color used when the ListItem is enabled and pressed
     * @param selectedColor the color used when the ListItem is enabled and selected
     * @param disabledColor the color used when the ListItem is not enabled
     * @param focusedSelectedColor the color used when the ListItem is enabled, focused and selected
     * @param pressedSelectedColor the color used when the ListItem is enabled, pressed and selected
     */
    @ReadOnlyComposable
    @Composable
    fun contentColor(
        color: Color = MaterialTheme.colorScheme.onSurface,
        focusedColor: Color = MaterialTheme.colorScheme.surface,
        pressedColor: Color = MaterialTheme.colorScheme.surface,
        selectedColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledColor: Color = MaterialTheme.colorScheme.onSurface,
        focusedSelectedColor: Color = focusedColor,
        pressedSelectedColor: Color = pressedColor
    ) = ListItemColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Selects the most appropriate [Color] for the trailingContent of a ListItem depending on its
     * [Interaction] state.
     * @param enabled whether the list item is enabled or not.
     * @param focused whether the list item is focused or not.
     * @param selected whether the list item is selected or not.
     * @param listItemColor the instance of [ListItemColor] to fetch [Color] for the current state
     * from.
     */
    internal fun trailingContentColor(
        enabled: Boolean,
        focused: Boolean,
        pressed: Boolean,
        selected: Boolean,
        listItemColor: ListItemColor
    ): Color {
        return when {
            enabled && selected && pressed -> listItemColor.pressedSelectedColor
            enabled && selected && focused -> listItemColor.focusedSelectedColor
            enabled && selected -> listItemColor.selectedColor
            enabled && pressed -> listItemColor.pressedColor
            enabled && focused -> listItemColor.focusedColor
            enabled -> listItemColor.color
            else -> listItemColor.disabledColor
        }
    }

    /**
     * Creates a [ListItemColor] that represents the default trailing content colors used in a
     * ListItem.
     *
     * @param color the color used when the ListItem is enabled, and has no other [Interaction]s.
     * @param focusedColor the color used when the ListItem is enabled and focused.
     * @param pressedColor the color used when the ListItem is enabled and pressed.
     * @param selectedColor the color used when the ListItem is enabled and selected.
     * @param disabledColor the color used when the ListItem is not enabled.
     * @param focusedSelectedColor the color used when the ListItem is enabled, focused and
     * selected.
     * @param pressedSelectedColor the color used when the ListItem is enabled, pressed and
     * selected.
     */
    @ReadOnlyComposable
    @Composable
    fun trailingContentColor(
        color: Color = MaterialTheme.colorScheme.secondary,
        focusedColor: Color = MaterialTheme.colorScheme.inversePrimary,
        pressedColor: Color = MaterialTheme.colorScheme.inversePrimary,
        selectedColor: Color = MaterialTheme.colorScheme.secondary,
        disabledColor: Color = MaterialTheme.colorScheme.secondary,
        focusedSelectedColor: Color = selectedColor,
        pressedSelectedColor: Color = pressedColor
    ) = ListItemColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Creates a [ListItemScale] that represents the default scales used in a ListItem.
     * scales are used to modify the size of a composable in different [Interaction] states
     * e.g. 1f (original) in default state, 1.2f (scaled up) in focused state, 0.8f (scaled down)
     * in pressed state, etc.
     *
     * @param scale the scale used when the ListItem is enabled, and has no other
     * [Interaction]s.
     * @param focusedScale the scale used when the ListItem is enabled and focused.
     * @param pressedScale the scale used when the ListItem is enabled and pressed.
     * @param selectedScale the scale used when the ListItem is enabled and selected.
     * @param disabledScale the scale used when the ListItem is not enabled.
     * @param focusedSelectedScale the scale used when the ListItem is enabled, focused and
     * selected.
     * @param focusedDisabledScale the scale used when the ListItem is not enabled and
     * focused.
     * @param pressedSelectedScale the scale used when the ListItem is enabled, pressed and
     * selected.
     * @param selectedDisabledScale the scale used when the ListItem is not enabled and
     * selected.
     * @param focusedSelectedDisabledScale the scale used when the ListItem is not enabled,
     * focused and selected.
     */
    fun scale(
        @FloatRange(from = 0.0) scale: Float = 1f,
        @FloatRange(from = 0.0) focusedScale: Float = 1.05f,
        @FloatRange(from = 0.0) pressedScale: Float = scale,
        @FloatRange(from = 0.0) selectedScale: Float = scale,
        @FloatRange(from = 0.0) disabledScale: Float = scale,
        @FloatRange(from = 0.0) focusedSelectedScale: Float = focusedScale,
        @FloatRange(from = 0.0) focusedDisabledScale: Float = disabledScale,
        @FloatRange(from = 0.0) pressedSelectedScale: Float = scale,
        @FloatRange(from = 0.0) selectedDisabledScale: Float = disabledScale,
        @FloatRange(from = 0.0) focusedSelectedDisabledScale: Float = disabledScale
    ) = ListItemScale(
        scale = scale,
        focusedScale = focusedScale,
        pressedScale = pressedScale,
        selectedScale = selectedScale,
        disabledScale = disabledScale,
        focusedSelectedScale = focusedSelectedScale,
        focusedDisabledScale = focusedDisabledScale,
        pressedSelectedScale = pressedSelectedScale,
        selectedDisabledScale = selectedDisabledScale,
        focusedSelectedDisabledScale = focusedSelectedDisabledScale
    )

    /**
     * Creates a [ListItemBorder] that represents the default [Border]s applied on a ListItem in
     * different [Interaction] states
     *
     * @param border the [Border] used when the ListItem is enabled, and has no other
     * [Interaction]s
     * @param focusedBorder the [Border] used when the ListItem is enabled and focused
     * @param pressedBorder the [Border] used when the ListItem is enabled and pressed
     * @param selectedBorder the [Border] used when the ListItem is enabled and selected
     * @param disabledBorder the [Border] used when the ListItem is not enabled
     * @param focusedSelectedBorder the [Border] used when the ListItem is enabled, focused and
     * selected
     * @param focusedDisabledBorder the [Border] used when the ListItem is not enabled and focused
     * @param pressedSelectedBorder the [Border] used when the ListItem is enabled, pressed and
     * selected
     * @param selectedDisabledBorder the [Border] used when the ListItem is not enabled and
     * selected
     * @param focusedSelectedDisabledBorder the [Border] used when the ListItem is not enabled,
     * focused and selected
     */
    @ReadOnlyComposable
    @Composable
    fun border(
        border: Border = Border.None,
        focusedBorder: Border = border,
        pressedBorder: Border = focusedBorder,
        selectedBorder: Border = border,
        disabledBorder: Border = border,
        focusedSelectedBorder: Border = focusedBorder,
        focusedDisabledBorder: Border = DefaultBorder,
        pressedSelectedBorder: Border = border,
        selectedDisabledBorder: Border = disabledBorder,
        focusedSelectedDisabledBorder: Border = DefaultBorder
    ) = ListItemBorder(
        border = border,
        focusedBorder = focusedBorder,
        pressedBorder = pressedBorder,
        selectedBorder = selectedBorder,
        disabledBorder = disabledBorder,
        focusedSelectedBorder = focusedSelectedBorder,
        focusedDisabledBorder = focusedDisabledBorder,
        pressedSelectedBorder = pressedSelectedBorder,
        selectedDisabledBorder = selectedDisabledBorder,
        focusedSelectedDisabledBorder = focusedSelectedDisabledBorder
    )

    /**
     * Creates a [ListItemGlow] that represents the default [Glow]s used in a ListItem.
     *
     * @param glow the [Glow] used when the ListItem is enabled, and has no other [Interaction]s.
     * @param focusedGlow the [Glow] used when the ListItem is enabled and focused.
     * @param pressedGlow the [Glow] used when the ListItem is enabled and pressed.
     * @param selectedGlow the [Glow] used when the ListItem is enabled and selected.
     * @param focusedSelectedGlow the [Glow] used when the ListItem is enabled, focused and selected.
     * @param pressedSelectedGlow the [Glow] used when the ListItem is enabled, pressed and selected.
     */
    fun glow(
        glow: Glow = Glow.None,
        focusedGlow: Glow = glow,
        pressedGlow: Glow = glow,
        selectedGlow: Glow = glow,
        focusedSelectedGlow: Glow = focusedGlow,
        pressedSelectedGlow: Glow = glow
    ) = ListItemGlow(
        glow = glow,
        focusedGlow = focusedGlow,
        pressedGlow = pressedGlow,
        selectedGlow = selectedGlow,
        focusedSelectedGlow = focusedSelectedGlow,
        pressedSelectedGlow = pressedSelectedGlow
    )
}
