package me.yunleah.plugin.coldestiny.internal

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.ConfigFileList
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.DropFileList
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.RedeemFileList
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.internal.module.RedeemModule
import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics
import java.io.File


object ColdEstinyLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
        Metrics(19017, plugin.description.version, Platform.BUKKIT)
        console().sendLang("plugin-load", KEY)
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
        console().sendLang("plugin-enable", KEY)
        DropModule.loadDropModule(DropFileList as ArrayList<File>)
        ConfigModule.loadConfigModule(ConfigFileList as ArrayList<File>)
        RedeemModule.loadConfigModule(RedeemFileList as ArrayList<File>)
        "def main = { print 'Kether模块已加载' }".toKetherScript().runActions {
            this.sender = adaptCommandSender(sender!!)
        }
        ToolsUtil.debug("Debug模式已开启!")
    }
    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("plugin-disable", KEY)
    }
}