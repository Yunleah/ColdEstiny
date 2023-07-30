package me.yunleah.plugin.coldestiny.internal.listner

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.settingDeathInfo
import me.yunleah.plugin.coldestiny.internal.module.ManagerModule
import me.yunleah.plugin.coldestiny.internal.module.RegionModule
import me.yunleah.plugin.coldestiny.internal.module.SelectModule
import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.SectionUtil.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import me.yunleah.plugin.coldestiny.util.ToolsUtil.timing
import org.bukkit.GameRule
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.console
import taboolib.module.kether.printKetherErrorMessage

object PlayerDeathListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listenerKeep(event: PlayerDeathEvent) {
        if (settingDeathInfo)
            console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...".replace("&", "§"))
        else
            debug("监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...")
        val startTime = timing()
        // 关闭死亡不掉落
        event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, false)
        // 获取Select
        val selectFile = SelectModule.checkSelect(event.entity.player!!)
        debug("获取到的Select -> $selectFile")
        // 获取Region
        val regionFile = RegionModule.checkRegion(event.entity.player!!)
        debug("获取到的Region -> $regionFile")
        // 获取Manager
        if (selectFile != null && regionFile != null) {
            val managerFile = ManagerModule.checkManager(getKey(selectFile, "SelectGroup.GroupKey")!!, getKey(regionFile, "RegionGroup.GroupKey")!!)
            debug("获取到的Manager -> $managerFile")

            //获取Pre-Action
            val preAction = getKey(managerFile!!, "ManagerGroup.runAction.Pre-Action")
            //运行Pre-Action
            var preResult = false
            try {
                val script = if (preAction!!.startsWith("def")) {
                    preAction
                } else {
                    "def main = { $preAction }"
                }
                script.toKetherScript().runActions {
                    this.sender = adaptCommandSender(event.entity)
                    if (sender is Player) {
                        set("player", sender as Player)
                        set("hand", (sender as Player).inventory.itemInMainHand)
                    }
                }.thenAccept {
                    if (it != false) preResult = true
                    debug("Pre-Action 未处理结果 -> $it")
                }
            } catch (e: Exception) {
                e.printKetherErrorMessage()
            }
            // Pre-Action 运行结果
            debug("Pre-Action 处理后结果 -> $preResult")
            if (preResult) {
                // 设置世界死亡不掉落
                event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, true)




            }
        } else {
            debug("Region & Select 不存在.")
        }
        if (settingDeathInfo)
            console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f插件逻辑执行完毕! 耗时 &6${timing(startTime)} &fms".replace("&", "§"))
        else
            debug("插件逻辑执行完毕! 耗时 &6${timing(startTime)} &fms")
    }
}