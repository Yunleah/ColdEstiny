package me.yunleah.plugin.coldestiny.internal.listener

import me.yunleah.plugin.coldestiny.internal.handle.PluginHandle
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit

object PlayerDeathListener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listenKeep(event: PlayerDeathEvent) {
        event.keepLevel = true

        event.keepInventory = true
        ToolsUtil.debug("KeepLevel -> ${event.keepLevel}")
        ToolsUtil.debug("KeepInventory -> ${event.keepInventory}")
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    fun listener(event: PlayerDeathEvent) {
        ToolsUtil.debug("当前死亡掉落模式 -> ${event.keepInventory}")
        if (!event.keepInventory)

        ToolsUtil.debug("监听到玩家死亡 => ${event.entity.player!!.name}")
        submit(async = true) {
            PluginHandle.preHandle(event)
        }
    }
}