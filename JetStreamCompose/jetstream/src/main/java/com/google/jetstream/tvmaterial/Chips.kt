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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Glow
import androidx.tv.material3.LocalTextStyle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface

/**
 * Material Design assist chip.
 *
 * Chips help people enter information, make selections, filter content, or trigger actions. Chips
 * can show multiple interactive elements together in the same area, such as a list of selectable
 * movie times, or a series of email contacts.
 *
 * Assist chips represent smart or automated actions that can span multiple apps, such as opening a
 * calendar event from the home screen. Assist chips function as though the user asked an assistant
 * to complete the action. They should appear dynamically and contextually in a UI.
 *
 * @param onClick called when this chip is clicked
 * @param modifier the [Modifier] to be applied to this chip
 * @param enabled controls the enabled state of this chip. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param leadingIcon optional icon at the start of the chip, preceding the [label] text
 * @param trailingIcon optional icon at the end of the chip
 * @param shape Defines the Chip's shape.
 * @param color Color to be used on background of the chip
 * @param contentColor The preferred content color provided by this chip to its children.
 * @param scale Defines size of the chip relative to its original size.
 * @param border Defines a border around the chip.
 * @param glow Shadow to be shown behind the chip.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this chip. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this chip in different states.
 * @param label text label for this chip
 */
@ExperimentalTvMaterial3Api
@NonRestartableComposable
@Composable
fun AssistChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: ChipShape = AssistChipDefaults.shape(),
    color: ChipColor = AssistChipDefaults.color(),
    contentColor: ChipColor = AssistChipDefaults.contentColor(),
    scale: ChipScale = AssistChipDefaults.scale(),
    border: ChipBorder = AssistChipDefaults.border(),
    glow: ChipGlow = AssistChipDefaults.glow(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: @Composable () -> Unit
) {
    Chip(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        label = label,
        labelTextStyle = MaterialTheme.typography.labelLarge,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        shape = shape,
        color = color,
        contentColor = contentColor,
        scale = scale,
        border = border,
        glow = glow,
        minHeight = AssistChipDefaults.ContainerHeight,
        paddingValues = chipPadding(
            hasAvatar = false,
            hasLeadingIcon = leadingIcon != null,
            hasTrailingIcon = trailingIcon != null
        ),
        interactionSource = interactionSource
    )
}

/**
 * Material Design filter chip.
 *
 * Chips help people enter information, make selections, filter content, or trigger actions. Chips
 * can show multiple interactive elements together in the same area, such as a list of selectable
 * movie times, or a series of email contacts.
 *
 * Filter chips use tags or descriptive words to filter content. They can be a good alternative to
 * toggle buttons or checkboxes.
 *
 * Tapping on a filter chip toggles its selection state. A selection state [leadingIcon] can be
 * provided (e.g. a checkmark) to be appended at the starting edge of the chip's label.
 *
 * @param selected whether this chip is selected or not
 * @param onClick called when this chip is clicked
 * @param modifier the [Modifier] to be applied to this chip
 * @param enabled controls the enabled state of this chip. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param leadingIcon optional icon at the start of the chip, preceding the [label] text
 * @param trailingIcon optional icon at the end of the chip
 * @param shape Defines the Chip's shape.
 * @param color Color to be used on background of the chip
 * @param contentColor The preferred content color provided by this chip to its children.
 * @param scale Defines size of the chip relative to its original size.
 * @param border Defines a border around the chip.
 * @param glow Shadow to be shown behind the chip.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this chip. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this chip in different states.
 * @param label text label for this chip
 */
@ExperimentalTvMaterial3Api
@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: SelectableChipShape = FilterChipDefaults.shape(),
    color: SelectableChipColor = FilterChipDefaults.color(),
    contentColor: SelectableChipColor = FilterChipDefaults.contentColor(),
    scale: SelectableChipScale = FilterChipDefaults.scale(),
    border: SelectableChipBorder = FilterChipDefaults.border(),
    glow: SelectableChipGlow = FilterChipDefaults.glow(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: @Composable () -> Unit
) {
    SelectableChip(
        selected = selected,
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        label = label,
        labelTextStyle = MaterialTheme.typography.labelLarge,
        leadingIcon = leadingIcon,
        avatar = null,
        trailingIcon = trailingIcon,
        shape = shape,
        color = color,
        contentColor = contentColor,
        scale = scale,
        border = border,
        glow = glow,
        minHeight = FilterChipDefaults.ContainerHeight,
        paddingValues = chipPadding(
            hasAvatar = false,
            hasLeadingIcon = leadingIcon != null,
            hasTrailingIcon = trailingIcon != null
        ),
        interactionSource = interactionSource
    )
}

/**
 * Chips help people enter information, make selections, filter content, or trigger actions. Chips
 * can show multiple interactive elements together in the same area, such as a list of selectable
 * movie times, or a series of email contacts.
 *
 * Input chips represent discrete pieces of information entered by a user.
 *
 * An Input Chip can have a leading icon or an avatar at its start. In case both are provided, the
 * avatar will take precedence and will be displayed.
 *
 * @param selected whether this chip is selected or not
 * @param onClick called when this chip is clicked
 * @param modifier the [Modifier] to be applied to this chip
 * @param enabled controls the enabled state of this chip. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param leadingIcon optional icon at the start of the chip, preceding the [label] text
 * @param avatar optional avatar at the start of the chip, preceding the [label] text
 * @param trailingIcon optional icon at the end of the chip
 * @param shape Defines the Chip's shape.
 * @param color Color to be used on background of the chip
 * @param contentColor The preferred content color provided by this chip to its children.
 * @param scale Defines size of the chip relative to its original size.
 * @param border Defines a border around the chip.
 * @param glow Shadow to be shown behind the chip.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this chip. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this chip in different states.
 * @param label text label for this chip
 */
