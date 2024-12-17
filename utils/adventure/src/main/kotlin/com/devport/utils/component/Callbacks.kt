package com.devport.utils.component

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.jetbrains.annotations.Range
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun Component.callback(
    uses: @Range(from = 1, to = 100) Int = 1,
    lifetime: Duration = 1.minutes,
    callback: (Audience) -> Unit
) = this.clickEvent(
    ClickEvent.callback(callback,
        ClickCallback.Options.builder()
            .uses(uses)
            .lifetime(lifetime.toJavaDuration())
            .build())
)

fun Component.hoverText(component: Component) = this.hoverEvent(HoverEvent.showText(component))

fun Component.hoverText(text: String) = this.hoverEvent(HoverEvent.showText(Component.text(text)))