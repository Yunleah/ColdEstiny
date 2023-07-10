package me.yunleah.plugin.coldestiny.internal

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console

object ColdEstinyLoader {

    @Awake(LifeCycle.LOAD)
    fun load() {

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
    }
    @Awake(LifeCycle.DISABLE)
    fun disable() {

    }
}