@ExperimentalTvMaterial3Api
@NonRestartableComposable
@Composable
fun InputChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    avatar: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: SelectableChipShape = InputChipDefaults.shape(hasAvatar = avatar != null),
    color: SelectableChipColor = InputChipDefaults.color(),
    contentColor: SelectableChipColor = InputChipDefaults.contentColor(),
    scale: SelectableChipScale = InputChipDefaults.scale(),
    border: SelectableChipBorder = InputChipDefaults.border(hasAvatar = avatar != null),
    glow: SelectableChipGlow = InputChipDefaults.glow(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: @Composable () -> Unit
) {
    SelectableChip(
        selected = selected,
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        label = label,
        labelTextStyle = MaterialTheme.typography.labelLarge,
        leadingIcon = leadingIcon,
        avatar = avatar,
        trailingIcon = trailingIcon,
        shape = shape,
        color = color,
        contentColor = contentColor,
        scale = scale,
        border = border,
        glow = glow,
        minHeight = InputChipDefaults.ContainerHeight,
        paddingValues = chipPadding(
            hasAvatar = avatar != null,
            hasLeadingIcon = leadingIcon != null,
            hasTrailingIcon = trailingIcon != null
        ),
        interactionSource = interactionSource
    )
}

/**
 * Material Design suggestion chip.
 *
 * Chips help people enter information, make selections, filter content, or trigger actions. Chips
 * can show multiple interactive elements together in the same area, such as a list of selectable
 * movie times, or a series of email contacts.
 *
 * Suggestion chips help narrow a user's intent by presenting dynamically generated suggestions,
 * such as possible responses or search filters.
 *
 * @param onClick called when this chip is clicked
 * @param modifier the [Modifier] to be applied to this chip
 * @param enabled controls the enabled state of this chip. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape Defines the Chip's shape.
 * @param color Color to be used on background of the chip
 * @param contentColor The preferred content color provided by this chip to its children.
 * @param scale Defines size of the chip relative to its original size.
 * @param border Defines a border around the chip.
 * @param glow Shadow to be shown behind the chip.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this chip. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this chip in different states.
 * @param label text label for this chip
 */
@ExperimentalTvMaterial3Api
@NonRestartableComposable
@Composable
fun SuggestionChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: ChipShape = SuggestionChipDefaults.shape(),
    color: ChipColor = SuggestionChipDefaults.color(),
    contentColor: ChipColor = SuggestionChipDefaults.contentColor(),
    scale: ChipScale = SuggestionChipDefaults.scale(),
    border: ChipBorder = SuggestionChipDefaults.border(),
    glow: ChipGlow = SuggestionChipDefaults.glow(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: @Composable () -> Unit
) {
    Chip(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        label = label,
        labelTextStyle = MaterialTheme.typography.labelLarge,
        leadingIcon = null,
        trailingIcon = null,
        shape = shape,
        color = color,
        contentColor = contentColor,
        scale = scale,
        border = border,
        glow = glow,
        minHeight = SuggestionChipDefaults.ContainerHeight,
        paddingValues = chipPadding(
            hasAvatar = false,
            hasLeadingIcon = false,
            hasTrailingIcon = false
        ),
        interactionSource = interactionSource
    )
}

@ExperimentalTvMaterial3Api
@NonRestartableComposable
@Composable
private fun Chip(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    label: @Composable () -> Unit,
    labelTextStyle: TextStyle,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    shape: ChipShape,
    color: ChipColor,
    contentColor: ChipColor,
    scale: ChipScale,
    border: ChipBorder,
    glow: ChipGlow,
    minHeight: Dp,
    paddingValues: PaddingValues,
    interactionSource: MutableInteractionSource
) {
    Surface(
        modifier = modifier.semantics { role = Role.Button },
        onClick = onClick,
        enabled = enabled,
        shape = shape.toClickableSurfaceShape(),
        color = color.toClickableSurfaceColor(),
        contentColor = contentColor.toClickableSurfaceColor(),
        scale = scale.toClickableSurfaceScale(),
        border = border.toClickableSurfaceBorder(),
        glow = glow.toClickableSurfaceGlow(),
        interactionSource = interactionSource
    ) {
        ChipContent(
            label = label,
            labelTextStyle = labelTextStyle,
            leadingIcon = leadingIcon,
            avatar = null,
            trailingIcon = trailingIcon,
            minHeight = minHeight,
            paddingValues = paddingValues
        )
    }
}

@ExperimentalTvMaterial3Api
@Composable
private fun SelectableChip(
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    label: @Composable () -> Unit,
    labelTextStyle: TextStyle,
    leadingIcon: @Composable (() -> Unit)?,
    avatar: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    shape: SelectableChipShape,
    color: SelectableChipColor,
    contentColor: SelectableChipColor,
    scale: SelectableChipScale,
    border: SelectableChipBorder,
    glow: SelectableChipGlow,
    minHeight: Dp,
    paddingValues: PaddingValues,
    interactionSource: MutableInteractionSource
) {
    Surface(
        checked = selected,
        onCheckedChange = { onClick() },
        modifier = modifier.semantics { role = Role.Checkbox },
        enabled = enabled,
        shape = shape.toToggleableSurfaceShape(),
        color = color.toToggleableSurfaceColor(),
        contentColor = contentColor.toToggleableSurfaceColor(),
        scale = scale.toToggleableSurfaceScale(),
        border = border.toToggleableSurfaceBorder(),
        glow = glow.toToggleableSurfaceGlow(),
        interactionSource = interactionSource
    ) {
        ChipContent(
            label = label,
            labelTextStyle = labelTextStyle,
            leadingIcon = leadingIcon,
            avatar = avatar,
            trailingIcon = trailingIcon,
            minHeight = minHeight,
            paddingValues = paddingValues
        )
    }
}

