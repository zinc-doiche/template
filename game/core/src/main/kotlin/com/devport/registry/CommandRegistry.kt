package com.devport.registry

import com.devport.annotation.All
import com.devport.plugin
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

interface CommandRegistry {

    fun commands(): List<CommandHolder>

    @All
    fun register() {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            commands().forEach { commandHolder ->
                event.registrar().register(
                    commandHolder.command,
                    commandHolder.permission,
                    commandHolder.aliases
                )
            }
        }
    }
}

data class CommandHolder(
    val command: LiteralCommandNode<CommandSourceStack>,
    val permission: String,
    val aliases: List<String>
)