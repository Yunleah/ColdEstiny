package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common5.cbool
import java.io.File

object SpawnModule {
    fun checkSpawnLocation(file: File, event: PlayerDeathEvent): Location? {
        val spawnEnable = SectionUtil.getKey(file, "ManagerGroup.SpawnPlayer.Enable").cbool
        if (!spawnEnable) return null
        val spawnType = SectionUtil.getKey(file, "ManagerGroup.SpawnPlayer.Type")
        val spawnInfo = SectionUtil.getKey(file, "ManagerGroup.SpawnPlayer.Info")
        when (spawnType) {
            "death" -> {
                return event.entity.location
            }
            "coo" -> {
                val world = spawnInfo!!.split(" ").last().toString()
                val xyz = spawnInfo.removeSuffix(" $world").split(" ")
                val x = xyz[0].toDouble()
                val y = xyz[1].toDouble()
                val z = xyz[2].toDouble()
                return Location(Bukkit.getWorld(world), x, y, z)
            }
            "loc" -> {
                val bedSpawnLocation = event.entity.player?.world?.spawnLocation
                if (bedSpawnLocation is Location) {
                    return bedSpawnLocation
                }
                ToolsUtil.debug("当前世界不存在重生点 -> ${event.entity.world.name}")
                return null
            }
            else -> throw IllegalArgumentException("Invalid spawn type")
        }
    }
}