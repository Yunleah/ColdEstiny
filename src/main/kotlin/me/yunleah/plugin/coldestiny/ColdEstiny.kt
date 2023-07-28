package me.yunleah.plugin.coldestiny

import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

object ColdEstiny : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("setting.yml", true)
    lateinit var setting: ConfigFile

}