package com.louis993546.metro

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String
) {
    val state = remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .forceTapAnimation()
            .pointerInteropFilter {
                when(it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        state.value = true
                    }
                    MotionEvent.ACTION_UP -> {
                        state.value = false
                    }
                    else -> {
                        state.value = false
                    }
                }
                false
            }
            .background(color = if (state.value) LocalTextOnBackgroundColor.current else Color.Transparent)
            .border(
                width = 3.dp,
                color = if (state.value) LocalTextOnBackgroundColor.current else LocalTextOnBackgroundColor.current
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .padding(horizontal = 12.dp),

            text = text,
            color = if (state.value) LocalBackgroundColor.current else LocalTextOnBackgroundColor.current,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircleButton(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .forceTapAnimation()
            .pointerInteropFilter {
                when(it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        state.value = true
                    }
                    MotionEvent.ACTION_UP -> {
                        state.value = false
                    }
                    else -> {
                        state.value = false
                    }
                }
                false
            }
            .clip(CircleShape)
            .background(color = if (state.value) LocalTextOnBackgroundColor.current else Color.Transparent)
            .border(
                width = 3.dp,
                color = if (state.value) LocalTextOnBackgroundColor.current else LocalTextOnBackgroundColor.current,
                shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * Windows Phone 8.1-like tap animation
 */
private enum class ButtonState { Pressed, Idle }
fun Modifier.forceTapAnimation() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    var touchedPoint by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.95f else 1f)
    val tilt by animateFloatAsState(if (buttonState == ButtonState.Pressed) 5f else 0f)

    this
        .onGloballyPositioned {
            size = it.size
        }
        .graphicsLayer {
            scaleX = scale
            scaleY = scale

            // TODO Allow for just tilting in one direction
            // TODO Allow for no-tilt and just being pressed down (eg centered tap)
            if(touchedPoint.x != 0f)
                rotationY = if(touchedPoint.x < (size.width / 2)) -tilt else tilt
            if(touchedPoint.y != 0f)
                rotationX = if(touchedPoint.y > (size.height / 2)) -tilt else tilt
        }
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)

                    var event = this.currentEvent.changes[0]
                    touchedPoint = event.position

                    ButtonState.Pressed
                }
            }
        }
}