@Composable
private fun ChipContent(
    label: @Composable () -> Unit,
    labelTextStyle: TextStyle,
    leadingIcon: @Composable (() -> Unit)?,
    avatar: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    minHeight: Dp,
    paddingValues: PaddingValues
) {
    CompositionLocalProvider(
        LocalTextStyle provides labelTextStyle
    ) {
        Row(
            Modifier
                .defaultMinSize(minHeight = minHeight)
                .padding(paddingValues),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.animation.AnimatedVisibility(visible = avatar != null) {
                Row {
                    avatar?.invoke()
                    Spacer(Modifier.width(HorizontalElementsPadding))
                }
            }
            androidx.compose.animation.AnimatedVisibility(visible = leadingIcon != null) {
                Row {
                    leadingIcon?.invoke()
                    Spacer(Modifier.width(HorizontalElementsPadding))
                }
            }
            label()
            trailingIcon?.let { nnTrailingIcon ->
                Spacer(Modifier.width(HorizontalElementsPadding))
                nnTrailingIcon()
            }
        }
    }
}

/**
 * Returns the [PaddingValues] for any TV chip component.
 */
private fun chipPadding(
    hasAvatar: Boolean,
    hasLeadingIcon: Boolean,
    hasTrailingIcon: Boolean
): PaddingValues {
    val start = if (hasAvatar) 4.dp else if (hasLeadingIcon) 12.dp else 16.dp
    val end = if (hasTrailingIcon) 12.dp else 16.dp
    val vertical = if (hasAvatar) 4.dp else 8.dp
    return PaddingValues(
        start = start,
        end = end,
        top = vertical,
        bottom = vertical
    )
}

/**
 * Contains the default values used by [AssistChip].
 */
@ExperimentalTvMaterial3Api
object AssistChipDefaults {
    /**
     * The height applied to an assist chip.
     * Note that you can override it by applying Modifier.height directly on a chip.
     */
    val ContainerHeight = 36.dp

    /**
     * The size of an Assist chip icon.
     */
    val IconSize = 18.dp

    /**
     * The default [Shape] applied to an assist chip.
     */
    val ContainerShape = RoundedCornerShape(8.dp)

    private const val DisabledBackgroundColorOpacity = 0.2f
    private const val DisabledContentColorOpacity = 0.8f

    /**
     * Creates a [ChipShape] that represents the default container shapes used in an AssistChip.
     *
     * @param shape the shape used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedShape the shape used when the Chip is enabled and focused.
     * @param pressedShape the shape used when the Chip is enabled pressed.
     * @param disabledShape the shape used when the Chip is not enabled.
     * @param focusedDisabledShape the shape used when the Chip is not enabled and focused.
     */
    fun shape(
        shape: Shape = ContainerShape,
        focusedShape: Shape = shape,
        pressedShape: Shape = shape,
        disabledShape: Shape = shape,
        focusedDisabledShape: Shape = disabledShape
    ) = ChipShape(
        shape = shape,
        focusedShape = focusedShape,
        pressedShape = pressedShape,
        disabledShape = disabledShape,
        focusedDisabledShape = focusedDisabledShape
    )

    /**
     * Creates a [ChipColor] that represents the default container colors used in an AssistChip.
     *
     * @param color the container color of this Chip when enabled
     * @param focusedColor the container color of this Chip when enabled and focused
     * @param pressedColor the container color of this Chip when enabled and pressed
     * @param disabledColor the container color of this Chip when not enabled
     */
    @ReadOnlyComposable
    @Composable
    fun color(
        color: Color = Color.Transparent,
        focusedColor: Color = MaterialTheme.colorScheme.onSurface,
        pressedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = DisabledBackgroundColorOpacity
        )
    ) = ChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        disabledColor = disabledColor
    )

    /**
     * Creates a [ChipColor] that represents the default content colors used in an AssistChip.
     *
     * @param color the container color of this Chip when enabled
     * @param focusedColor the container color of this Chip when enabled and focused
     * @param pressedColor the container color of this Chip when enabled and pressed
     * @param disabledColor the container color of this Chip when not enabled
     */
    @ReadOnlyComposable
    @Composable
    fun contentColor(
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
        pressedColor: Color = MaterialTheme.colorScheme.surface,
        disabledColor: Color = MaterialTheme.colorScheme.border.copy(
            alpha = DisabledContentColorOpacity
        )
    ) = ChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        disabledColor = disabledColor
    )

    /**
     * Creates a [ChipScale] that represents the default scaleFactors used in an AssistChip.
     * scaleFactors are used to modify the size of a composable in different [Interaction]
     * states e.g. 1f (original) in default state, 1.2f (scaled up) in focused state,
     * 0.8f (scaled down) in pressed state, etc.
     *
     * @param scale the scaleFactor to be used for this Chip when enabled
     * @param focusedScale the scaleFactor to be used for this Chip when focused
     * @param pressedScale the scaleFactor to be used for this Chip when pressed
     * @param disabledScale the scaleFactor to be used for this Chip when disabled
     * @param focusedDisabledScale the scaleFactor to be used for this Chip when disabled and
     * focused
     */
    fun scale(
        @FloatRange(from = 0.0) scale: Float = 1f,
        @FloatRange(from = 0.0) focusedScale: Float = 1.1f,
        @FloatRange(from = 0.0) pressedScale: Float = scale,
        @FloatRange(from = 0.0) disabledScale: Float = scale,
        @FloatRange(from = 0.0) focusedDisabledScale: Float = disabledScale
    ) = ChipScale(
        scale = scale,
        focusedScale = focusedScale,
        pressedScale = pressedScale,
        disabledScale = disabledScale,
        focusedDisabledScale = focusedDisabledScale
    )

    /**
     * Creates a [ChipBorder] that represents the default [Border]s applied on an
     * AssistChip in different [Interaction] states.
     *
     * @param border the [Border] to be used for this Chip when enabled
     * @param focusedBorder the [Border] to be used for this Chip when focused
     * @param pressedBorder the [Border] to be used for this Chip when pressed
     * @param disabledBorder the [Border] to be used for this Chip when disabled
     * @param focusedDisabledBorder the [Border] to be used for this Chip when disabled and
     * focused
     */
    @ReadOnlyComposable
    @Composable
    fun border(
        border: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.border
            ),
            shape = ContainerShape
        ),
        focusedBorder: Border = Border.None,
        pressedBorder: Border = focusedBorder,
        disabledBorder: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = ContainerShape
        ),
        focusedDisabledBorder: Border = border
    ) = ChipBorder(
        border = border,
        focusedBorder = focusedBorder,
        pressedBorder = pressedBorder,
        disabledBorder = disabledBorder,
        focusedDisabledBorder = focusedDisabledBorder
    )

    /**
     * Creates a [ChipGlow] that represents the default [Glow]s used in an AssistChip.
     *
     * @param glow the Glow behind this Button when enabled
     * @param focusedGlow the Glow behind this Button when focused
     * @param pressedGlow the Glow behind this Button when pressed
     */
    fun glow(
        glow: Glow = Glow.None,
        focusedGlow: Glow = glow,
        pressedGlow: Glow = glow
    ) = ChipGlow(
        glow = glow,
        focusedGlow = focusedGlow,
        pressedGlow = pressedGlow
    )
}

