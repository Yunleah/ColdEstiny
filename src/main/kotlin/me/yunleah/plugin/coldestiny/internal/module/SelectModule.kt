package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.selectFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import java.io.File

object SelectModule {
    fun loadSelectFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "select"))
        console().sendLang("Plugin-LoadFile", selectFileList.size, "Select", ToolsUtil.timing(startTime))
    }

    fun checkSelect(player: Player): File? {
        if (selectFileList.isEmpty()) {
            return null
        }
        val selectFile = selectFileList.filter {
            val selectType = SectionUtil.getKey(it, "SelectGroup.Options.Select")
            when (selectType) {
                "player" -> {
                    val playerList = SectionUtil.getList(it, "SelectGroup.Options.Info")
                    if (player.name in playerList) { return@filter true }
                }
                "perm" -> {
                    val perm = SectionUtil.getKey(it, "SelectGroup.Options.Info")
                    if (player.hasPermission(perm!!)) { return@filter true }
                }
            }
            return@filter false
        } as ArrayList<File>
        return when (selectFile.size) {
            0 -> {
                null
            }

            1 -> {
                selectFile.first()
            }

            else -> {
                val weighFileList = SectionUtil.getFileKey(selectFile, "SelectGroup.Weigh")
                val highWeigh = weighFileList.maxOfOrNull { it.second.cint }
                weighFileList.filter { it.second.cint == highWeigh }.map { it.first }.first()
            }
        }
    }
}