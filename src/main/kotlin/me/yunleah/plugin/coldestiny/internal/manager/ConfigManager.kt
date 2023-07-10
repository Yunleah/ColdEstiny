package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny

object ConfigManager {
    val Setting_Debug: Boolean
        get() = ColdEstiny.setting.getBoolean("Options.Debug")

}