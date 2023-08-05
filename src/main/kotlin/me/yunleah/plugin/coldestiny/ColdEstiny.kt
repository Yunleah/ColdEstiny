package me.yunleah.plugin.coldestiny

import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

object ColdEstiny : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    val bukkitScheduler by lazy { Bukkit.getScheduler() }

    @Config("setting.yml", true)
    lateinit var setting: ConfigFile
}