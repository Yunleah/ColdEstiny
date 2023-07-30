package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.dropFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.module.lang.sendLang
import java.io.File

object DropModule {
    fun loadDropFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "drop"))
        console().sendLang("Plugin-LoadFile", dropFileList.size, "Drop", ToolsUtil.timing(startTime))
    }

    fun checkDrop(managerFile: File): File {
        val dropKey = SectionUtil.getKey(managerFile, "ManagerGroup.BindGroup.DropKey")
        val dropFile = dropFileList.filter {
            val key = SectionUtil.getKey(it, "DropGroup.GroupKey")
            return@filter key == dropKey
        } as ArrayList<File>
        return dropFile.first()
    }
}