package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny

object ConfigManager {
    val settingDebugEnable
        get() = ColdEstiny.setting.getBoolean("Options.Debug")
    val settingDeathInfo
        get() = ColdEstiny.setting.getBoolean("Options.DeathInfo")
}