/**
 * Contains the default values used by [FilterChip].
 */
@ExperimentalTvMaterial3Api
object FilterChipDefaults {
    /**
     * The height applied to a filter chip.
     * Note that you can override it by applying Modifier.height directly on a chip.
     */
    val ContainerHeight = 36.dp

    /**
     * The size of a Filter chip icon.
     */
    val IconSize = 18.dp

    /**
     * The default [Shape] applied to a filter chip.
     */
    val ContainerShape = RoundedCornerShape(8.dp)

    private const val SelectedBackgroundColorOpacity = 0.4f
    private const val DisabledBackgroundColorOpacity = 0.2f
    private const val DisabledContentColorOpacity = 0.8f

    /**
     * Creates a [SelectableChipShape] that represents the default container shapes used in a
     * FilterChip.
     *
     * @param shape the shape used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedShape the shape used when the Chip is enabled and focused.
     * @param pressedShape the shape used when the Chip is enabled and pressed.
     * @param selectedShape the shape used when the Chip is enabled and selected.
     * @param disabledShape the shape used when the Chip is not enabled.
     * @param focusedSelectedShape the shape used when the Chip is enabled, focused and selected.
     * @param focusedDisabledShape the shape used when the Chip is not enabled and focused.
     * @param pressedSelectedShape the shape used when the Chip is enabled, pressed and selected.
     * @param selectedDisabledShape the shape used when the Chip is not enabled and selected.
     * @param focusedSelectedDisabledShape the shape used when the Chip is not enabled, focused
     * and selected.
     */
    fun shape(
        shape: Shape = ContainerShape,
        focusedShape: Shape = shape,
        pressedShape: Shape = shape,
        selectedShape: Shape = shape,
        disabledShape: Shape = shape,
        focusedSelectedShape: Shape = shape,
        focusedDisabledShape: Shape = disabledShape,
        pressedSelectedShape: Shape = shape,
        selectedDisabledShape: Shape = disabledShape,
        focusedSelectedDisabledShape: Shape = disabledShape
    ) = SelectableChipShape(
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
     * Creates a [SelectableChipColor] that represents the default container colors used in a
     * FilterChip.
     *
     * @param color the color used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedColor the color used when the Chip is enabled and focused.
     * @param pressedColor the color used when the Chip is enabled and pressed.
     * @param selectedColor the color used when the Chip is enabled and selected.
     * @param disabledColor the color used when the Chip is not enabled.
     * @param focusedSelectedColor the color used when the Chip is enabled, focused and selected.
     * @param pressedSelectedColor the color used when the Chip is enabled, pressed and selected.
     */
    @ReadOnlyComposable
    @Composable
    fun color(
        color: Color = Color.Transparent,
        focusedColor: Color = MaterialTheme.colorScheme.onSurface,
        pressedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(
            alpha = SelectedBackgroundColorOpacity
        ),
        disabledColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = DisabledBackgroundColorOpacity
        ),
        focusedSelectedColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        pressedSelectedColor: Color = MaterialTheme.colorScheme.secondary
    ) = SelectableChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Creates a [SelectableChipColor] that represents the default content colors used in a
     * FilterChip.
     *
     * @param color the color used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedColor the color used when the Chip is enabled and focused.
     * @param pressedColor the color used when the Chip is enabled and pressed.
     * @param selectedColor the color used when the Chip is enabled and selected.
     * @param disabledColor the color used when the Chip is not enabled.
     * @param focusedSelectedColor the color used when the Chip is enabled, focused and selected.
     * @param pressedSelectedColor the color used when the Chip is enabled, pressed and selected.
     */
    @ReadOnlyComposable
    @Composable
    fun contentColor(
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
        pressedColor: Color = MaterialTheme.colorScheme.surface,
        selectedColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledColor: Color = MaterialTheme.colorScheme.border.copy(
            alpha = DisabledContentColorOpacity
        ),
        focusedSelectedColor: Color = MaterialTheme.colorScheme.onPrimary,
        pressedSelectedColor: Color = MaterialTheme.colorScheme.onSecondary
    ) = SelectableChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Creates a [SelectableChipScale] that represents the default scaleFactors used in a
     * FilterChip. scaleFactors are used to modify the size of a composable in different
     * [Interaction] states e.g. 1f (original) in default state, 1.2f (scaled up) in focused state,
     * 0.8f (scaled down) in pressed state, etc.
     *
     * @param scale the scaleFactor used when the Chip is enabled, and has no other
     * [Interaction]s.
     * @param focusedScale the scaleFactor used when the Chip is enabled and focused.
     * @param pressedScale the scaleFactor used when the Chip is enabled and pressed.
     * @param selectedScale the scaleFactor used when the Chip is enabled and selected.
     * @param disabledScale the scaleFactor used when the Chip is not enabled.
     * @param focusedSelectedScale the scaleFactor used when the Chip is enabled, focused and
     * selected.
     * @param focusedDisabledScale the scaleFactor used when the Chip is not enabled and
     * focused.
     * @param pressedSelectedScale the scaleFactor used when the Chip is enabled, pressed and
     * selected.
     * @param selectedDisabledScale the scaleFactor used when the Chip is not enabled and
     * selected.
     * @param focusedSelectedDisabledScale the scaleFactor used when the Chip is not enabled,
     * focused and selected.
     */
    fun scale(
        @FloatRange(from = 0.0) scale: Float = 1f,
        @FloatRange(from = 0.0) focusedScale: Float = 1.1f,
        @FloatRange(from = 0.0) pressedScale: Float = scale,
        @FloatRange(from = 0.0) selectedScale: Float = scale,
        @FloatRange(from = 0.0) disabledScale: Float = scale,
        @FloatRange(from = 0.0) focusedSelectedScale: Float = focusedScale,
        @FloatRange(from = 0.0) focusedDisabledScale: Float = disabledScale,
        @FloatRange(from = 0.0) pressedSelectedScale: Float = scale,
        @FloatRange(from = 0.0) selectedDisabledScale: Float = disabledScale,
        @FloatRange(from = 0.0) focusedSelectedDisabledScale: Float = disabledScale
    ) = SelectableChipScale(
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
     * Creates a [SelectableChipBorder] that represents the default [Border]s applied on a
     * FilterChip in different [Interaction] states.
     *
     * @param border the [Border] used when the Chip is enabled, and has no other
     * [Interaction]s.
     * @param focusedBorder the [Border] used when the Chip is enabled and focused.
     * @param pressedBorder the [Border] used when the Chip is enabled and pressed.
     * @param selectedBorder the [Border] used when the Chip is enabled and selected.
     * @param disabledBorder the [Border] used when the Chip is not enabled.
     * @param focusedSelectedBorder the [Border] used when the Chip is enabled, focused and
     * selected.
     * @param focusedDisabledBorder the [Border] used when the Chip is not enabled and focused.
     * @param pressedSelectedBorder the [Border] used when the Chip is enabled, pressed and
     * selected.
     * @param selectedDisabledBorder the [Border] used when the Chip is not enabled and
     * selected.
     * @param focusedSelectedDisabledBorder the [Border] used when the Chip is not enabled,
     * focused and selected.
     */
    @ReadOnlyComposable
    @Composable
    fun border(
        border: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.border
            ),
            shape = ContainerShape
        ),
        focusedBorder: Border = border.copy(
            border = border.border.copy(width = 1.5.dp)
        ),
        pressedBorder: Border = focusedBorder,
        selectedBorder: Border = focusedBorder,
        disabledBorder: Border = focusedBorder,
        focusedSelectedBorder: Border = focusedBorder,
        focusedDisabledBorder: Border = focusedBorder,
        pressedSelectedBorder: Border = focusedBorder,
        selectedDisabledBorder: Border = focusedBorder,
        focusedSelectedDisabledBorder: Border = focusedBorder
    ) = SelectableChipBorder(
        border = border,
        focusedBorder = focusedBorder,
        pressedBorder = pressedBorder,
        selectedBorder = selectedBorder,
        disabledBorder = disabledBorder,
        focusedSelected = focusedSelectedBorder,
        focusedDisabled = focusedDisabledBorder,
        pressedSelected = pressedSelectedBorder,
        selectedDisabled = selectedDisabledBorder,
        focusedSelectedDisabled = focusedSelectedDisabledBorder
    )

    /**
     * Creates a [SelectableChipGlow] that represents the default [Glow]s used in a FilterChip.
     *
     * @param glow the [Glow] used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedGlow the [Glow] used when the Chip is enabled and focused.
     * @param pressedGlow the [Glow] used when the Chip is enabled and pressed.
     * @param selectedGlow the [Glow] used when the Chip is enabled and selected.
     * @param focusedSelectedGlow the [Glow] used when the Chip is enabled, focused and selected.
     * @param pressedSelectedGlow the [Glow] used when the Chip is enabled, pressed and selected.
     */
    fun glow(
        glow: Glow = Glow.None,
        focusedGlow: Glow = glow,
        pressedGlow: Glow = glow,
        selectedGlow: Glow = glow,
        focusedSelectedGlow: Glow = focusedGlow,
        pressedSelectedGlow: Glow = glow
    ) = SelectableChipGlow(
        glow = glow,
        focusedGlow = focusedGlow,
        pressedGlow = pressedGlow,
        selectedGlow = selectedGlow,
        focusedSelectedGlow = focusedSelectedGlow,
        pressedSelectedGlow = pressedSelectedGlow
    )
}

