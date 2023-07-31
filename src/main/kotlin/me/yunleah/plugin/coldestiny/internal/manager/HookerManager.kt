package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.internal.hook.griefdefender.GriefDefenderHooker
import me.yunleah.plugin.coldestiny.internal.hook.griefdefender.impl.GriefDefenderHookerImpl
import me.yunleah.plugin.coldestiny.internal.hook.residence.ResidenceHooker
import me.yunleah.plugin.coldestiny.internal.hook.residence.impl.ResidenceHookerImpl
import me.yunleah.plugin.coldestiny.internal.hook.worldguard.impl.WorldGuardHookerImpl
import org.bukkit.Bukkit
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * 插件兼容管理器, 用于尝试与各个软依赖插件取得联系
 */
object HookerManager {
    val ResidenceHooker: ResidenceHooker? =
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
    val GriefDefenderHooker: GriefDefenderHooker? =
        if (Bukkit.getPluginManager().isPluginEnabled("GriefDefender")) {
            console().sendLang("Plugin-Hooker-True", "GriefDefender")
            try {
                GriefDefenderHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            console().sendLang("Plugin-Hooker-False", "GriefDefender")
            null
        }
    val WorldGuardHooker: WorldGuardHookerImpl? =
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            console().sendLang("Plugin-Hooker-True", "WorldGuard")
            try {
                WorldGuardHookerImpl()
            } catch (error: Throwable) {
                null
            }
        } else {
            console().sendLang("Plugin-Hooker-False", "WorldGuard")
            null
        }
}