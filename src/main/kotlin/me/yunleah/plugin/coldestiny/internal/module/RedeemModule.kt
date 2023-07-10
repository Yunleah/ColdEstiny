package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object RedeemModule {
    fun loadConfigModule(configFile:  ArrayList<File>) {
        val configFileList = configFile.filter { it.name.endsWith("Redeem.yml") }
        console().sendLang("plugin-loadModule", KEY, configFileList.size ,"赎回组")
    }
}