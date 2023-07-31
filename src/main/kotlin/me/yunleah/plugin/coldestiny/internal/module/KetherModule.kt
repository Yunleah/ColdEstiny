package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common5.cint
import taboolib.module.kether.printKetherErrorMessage
import taboolib.platform.util.killer

object KetherModule {
    fun String.runKether(event: PlayerDeathEvent): Boolean {
        var result = false
        try {
            val script = if (this.startsWith("def")) {
                this
            } else {
                "def main = { $this }"
            }
            script.toKetherScript().runActions {
                this.sender = adaptCommandSender(event.entity)
                if (sender is Player) {
                    set("player", sender as Player)
                    set("hand", (sender as Player).inventory.itemInMainHand)
                    set("killer", event.entity.killer)
                    set("killers", event.killer)
                    set("cause", ToolsUtil.causeDeath(event))
                    set("type", ToolsUtil.type(event))
                    set("level", event.entity.player!!.level)
                }
            }.thenAccept {
                if (it != false) result = true
            }
        } catch (e: Exception) {
            e.printKetherErrorMessage()
        }
        return result
    }
    fun String.runKether(event: PlayerDeathEvent, i: Int): Int {
        var result = i
        try {
            val script = if (this.startsWith("def")) {
                this
            } else {
                "def main = { $this }"
            }
            script.toKetherScript().runActions {
                this.sender = adaptCommandSender(event.entity)
                if (sender is Player) {
                    set("player", sender as Player)
                    set("hand", (sender as Player).inventory.itemInMainHand)
                    set("level", event.entity.player!!.level)
                }
            }.thenAccept {
                result = it.cint
            }
        } catch (e: Exception) {
            e.printKetherErrorMessage()
        }
        return result
    }
}