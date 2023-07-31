package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.hook.griefdefender.impl.GriefDefenderHookerImpl
import me.yunleah.plugin.coldestiny.internal.hook.residence.impl.ResidenceHookerImpl
import me.yunleah.plugin.coldestiny.internal.hook.worldguard.impl.WorldGuardHookerImpl
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.regionFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import java.io.File

object RegionModule {
    fun loadRegionFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "region"))
        console().sendLang("Plugin-LoadFile", regionFileList.size, "Region", ToolsUtil.timing(startTime))
    }
    fun checkRegion(player: Player): File? {
        if (regionFileList.isEmpty()) {
            return null
        }
        val regionFile = regionFileList.filter {
            val regionType = SectionUtil.getKey(it, "RegionGroup.Options.Type")
            when (regionType) {
                "world" -> {
                    val world = SectionUtil.getList(it, "RegionGroup.Options.Info")
                    if (player.world.name in world) { return@filter true }
                }
                "res" -> {
                    val res = SectionUtil.getList(it, "RegionGroup.Options.Info")
                    if (ResidenceHookerImpl().getLocation(player) in res) { return@filter true }
                }
                "gd" -> {
                    val gd = SectionUtil.getList(it, "RegionGroup.Options.Info")
                    if (GriefDefenderHookerImpl().getLocation(player) in gd) { return@filter true }
                }
                "wg" -> {
                    val wg = SectionUtil.getList(it, "RegionGroup.Options.Info")
                    if (WorldGuardHookerImpl().getLocation(player) in wg) { return@filter true }
                }
            }
            return@filter false
        } as ArrayList<File>
        return when (regionFile.size) {
            0 -> {
                null
            }
            1 -> {
                regionFile.first()
            }
            else -> {
                val weighFileList = SectionUtil.getFileKey(regionFile, "RegionGroup.Weigh")
                val highWeigh = weighFileList.maxOfOrNull { it.second.cint }
                weighFileList.filter { it.second.cint == highWeigh }.map { it.first }.first()
            }
        }
    }
}