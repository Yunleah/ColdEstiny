package me.yunleah.plugin.coldestiny.internal

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

object ColdEstinyLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
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
        ToolsUtil.debug("Debug模式已开启!")
    }
    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("plugin-disable", KEY)
    }
}