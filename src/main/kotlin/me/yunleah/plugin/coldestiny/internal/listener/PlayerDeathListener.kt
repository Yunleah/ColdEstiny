package me.yunleah.plugin.coldestiny.internal.listener

import me.yunleah.plugin.coldestiny.internal.handle.PluginHandle
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

object PlayerDeathListener {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: PlayerDeathEvent) {
        event.keepLevel = true
        event.keepInventory = true
        ToolsUtil.debug("监听到玩家死亡 => ${event.entity.player!!.name}")
        PluginHandle.preHandle(event)
    }
}