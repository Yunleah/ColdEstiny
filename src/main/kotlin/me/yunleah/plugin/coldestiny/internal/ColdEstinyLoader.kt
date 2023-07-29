package me.yunleah.plugin.coldestiny.internal

import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.module.*
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit

import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import javax.swing.plaf.synth.Region

object ColdEstinyLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
        console().sendMessage("")
        console().sendLang("Plugin-Loading", Bukkit.getServer().version)
        console().sendMessage("")
        Metrics(19017, plugin.description.version, Platform.BUKKIT)
    }
    @Awake(LifeCycle.ENABLE)
    fun enable() {
        console().sendMessage("")
        console().sendMessage("§b  ▄▄·       ▄▄▌  ·▄▄▄▄  ▄▄▄ ..▄▄ · ▄▄▄▄▄▪   ▐ ▄  ▄· ▄▌")
        console().sendMessage("§b ▐█ ▌▪▪     ██•  ██▪ ██ ▀▄.▀·▐█ ▀. •██  ██ •█▌▐█▐█▪██▌")
        console().sendMessage("§b ██ ▄▄ ▄█▀▄ ██▪  ▐█· ▐█▌▐▀▀▪▄▄▀▀▀█▄ ▐█.▪▐█·▐█▐▐▌▐█▌▐█▪")
        console().sendMessage("§b ▐███▌▐█▌.▐▌▐█▌▐▌██. ██ ▐█▄▄▌▐█▄▪▐█ ▐█▌·▐█▌██▐█▌ ▐█▀·.")
        console().sendMessage("§b ·▀▀▀  ▀█▄▀▪.▀▀▀ ▀▀▀▀▀•  ▀▀▀  ▀▀▀▀  ▀▀▀ ▀▀▀▀▀ █▪  ▀ • ")
        console().sendMessage("")
        DropModule.loadDropFile()
        ManagerModule.loadManagerFile()
        RegionModule.loadRegionFile()
        RelicsModule.loadRelicsFile()
        SelectModule.loadSelectFile()
        console().sendLang("Plugin-Enabled")
        ToolsUtil.debug("Debug模式已开启.")
    }
    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("Plugin-Disable")
    }
}