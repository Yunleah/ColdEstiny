package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getFileKey
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File
import kotlin.random.Random

object DropModule {
    var verify: Boolean = false
    fun loadDropModule(dropFile:  ArrayList<File>) {
        val dropFileList = dropFile.filter { it.name.endsWith("Drop.yml") }
        console().sendLang("plugin-loadModule", KEY, dropFileList.size ,"掉落组")
    }

    fun dropFileModule(configPath: String, dropFile: ArrayList<File>): List<File>? {
        val config = File(configPath)
        val dropEnable = getKey(config, "Options.Drop.Enable").toBoolean()

        return if (dropEnable) {
            val dropKey = getKey(config, "Options.Drop.Bind")
            val fileKey = getFileKey(dropFile.filter { it.name.endsWith("Drop.yml")} as ArrayList<File>,"Options.Key")
            fileKey.filter { it.second == dropKey }.map { it.first }
        } else {
            null
        }
    }
}