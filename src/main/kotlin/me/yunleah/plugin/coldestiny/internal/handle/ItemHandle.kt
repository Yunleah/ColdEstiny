package me.yunleah.plugin.coldestiny.internal.handle

import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object ItemHandle {
    fun preItemHandle(file: File, player: Player) {
        console().sendLang("抱歉...赎回物品正在筹备中...")
    }
}