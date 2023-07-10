package me.yunleah.plugin.coldestiny.internal.listener

import me.yunleah.plugin.coldestiny.internal.handle.PluginHandle
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.SubscribeEvent

object PlayerDeathListener {
    @SubscribeEvent
    fun listener(event: PlayerDeathEvent) {
        ToolsUtil.debug("监听到玩家死亡 => ${event.entity.player!!.name}")
        PluginHandle.preHandle(event)
    }
}