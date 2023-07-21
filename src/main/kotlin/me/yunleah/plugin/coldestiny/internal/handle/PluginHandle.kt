package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.DropFileList
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.internal.module.SpawnModule
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.*
import taboolib.module.lang.sendError
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import java.io.File


object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        debug("<->preHandle开始运行...")
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
                mainHandle(permConfig, dropPath, event)
        }
    }

    private fun mainHandle(config: ArrayList<File>,drop: File, event: PlayerDeathEvent) {
        debug("<->mainHandle开始运行...")
        debug("config -> $config")
        debug("drop -> $drop")
        val dropItemList = DropModule.preDropModule(event,drop)
        val spawn = SpawnModule.spawnModule(event, config.first())
        postHandle(dropItemList, spawn, config.first(), event, drop)
    }

    private fun postHandle(list: Pair<MutableList<Int>, Int?>, spawn: Location?, config: File, event: PlayerDeathEvent, drop: File) {
        var tick = 0
        if (getKey(config, "Options.PlayerSpawn.AutoRespawn.Enable").toBoolean()) {
            tick = getKey(config, "Options.PlayerSpawn.AutoRespawn.Time")!!.toInt()
        }

        Bukkit.getScheduler().runTaskLater(bukkitPlugin, Runnable {
            event.entity.spigot().respawn()
            event.entity.player!!.teleport(spawn!!)
        }, tick.toLong())

        val removedItems = mutableListOf<ItemStack>()
        val slotList = list.first as ArrayList<Int>
        val player = event.entity.player
        val world = player!!.world
        val inv = player.inventory
        val loc = player.location
        for (slot in slotList) {
            val iStack = inv.getItem(slot)
            removedItems.add(iStack!!)
            inv.setItem(slot, ItemStack(Material.AIR))
            if (inv.getItem(slot).isNotAir()) {
                debug("出现错误！物品未正确清理！ 已删除凋落物！")
                inv.removeItem(inv.getItem(slot))
                if (inv.getItem(slot).isNotAir()) {
                    debug("Error 出错！")
                } else {
                    world.dropItem(loc, iStack)
                }
            }
            else {
                world.dropItem(loc, iStack)
                debug("物品正确掉落！")
            }
        }

        val newExpLevel = list.second
        val didnt = getKey(drop, "Options.Drop.Exp.Didnt")?.toInt()
        if (didnt == 0) {
            player.level = newExpLevel!!.toInt()
        } else {
            if (player.level >= didnt!!) {
                player.level = newExpLevel!!.toInt()
            }
        }

        //Post Action
        val postScriptEnable = getKey(config, "Options.Action.Post.Enable").toBoolean()
        if (postScriptEnable) {
            KetherHandle.runKetherHandle(config, event, "Post")
        }
        return debug("ColdEstiny -> Finish")
    }
}