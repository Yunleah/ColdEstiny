package me.yunleah.plugin.coldestiny.internal.listner

import me.yunleah.plugin.coldestiny.ColdEstiny.bukkitScheduler
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.settingDeathInfo
import me.yunleah.plugin.coldestiny.internal.manager.DropManager
import me.yunleah.plugin.coldestiny.internal.module.*
import me.yunleah.plugin.coldestiny.internal.module.KetherModule.runKether
import me.yunleah.plugin.coldestiny.util.SectionUtil.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil.timing
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common5.cbool
import taboolib.common5.clong

object PlayerDeathListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listenerKeep(event: PlayerDeathEvent) {
        if (settingDeathInfo)
            console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...".replace("&", "§"))
        else
            debug("监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...")
        // 设置世界死亡不掉落
        try {
            event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, true)
        } catch (error: NoClassDefFoundError) {
            console().sendMessage("§8[§3Cold§bEstiny§8] §e调试 §8| GameRule -> ${error.cause}")
            console().sendMessage("§8[§3Cold§bEstiny§8] §e调试 §8| 手动设置中...")
            event.keepInventory = true
            console().sendMessage("§8[§3Cold§bEstiny§8] §e调试 §8| keepInventory -> ${event.keepInventory}")
            event.keepLevel = true
            console().sendMessage("§8[§3Cold§bEstiny§8] §e调试 §8| keepLevel -> ${event.keepLevel}")
        }
        bukkitScheduler.callSyncMethod(plugin) {
            val startTime = timing()
            DropManager.runDrop(event, null, true)

            if (settingDeathInfo)
                console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f插件逻辑执行完毕! 耗时 &6${timing(startTime)} &fms".replace("&", "§"))
            else
                debug("插件逻辑执行完毕! 耗时 &6${timing(startTime)} &fms")
        }
    }
}