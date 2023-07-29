package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.regionFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
import taboolib.platform.util.bukkitPlugin
import java.io.File

object RegionModule {
    fun loadRegionFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "region"))
        console().sendLang("Plugin-LoadFile", regionFileList.size, "Region", ToolsUtil.timing(startTime))
    }
}