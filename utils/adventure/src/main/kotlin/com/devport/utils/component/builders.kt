package com.devport.utils.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

infix fun Component.color(color: TextColor) = this.color(color)

infix fun Component.append(component: Component) = this.append(component)

infix fun Component.append(text: String) = this.append(Component.text(text))

fun Component.append(text: String, color: TextColor) = this append Component.text(text, color)

infix fun Component.prepend(component: Component) = component.append(this)

infix fun Component.prepend(text: String) = Component.text(text).append(this)

fun Component.prepend(text: String, color: TextColor) = Component.text(text, color) append this