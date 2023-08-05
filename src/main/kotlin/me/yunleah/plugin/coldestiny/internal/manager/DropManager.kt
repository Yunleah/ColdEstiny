package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.ColdEstiny.bukkitScheduler
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.module.*
import me.yunleah.plugin.coldestiny.internal.module.KetherModule.runKether
import me.yunleah.plugin.coldestiny.util.SectionUtil.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common5.cbool
import taboolib.common5.clong

object DropManager {
    fun runDrop(event: PlayerDeathEvent? = null, p: Player? = null, spawn: Boolean) {
        val player: Player = if (event == null) p!!
        else event.entity.player!!
        // 获取Select
        val selectFile = SelectModule.checkSelect(player)
        debug("获取到的Select -> $selectFile")
        // 获取Region
        val regionFile = RegionModule.checkRegion(player)
        debug("获取到的Region -> $regionFile")
        // 获取Manager
        if (selectFile != null && regionFile != null) {
            val managerFile = ManagerModule.checkManager(
                getKey(selectFile, "SelectGroup.GroupKey")?: "null",
                getKey(regionFile, "RegionGroup.GroupKey")?: "null"
            )
            debug("获取到的Manager -> $managerFile")

            //获取Pre-Action
            val preAction = getKey(managerFile!!, "ManagerGroup.runAction.Pre-Action")
            //运行Pre-Action
            val preResult = preAction?.runKether(event, player)
            // Pre-Action 运行结果
            debug("Pre-Action 运行结果 -> $preResult")
            if (preResult == true) {
                //处理玩家掉落物品
                //获取Drop
                val dropFile = DropModule.checkDrop(managerFile)
                debug("获取到的Drop -> $dropFile")
                val dropInv = DropModule.checkDropInv(dropFile, player)
                val itemStackInv: ArrayList<ItemStack> = arrayListOf()
                debug("剔除玩家背包物品 -> Run")
                dropInv.forEach { slot ->
                    val item = player.inventory.getItem(slot)
                    itemStackInv.add(item!!)
                    player.inventory.setItem(slot, null)
                }
                debug("剔除玩家背包物品 -> End")

                //处理玩家掉落经验
                val dropExp = DropModule.checkDropExp(dropFile, player)
                val resultExp = player.level - dropExp
                player.level = resultExp

                //处理玩家遗物
                //获取Relics
                val relicsFile = RelicsModule.checkRelics(managerFile)
                debug("获取到的Relics -> $relicsFile")
                RelicsModule.runRelics(relicsFile, player, itemStackInv)
                //处理玩家Spawn
                val location = SpawnModule.checkSpawnLocation(managerFile, event, p)
                if (spawn) {
                    debug("最终获取的Location -> $location")
                    val autoEnable = getKey(managerFile, "ManagerGroup.SpawnPlayer.AutoSpawn.Enable").cbool
                    if (autoEnable && location != null) {
                        val autoTimed = getKey(managerFile, "ManagerGroup.SpawnPlayer.AutoSpawn.Timed").clong
                        bukkitScheduler.runTaskLater(plugin, Runnable {
                            player.spigot().respawn()
                            player.teleport(location)
                        }, autoTimed)
                    } else if (location != null){
                        player.spigot().respawn()
                        player.teleport(location)
                    }
                }
                //获取Post-Action
                val postAction = getKey(managerFile, "ManagerGroup.runAction.Post-Action")
                //运行Post-Action
                val postResult = postAction?.runKether(event, p,itemStackInv, location)
                debug("Post-Action 运行结果 -> $postResult")
            }
        } else {
            debug("Region & Select 不存在.")
        }
    }
}