package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.kether.printKetherErrorMessage

object KetherModule {
    fun String.runKether(entity: Entity): Boolean {
        var result = false
        try {
            val script = if (this.startsWith("def")) {
                this
            } else {
                "def main = { $this }"
            }
            script.toKetherScript().runActions {
                this.sender = adaptCommandSender(entity)
                if (sender is Player) {
                    set("player", sender as Player)
                    set("hand", (sender as Player).inventory.itemInMainHand)
                }
            }.thenAccept {
                if (it != false) result = true
            }
        } catch (e: Exception) {
            e.printKetherErrorMessage()
        }
        return result
    }
}