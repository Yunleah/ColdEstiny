package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang
import java.io.File

object ToolsUtil {
    fun debug(text: String) {
        if (ConfigManager.settingDebugEnable) {
          return console().sendLang("Plugin-Debug", text)
        }
    }

    fun timing(): Long {
        return System.nanoTime()
    }

    fun timing(start: Long): Double {
        return Coerce.format((System.nanoTime() - start).div(1000000.0))
    }
}