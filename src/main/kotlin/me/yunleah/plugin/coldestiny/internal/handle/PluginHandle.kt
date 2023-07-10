package me.yunleah.plugin.coldestiny.internal.handle

import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.submitAsync

object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        mainHandle()
    }

    private fun mainHandle() {
        postHandle()
    }

    private fun postHandle() {

    }
}