/**
 * Contains the default values used by [InputChip].
 */
@ExperimentalTvMaterial3Api
object InputChipDefaults {
    /**
     * The height applied for an input chip.
     * Note that you can override it by applying Modifier.height directly on a chip.
     */
    val ContainerHeight = 36.dp

    /**
     * The size of an Input chip icon.
     */
    val IconSize = 18.dp

    /**
     * The size of an Input chip avatar.
     */
    val AvatarSize = 28.dp

    /**
     * The default [Shape] applied to an input chip.
     */
    val ContainerShape = RoundedCornerShape(8.dp)

    /**
     * The default [Shape] applied to an input chip with avatar.
     */
    val ContainerShapeWithAvatar = RoundedCornerShape(36.dp)

    private const val SelectedBackgroundColorOpacity = 0.4f
    private const val DisabledBackgroundColorOpacity = 0.2f
    private const val DisabledContentColorOpacity = 0.8f

    /**
     * Creates a [SelectableChipShape] that represents the default container shapes used in an
     * InputChip.
     *
     * @param hasAvatar changes the default shape based on whether the avatar composable is not
     * null in the Chip.
     * @param shape the shape used when the Chip is enabled, and has no other
     * [Interaction]s.
     * @param focusedShape the shape used when the Chip is enabled and focused.
     * @param pressedShape the shape used when the Chip is enabled and pressed.
     * @param selectedShape the shape used when the Chip is enabled and selected.
     * @param disabledShape the shape used when the Chip is not enabled.
     * @param focusedSelectedShape the shape used when the Chip is enabled, focused and selected.
     * @param focusedDisabledShape the shape used when the Chip is not enabled and focused.
     * @param pressedSelectedShape the shape used when the Chip is enabled, pressed and selected.
     * @param selectedDisabledShape the shape used when the Chip is not enabled and selected.
     * @param focusedSelectedDisabledShape the shape used when the Chip is not enabled, focused
     * and selected.
     */
    fun shape(
        hasAvatar: Boolean,
        shape: Shape = if (hasAvatar) ContainerShapeWithAvatar else ContainerShape,
        focusedShape: Shape = shape,
        pressedShape: Shape = shape,
        selectedShape: Shape = shape,
        disabledShape: Shape = shape,
        focusedSelectedShape: Shape = shape,
        focusedDisabledShape: Shape = disabledShape,
        pressedSelectedShape: Shape = shape,
        selectedDisabledShape: Shape = disabledShape,
        focusedSelectedDisabledShape: Shape = disabledShape
    ) = SelectableChipShape(
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
     * Creates a [SelectableChipColor] that represents the default container colors used in an
     * InputChip.
     *
     * @param color the color used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedColor the color used when the Chip is enabled and focused.
     * @param pressedColor the color used when the Chip is enabled and pressed.
     * @param selectedColor the color used when the Chip is enabled and selected.
     * @param disabledColor the color used when the Chip is not enabled.
     * @param focusedSelectedColor the color used when the Chip is enabled, focused and selected.
     * @param pressedSelectedColor the color used when the Chip is enabled, pressed and selected.
     */
    @ReadOnlyComposable
    @Composable
    fun color(
        color: Color = Color.Transparent,
        focusedColor: Color = MaterialTheme.colorScheme.onSurface,
        pressedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        selectedColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(
            alpha = SelectedBackgroundColorOpacity
        ),
        disabledColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = DisabledBackgroundColorOpacity
        ),
        focusedSelectedColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        pressedSelectedColor: Color = MaterialTheme.colorScheme.secondary
    ) = SelectableChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Creates a [SelectableChipColor] that represents the default content colors used in an
     * InputChip.
     *
     * @param color the color used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedColor the color used when the Chip is enabled and focused.
     * @param pressedColor the color used when the Chip is enabled and pressed.
     * @param selectedColor the color used when the Chip is enabled and selected.
     * @param disabledColor the color used when the Chip is not enabled.
     * @param focusedSelectedColor the color used when the Chip is enabled, focused and selected.
     * @param pressedSelectedColor the color used when the Chip is enabled, pressed and selected.
     */
    @ReadOnlyComposable
    @Composable
    fun contentColor(
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
        pressedColor: Color = MaterialTheme.colorScheme.surface,
        selectedColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        disabledColor: Color = MaterialTheme.colorScheme.border.copy(
            alpha = DisabledContentColorOpacity
        ),
        focusedSelectedColor: Color = MaterialTheme.colorScheme.onPrimary,
        pressedSelectedColor: Color = MaterialTheme.colorScheme.onSecondary
    ) = SelectableChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        selectedColor = selectedColor,
        disabledColor = disabledColor,
        focusedSelectedColor = focusedSelectedColor,
        pressedSelectedColor = pressedSelectedColor
    )

    /**
     * Creates a [SelectableChipScale] that represents the default scaleFactors used in an
     * InputChip. scaleFactors are used to modify the size of a composable in different
     * [Interaction] states e.g. 1f (original) in default state, 1.2f (scaled up) in focused state,
     * 0.8f (scaled down) in pressed state, etc.
     *
     * @param scale the scaleFactor used when the Chip is enabled, and has no other
     * [Interaction]s.
     * @param focusedScale the scaleFactor used when the Chip is enabled and focused.
     * @param pressedScale the scaleFactor used when the Chip is enabled and pressed.
     * @param selectedScale the scaleFactor used when the Chip is enabled and selected.
     * @param disabledScale the scaleFactor used when the Chip is not enabled.
     * @param focusedSelectedScale the scaleFactor used when the Chip is enabled, focused and
     * selected.
     * @param focusedDisabledScale the scaleFactor used when the Chip is not enabled and
     * focused.
     * @param pressedSelectedScale the scaleFactor used when the Chip is enabled, pressed and
     * selected.
     * @param selectedDisabledScale the scaleFactor used when the Chip is not enabled and
     * selected.
     * @param focusedSelectedDisabledScale the scaleFactor used when the Chip is not enabled,
     * focused and selected.
     */
    fun scale(
        @FloatRange(from = 0.0) scale: Float = 1f,
        @FloatRange(from = 0.0) focusedScale: Float = 1.1f,
        @FloatRange(from = 0.0) pressedScale: Float = scale,
        @FloatRange(from = 0.0) selectedScale: Float = scale,
        @FloatRange(from = 0.0) disabledScale: Float = scale,
        @FloatRange(from = 0.0) focusedSelectedScale: Float = focusedScale,
        @FloatRange(from = 0.0) focusedDisabledScale: Float = disabledScale,
        @FloatRange(from = 0.0) pressedSelectedScale: Float = scale,
        @FloatRange(from = 0.0) selectedDisabledScale: Float = disabledScale,
        @FloatRange(from = 0.0) focusedSelectedDisabledScale: Float = disabledScale
    ) = SelectableChipScale(
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
     * Creates a [SelectableChipBorder] that represents the default [Border]s applied on an
     * InputChip in different [Interaction] states.
     * @param hasAvatar changes the default border shape based on whether the avatar composable is
     * not null in the Chip.
     * @param border the [Border] used when the Chip is enabled, and has no other
     * [Interaction]s.
     * @param focusedBorder the [Border] used when the Chip is enabled and focused.
     * @param pressedBorder the [Border] used when the Chip is enabled and pressed.
     * @param selectedBorder the [Border] used when the Chip is enabled and selected.
     * @param disabledBorder the [Border] used when the Chip is not enabled.
     * @param focusedSelectedBorder the [Border] used when the Chip is enabled, focused and
     * selected.
     * @param focusedDisabledBorder the [Border] used when the Chip is not enabled and focused.
     * @param pressedSelectedBorder the [Border] used when the Chip is enabled, pressed and
     * selected.
     * @param selectedDisabledBorder the [Border] used when the Chip is not enabled and
     * selected.
     * @param focusedSelectedDisabledBorder the [Border] used when the Chip is not enabled,
     * focused and selected.
     */
    @ReadOnlyComposable
    @Composable
    fun border(
        hasAvatar: Boolean,
        border: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.border
            ),
            shape = if (hasAvatar) ContainerShapeWithAvatar else ContainerShape
        ),
        focusedBorder: Border = Border.None,
        pressedBorder: Border = focusedBorder,
        selectedBorder: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            ),
            shape = if (hasAvatar) ContainerShapeWithAvatar else ContainerShape
        ),
        disabledBorder: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = if (hasAvatar) ContainerShapeWithAvatar else ContainerShape
        ),
        focusedSelectedBorder: Border = Border(
            border = BorderStroke(
                width = 1.1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            shape = if (hasAvatar) ContainerShapeWithAvatar else ContainerShape
        ),
        focusedDisabledBorder: Border = border,
        pressedSelectedBorder: Border = Border.None,
        selectedDisabledBorder: Border = Border.None,
        focusedSelectedDisabledBorder: Border = border
    ) = SelectableChipBorder(
        border = border,
        focusedBorder = focusedBorder,
        pressedBorder = pressedBorder,
        selectedBorder = selectedBorder,
        disabledBorder = disabledBorder,
        focusedSelected = focusedSelectedBorder,
        focusedDisabled = focusedDisabledBorder,
        pressedSelected = pressedSelectedBorder,
        selectedDisabled = selectedDisabledBorder,
        focusedSelectedDisabled = focusedSelectedDisabledBorder
    )

    /**
     * Creates a [SelectableChipGlow] that represents the default [Glow]s used in an InputChip.
     *
     * @param glow the [Glow] used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedGlow the [Glow] used when the Chip is enabled and focused.
     * @param pressedGlow the [Glow] used when the Chip is enabled and pressed.
     * @param selectedGlow the [Glow] used when the Chip is enabled and selected.
     * @param focusedSelectedGlow the [Glow] used when the Chip is enabled, focused and selected.
     * @param pressedSelectedGlow the [Glow] used when the Chip is enabled, pressed and selected.
     */
    fun glow(
        glow: Glow = Glow.None,
        focusedGlow: Glow = glow,
        pressedGlow: Glow = glow,
        selectedGlow: Glow = glow,
        focusedSelectedGlow: Glow = focusedGlow,
        pressedSelectedGlow: Glow = glow
    ) = SelectableChipGlow(
        glow = glow,
        focusedGlow = focusedGlow,
        pressedGlow = pressedGlow,
        selectedGlow = selectedGlow,
        focusedSelectedGlow = focusedSelectedGlow,
        pressedSelectedGlow = pressedSelectedGlow
    )
}

