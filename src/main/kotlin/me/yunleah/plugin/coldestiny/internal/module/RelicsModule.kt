package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.relicsFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
import taboolib.platform.util.bukkitPlugin
import java.io.File

object RelicsModule {
    fun loadRelicsFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "relics"))
        console().sendLang("Plugin-LoadFile", relicsFileList.size, "Relics", ToolsUtil.timing(startTime))
    }
}