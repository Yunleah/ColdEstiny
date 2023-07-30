package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.util.FileUtil
import taboolib.common.platform.function.getDataFolder
import java.io.File

object ConfigManager {
    val settingDebugEnable
        get() = ColdEstiny.setting.getBoolean("Options.Debug")
    val settingDeathInfo
        get() = ColdEstiny.setting.getBoolean("Options.DeathInfo")
    val dropFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace/drop")).filter { it.name.endsWith("yml") }
    val managerFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace/manager")).filter { it.name.endsWith("yml") }
    val regionFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace/region")).filter { it.name.endsWith("yml") }
    val relicsFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace/relics")).filter { it.name.endsWith("yml") }
    val selectFileList
        get() = FileUtil.getAllFiles(File(getDataFolder(), File.separator + "workspace/select")).filter { it.name.endsWith("yml") }
}