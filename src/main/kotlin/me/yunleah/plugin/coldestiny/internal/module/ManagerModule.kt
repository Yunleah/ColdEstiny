package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.managerFileList
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.regionFileList
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.selectFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import java.io.File

object ManagerModule {

    fun loadManagerFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "manager"))
        console().sendLang("Plugin-LoadFile", managerFileList.size, "Manager", ToolsUtil.timing(startTime))
    }

    fun checkManager(selectKey: String, regionKey: String): File? {
        val managerFile: ArrayList<File> = arrayListOf()
        val nullSelect = managerFileList.filter {
            return@filter "null" == SectionUtil.getKey(it, "ManagerGroup.BindGroup.SelectKey")
        }
        val nullRegion = managerFileList.filter {
            return@filter "null" == SectionUtil.getKey(it, "ManagerGroup.BindGroup.RegionKey")
        }
        managerFile += nullSelect.filter { file ->
            return@filter SectionUtil.getKey(file, "ManagerGroup.BindGroup.RegionKey") != "null"
        }
        managerFile += nullRegion.filter { file ->
            return@filter SectionUtil.getKey(file, "ManagerGroup.BindGroup.SelectKey") != "null"
        }
        managerFile += managerFileList.filter {
            return@filter selectKey == SectionUtil.getKey(it, "ManagerGroup.BindGroup.SelectKey") && regionKey == SectionUtil.getKey(it, "ManagerGroup.BindGroup.RegionKey")
        }

        return when (managerFile.size) {
            0 -> {
               null
            }
            1 -> {
                managerFile.first()
            }
            else -> {
                val weighFileList = SectionUtil.getFileKey(managerFile, "ManagerGroup.Weigh")
                val highWeigh = weighFileList.maxOfOrNull { it.second.cint }
                weighFileList.filter { it.second.cint == highWeigh }.map { it.first }.first()
            }
        }
    }
}