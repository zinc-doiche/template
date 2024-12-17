package com.devport.registry

import com.devport.annotation.All
import com.devport.plugin
import org.bukkit.event.Listener

interface AutoListener : Listener {
    @All
    fun register() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
}