package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.DropFileList
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.RedeemFileList
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getFileKey
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.internal.module.RedeemModule
import me.yunleah.plugin.coldestiny.internal.module.SpawnModule
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.*
import taboolib.common5.clong
import taboolib.module.lang.sendError
import taboolib.platform.util.bukkitPlugin
import java.io.File
import java.time.LocalDateTime

object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        debug("<->preHandle开始运行...")
        val redeemFile = FileUtil.getFileOrCreate("data${File.separator}${event.entity.player!!.uniqueId}${File.separator}${LocalDateTime.now()}.yml")
        val worldConfig = ConfigModule.worldConfigModule(event) ?: return debug("玩家暂未被托管")
        val permConfig = ConfigModule.permConfigModule(worldConfig as ArrayList<File>, event)
        if (permConfig == null) {
            return debug("玩家暂未被托管")
        } else if (permConfig.size != 1) {
            return console().sendError("plugin-format","有不止一个配置组符合玩家 -> ${event.entity.player!!.name} | 配置组 -> $permConfig")
        }
        val configPath = permConfig.first().toString()
        val dropPath = DropModule.dropFileModule(configPath, DropFileList as ArrayList<File>)?.first()
            ?: return console().sendError("DropConfig -> Null")
        //Pre Action
        val preScriptEnable = getKey(permConfig.first(), "Options.Action.Pre.Enable").toBoolean()
        if (preScriptEnable) {
            if (KetherHandle.runKetherHandle(permConfig.first(), event, "Pre"))
                mainHandle(redeemFile, permConfig, dropPath, event)
        }
    }

    private fun mainHandle(redeem: File, config: ArrayList<File>,drop: File, event: PlayerDeathEvent) {
        debug("<->mainHandle开始运行...")
        debug("redeem -> $redeem")
        debug("config -> $config")
        debug("drop -> $drop")
        val dropItemList = DropModule.preDropModule(event,drop)
        val spawn = SpawnModule.spawnModule(event, config.first())
        postHandle(dropItemList, spawn, config.first(), event)
    }

    private fun postHandle(dropItemList: MutableList<Int>, spawn: Location?, config: File, event: PlayerDeathEvent) {
        val removedItems = mutableListOf<ItemStack>()
        val time = SpawnModule.spawnAuto(config)
        if (time != 0.clong) {
            Bukkit.getScheduler().runTaskLater(bukkitPlugin, Runnable {
                val inv = event.entity.player!!.inventory

                for (dropId in dropItemList) {
                    val itemS = inv.getItem(dropId)
                    inv.removeItem(itemS)
                    if (getKey(config, "Options.ItemRedemption.Enable").toBoolean()) {
                        val reConf = getFileKey(RedeemFileList as ArrayList<File>, "Options.Key").filter { it.second == getKey(config, "Options.ItemRedemption.Bind") }.map { it.first }.first()
                        val perm = getKey(reConf, "Options.Perm")
                        val verify = event.entity.player!!.hasPermission(perm!!)
                        if (!verify) {
                            val world = event.entity.world
                            val loc = event.entity.location
                            world.dropItem(loc, itemS!!)
                        }
                    }
                    removedItems.add(itemS!!)
                }
               event.entity.spigot().respawn()
                event.entity.player!!.teleport(spawn!!)
            }, time)
        }


        //Post Action
        val postScriptEnable = getKey(config, "Options.Action.Post.Enable").toBoolean()
        if (postScriptEnable) {
            KetherHandle.runKetherHandle(config, event, "Post")
        }
        RedeemModule.preRedeemModule(removedItems)
        return debug("ColdEstiny -> Finish")
    }
}