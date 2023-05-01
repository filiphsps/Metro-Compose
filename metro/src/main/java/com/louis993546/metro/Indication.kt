package com.louis993546.metro

import android.util.Log
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp

@Composable
fun rememberMetro(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
): Indication {
    return remember(bounded, radius) {
        Metro(bounded, radius)
    }
}

internal class Metro(
    private val bounded: Boolean,
    private val radius: Dp,
) : Indication {
    @Composable
    override fun rememberUpdatedInstance(
        interactionSource: InteractionSource
    ): IndicationInstance {
        val instance = rememberUpdatedMetroInstance(
            interactionSource,
            bounded,
            radius,
        )

        LaunchedEffect(instance, interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> Log.i("interaction", "press")
                    is PressInteraction.Release -> Log.i("interaction", "release")
                    is PressInteraction.Cancel -> Log.i("interaction", "cancel")
                    else -> Log.i("interaction", "else")
                }
            }
        }

        return instance
    }

    @Composable
    fun rememberUpdatedMetroInstance(
        interactionSource: InteractionSource,
        bounded: Boolean,
        radius: Dp,
    ): MetroIndicationInstance {
        return remember(interactionSource, this) {
            CommonMetroIndicationInstance(bounded, radius)
        }
    }
}

internal class CommonMetroIndicationInstance(
    private val bounded: Boolean,
    private val radius: Dp,
) : MetroIndicationInstance(bounded), RememberObserver {
    override fun ContentDrawScope.drawIndication() {
        drawContent()
    }

    override fun onRemembered() {}

    override fun onForgotten() {
    }

    override fun onAbandoned() {
    }
}

internal abstract class MetroIndicationInstance(
    bounded: Boolean,
) : IndicationInstance
