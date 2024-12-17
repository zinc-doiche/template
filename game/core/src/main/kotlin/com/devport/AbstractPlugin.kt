package com.devport

import com.devport.generated.AutoListenerRegisters
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: AbstractPlugin

abstract class AbstractPlugin: JavaPlugin() {

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        AutoListenerRegisters.registerAll()
    }

    override fun onDisable() {

    }
}