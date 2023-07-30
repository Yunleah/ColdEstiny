package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.internal.hook.residence.ResidenceHooker
import me.yunleah.plugin.coldestiny.internal.hook.residence.impl.ResidenceHookerImpl
import org.bukkit.Bukkit
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * 插件兼容管理器, 用于尝试与各个软依赖插件取得联系
 */
object HookerManager {
    val residenceHooker: ResidenceHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("Residence")) {
            console().sendLang("Plugin-Hooker-True", "Residence")
            try {
                ResidenceHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            console().sendLang("Plugin-Hooker-False", "Residence")
            null
        }
}