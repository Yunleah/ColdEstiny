package me.yunleah.plugin.coldestiny.internal.listner

import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.settingDeathInfo
import me.yunleah.plugin.coldestiny.internal.module.*
import me.yunleah.plugin.coldestiny.internal.module.KetherModule.runKether
import me.yunleah.plugin.coldestiny.util.ItemUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import me.yunleah.plugin.coldestiny.util.ToolsUtil.timing
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit

object PlayerDeathListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listenerKeep(event: PlayerDeathEvent) {
        submit(async = true) {
            if (settingDeathInfo)
                console().sendMessage("&8[&3Cold&bEstiny&8] &e调试 &8| &f监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...".replace("&", "§"))
            else
                debug("监听到玩家${event.entity.player!!.name}死亡 开始处理插件逻辑...")
            val startTime = timing()
            // 设置世界死亡不掉落
            event.entity.world.setGameRule(GameRule.KEEP_INVENTORY, true)
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
                val preResult = preAction?.runKether(event)
                // Pre-Action 运行结果
                debug("Pre-Action 运行结果 -> $preResult")
                if (preResult == true) {
                    //处理玩家掉落
                    //获取Drop
                    val dropFile = DropModule.checkDrop(managerFile)
                    debug("获取到的Drop -> $dropFile")
                    val dropInv = DropModule.checkDropInv(dropFile, event.entity.player!!, event)
                    val itemStackInv: ArrayList<ItemStack> = arrayListOf()
                    debug("剔除玩家背包物品 -> Run")
                    dropInv.forEach { slot ->
                        val item = event.entity.player!!.inventory.getItem(slot)
                        itemStackInv.add(item!!)
                        event.entity.player!!.inventory.setItem(slot, null)
                    }
                    debug("剔除玩家背包物品 -> End")
                    //获取Post-Action
                    val postAction = getKey(managerFile, "ManagerGroup.runAction.Post-Action")
                    //运行Post-Action
                    val postResult = postAction?.runKether(event)
                    debug("Post-Action 运行结果 -> $postResult")
                    if (postResult == true) {
                        //处理玩家遗物
                        //获取Relics
                        val relicsFile = RelicsModule.checkRelics(managerFile)
                        debug("获取到的Relics -> $relicsFile")
                        val type = getKey(relicsFile, "RelicsGroup.Options.Type")
                        RelicsModule.runRelics(relicsFile, type!!, event, itemStackInv)
                    }
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
}