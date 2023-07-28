package me.yunleah.plugin.coldestiny.internal.listner

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.settingDeathInfo
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import me.yunleah.plugin.coldestiny.util.ToolsUtil.timing
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console

object PlayerDeathListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listenerKeep(event: PlayerDeathEvent) {
        if (settingDeathInfo)
            console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...".replace("&", "§"))
        else
            debug("监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...")
        val startTime = timing()


        // 设置世界死亡不掉落
        event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, true)




        if (settingDeathInfo)
            console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f插件逻辑执行完毕! 耗时 &6${timing(startTime)} &fms".replace("&", "§"))
        else
            debug("插件逻辑执行完毕! 耗时 &6${timing(startTime)} &fms")
    }

}