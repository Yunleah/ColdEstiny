package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.manager.GetManager
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.platform.util.bukkitPlugin
import java.io.File
import javax.tools.Tool

object SpawnModule {
    fun spawnModule(event: PlayerDeathEvent, config: File): Location? {
        val spawnType = getKey(config, "Options.PlayerSpawn.Spawn.Type").toString()
        when (spawnType) {
            "death" -> {
                return event.entity.lastDeathLocation
            }

            "coo" -> {
                val coo = getKey(config, "Options.PlayerSpawn.Spawn.Info").toString()
                val world = coo.split(" ").last().toString()
                val xyz = coo.removeSuffix(world).split(" ")
                val x = xyz[0].toDouble()
                val y = xyz[1].toDouble()
                val z = xyz[2].toDouble()
                return Location(Bukkit.getWorld(world), x, y, z)
            }

            "loc" -> {
                return event.entity.player!!.bedSpawnLocation
            }
        }
        return null
    }
}