/**
 * Contains the default values used by [SuggestionChip].
 */
@ExperimentalTvMaterial3Api
object SuggestionChipDefaults {
    /**
     * The height applied to a suggestion chip.
     * Note that you can override it by applying Modifier.height directly on a chip.
     */
    val ContainerHeight = 36.dp

    /**
     * The default [Shape] applied to a suggestion chip.
     */
    val ContainerShape = RoundedCornerShape(8.dp)

    private const val DisabledBackgroundColorOpacity = 0.2f
    private const val DisabledContentColorOpacity = 0.8f

    /**
     * Creates a [ChipShape] that represents the default container shapes used in a SuggestionChip.
     *
     * @param shape the shape used when the Chip is enabled, and has no other [Interaction]s.
     * @param focusedShape the shape used when the Chip is enabled and focused.
     * @param pressedShape the shape used when the Chip is enabled pressed.
     * @param disabledShape the shape used when the Chip is not enabled.
     * @param focusedDisabledShape the shape used when the Chip is not enabled and focused.
     */
    fun shape(
        shape: Shape = ContainerShape,
        focusedShape: Shape = shape,
        pressedShape: Shape = shape,
        disabledShape: Shape = shape,
        focusedDisabledShape: Shape = disabledShape
    ) = ChipShape(
        shape = shape,
        focusedShape = focusedShape,
        pressedShape = pressedShape,
        disabledShape = disabledShape,
        focusedDisabledShape = focusedDisabledShape
    )

