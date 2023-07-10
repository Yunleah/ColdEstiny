package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console

object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        val playerInWorld = event.entity.world
        if (!ConfigModule.worldConfigModule(event)) {
            return ToolsUtil.debug("玩家所在世界未被本插件托管 -> ${playerInWorld.name}")
        }
        mainHandle()
    }

    private fun mainHandle() {
        postHandle()
    }

    private fun postHandle() {

    }
}