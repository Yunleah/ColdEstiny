package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.manager.GetManager
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.common5.clong
import taboolib.module.lang.sendError
import taboolib.platform.util.bukkitPlugin
import java.io.File
import javax.tools.Tool

object SpawnModule {
    fun spawnModule(event: PlayerDeathEvent, config: File): Location? {
        debug("spawnModule -> Run")
        val spawnType = getKey(config, "Options.PlayerSpawn.Spawn.Type").toString()
        debug("spawnType -> $spawnType")
        when (spawnType) {
            "death" -> {
                debug("Type -> death")
                val lastDeathLocation = event.entity.location
                debug("Location -> $lastDeathLocation")
                return lastDeathLocation
            }

            "coo" -> {
                debug("Type -> coo")
                val coo = getKey(config, "Options.PlayerSpawn.Spawn.Info").toString()
                val world = coo.split(" ").last().toString()
                val xyz = coo.removeSuffix(" $world").split(" ")
                val x = xyz[0].toDouble()
                val y = xyz[1].toDouble()
                val z = xyz[2].toDouble()
                val location = Location(Bukkit.getWorld(world), x, y, z)
                debug("Location -> $location")
                return location
            }

            "loc" -> {
                debug("Type -> loc")
                val bedSpawnLocation = event.entity.player?.world?.spawnLocation
                if (bedSpawnLocation != null) {
                    debug("Location -> $bedSpawnLocation")
                    return bedSpawnLocation
                } else {
                    console().sendError("世界[${event.entity.player?.world?.name}] -> 不存在重生点!")
                }
            }
        }
        return null
    }

    fun spawnAuto(config: File): Long {
        val timeEnable = getKey(config, "Options.PlayerSpawn.AutoRespawn.Enable").toBoolean()
        if (!timeEnable) { return 0.clong }
        return getKey(config, "Options.PlayerSpawn.AutoRespawn.Time")!!.removeSuffix("s").clong
    }
}