    /**
     * Creates a [ChipColor] that represents the default container colors used in a SuggestionChip.
     *
     * @param color the container color of this Chip when enabled
     * @param focusedColor the container color of this Chip when enabled and focused
     * @param pressedColor the container color of this Chip when enabled and pressed
     * @param disabledColor the container color of this Chip when not enabled
     */
    @ReadOnlyComposable
    @Composable
    fun color(
        color: Color = Color.Transparent,
        focusedColor: Color = MaterialTheme.colorScheme.onSurface,
        pressedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = DisabledBackgroundColorOpacity
        )
    ) = ChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        disabledColor = disabledColor
    )

    /**
     * Creates a [ChipColor] that represents the default content colors used in a SuggestionChip.
     *
     * @param color the container color of this Chip when enabled
     * @param focusedColor the container color of this Chip when enabled and focused
     * @param pressedColor the container color of this Chip when enabled and pressed
     * @param disabledColor the container color of this Chip when not enabled
     */
    @ReadOnlyComposable
    @Composable
    fun contentColor(
        color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
        pressedColor: Color = MaterialTheme.colorScheme.surface,
        disabledColor: Color = MaterialTheme.colorScheme.border.copy(
            alpha = DisabledContentColorOpacity
        )
    ) = ChipColor(
        color = color,
        focusedColor = focusedColor,
        pressedColor = pressedColor,
        disabledColor = disabledColor
    )

    /**
     * Creates a [ChipScale] that represents the default scaleFactors used in a SuggestionChip.
     * scaleFactors are used to modify the size of a composable in different [Interaction]
     * states e.g. 1f (original) in default state, 1.2f (scaled up) in focused state,
     * 0.8f (scaled down) in pressed state, etc.
     *
     * @param scale the scaleFactor to be used for this Chip when enabled
     * @param focusedScale the scaleFactor to be used for this Chip when focused
     * @param pressedScale the scaleFactor to be used for this Chip when pressed
     * @param disabledScale the scaleFactor to be used for this Chip when disabled
     * @param focusedDisabledScale the scaleFactor to be used for this Chip when disabled and
     * focused
     */
    fun scale(
        @FloatRange(from = 0.0) scale: Float = 1f,
        @FloatRange(from = 0.0) focusedScale: Float = 1.1f,
        @FloatRange(from = 0.0) pressedScale: Float = scale,
        @FloatRange(from = 0.0) disabledScale: Float = scale,
        @FloatRange(from = 0.0) focusedDisabledScale: Float = disabledScale
    ) = ChipScale(
        scale = scale,
        focusedScale = focusedScale,
        pressedScale = pressedScale,
        disabledScale = disabledScale,
        focusedDisabledScale = focusedDisabledScale
    )

    /**
     * Creates a [ChipBorder] that represents the default [Border]s applied on a
     * SuggestionChip in different [Interaction] states.
     *
     * @param border the [Border] to be used for this Chip when enabled
     * @param focusedBorder the [Border] to be used for this Chip when focused
     * @param pressedBorder the [Border] to be used for this Chip when pressed
     * @param disabledBorder the [Border] to be used for this Chip when disabled
     * @param focusedDisabledBorder the [Border] to be used for this Chip when disabled and
     * focused
     */
    @ReadOnlyComposable
    @Composable
    fun border(
        border: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.border
            ),
            shape = ContainerShape
        ),
        focusedBorder: Border = Border.None,
        pressedBorder: Border = focusedBorder,
        disabledBorder: Border = Border(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = ContainerShape
        ),
        focusedDisabledBorder: Border = border
    ) = ChipBorder(
        border = border,
        focusedBorder = focusedBorder,
        pressedBorder = pressedBorder,
        disabledBorder = disabledBorder,
        focusedDisabledBorder = focusedDisabledBorder
    )

    /**
     * Creates a [ChipGlow] that represents the default [Glow]s used in a SuggestionChip.
     *
     * @param glow the Glow behind this Button when enabled
     * @param focusedGlow the Glow behind this Button when focused
     * @param pressedGlow the Glow behind this Button when pressed
     */
    fun glow(
        glow: Glow = Glow.None,
        focusedGlow: Glow = glow,
        pressedGlow: Glow = glow
    ) = ChipGlow(
        glow = glow,
        focusedGlow = focusedGlow,
        pressedGlow = pressedGlow
    )
}

/**
 * The padding between the elements in the chip.
 */
internal val HorizontalElementsPadding = 8.dp
