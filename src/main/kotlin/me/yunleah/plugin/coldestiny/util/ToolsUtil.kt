package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import taboolib.common.platform.function.console
import taboolib.module.lang.sendWarn

object ToolsUtil {
    fun debug(text: String) {
        if (ConfigManager.Setting_Debug)
            return console().sendWarn("plugin-debug", text)
    }
}