package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object DropModule {
    fun loadDropModule(dropFile:  ArrayList<File>) {
        val dropFileList = dropFile.filter { it.name.endsWith("Drop.yml") }
        console().sendLang("plugin-loadModule", KEY, dropFileList.size ,"掉落组")
    }
}