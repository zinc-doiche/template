package com.devport.proxy;

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "proxy",
    name = "proxy",
    version = BuildConstants.VERSION
)
class Main @Inject constructor(
    val proxyServer: ProxyServer,
    @DataDirectory val dataDirectory: Path,
    val logger: Logger
) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {

    }
}
