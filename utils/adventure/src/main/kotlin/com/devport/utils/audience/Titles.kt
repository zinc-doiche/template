package com.devport.utils.audience

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import kotlin.time.Duration
import kotlin.time.toJavaDuration

typealias TitleTimes = Title.Times

@Suppress("FunctionName")
fun TitleTimes(
    fadeIn: Duration = Duration.ZERO,
    stay: Duration = Duration.ZERO,
    fadeOut: Duration = Duration.ZERO
) = Title.Times.times(
    fadeIn.toJavaDuration(),
    stay.toJavaDuration(),
    fadeOut.toJavaDuration()
)

fun Title(
    title: Component = Component.empty(),
    subtitle: Component = Component.empty(),
    times: TitleTimes = Title.DEFAULT_TIMES
) = Title.title(title, subtitle, times)