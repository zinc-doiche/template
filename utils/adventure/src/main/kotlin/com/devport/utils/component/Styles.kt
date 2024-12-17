package com.devport.utils.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

// Decorations ==============================================================

fun Component.bold() = this.decoration(TextDecoration.BOLD, true)

fun Component.noItalic() = this.decoration(TextDecoration.ITALIC, false)

// Colors ==============================================================

fun Component.warn() = this color NamedTextColor.RED

fun Component.whiteLike() = this color TextColor.color(254, 254, 254)