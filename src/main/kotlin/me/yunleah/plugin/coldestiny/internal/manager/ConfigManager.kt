package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.util.FileUtil
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.submit
import taboolib.module.configuration.ConfigFile
import java.io.File

object ConfigManager {
    val Setting_Debug: Boolean
        get() = ColdEstiny.setting.getBoolean("Options.Debug")
    val DropFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace/drop")).filter { it.name.endsWith("Drop.yml") }
    val ConfigFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace")).filter { it.name.endsWith("Config.yml") }
}