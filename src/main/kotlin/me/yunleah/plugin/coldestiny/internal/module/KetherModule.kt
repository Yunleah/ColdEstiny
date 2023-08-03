package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common5.cint
import taboolib.common5.util.replace
import taboolib.module.kether.printKetherErrorMessage
import taboolib.module.nms.getI18nName
import taboolib.platform.util.killer

object KetherModule {
    fun String.runKether(event: PlayerDeathEvent? = null, player: Player? = null,dropInv: ArrayList<ItemStack>? = null, location: Location? = null): Boolean {
        var result = false
        try {
            val script = if (this.startsWith("def")) {
                this
            } else {
                "def main = { $this }"
            }
            val entity = event?.entity ?: player
            script.toKetherScript().runActions {
                this.sender = adaptCommandSender(entity!!)
                set("player", entity)
                set("hand", entity.inventory.itemInMainHand)
                if (event != null) {
                    set("killer", event.entity.killer)
                    set("killers", event.entity.killer)
                    set("cause", ToolsUtil.causeDeath(event))
                    set("type", ToolsUtil.type(event))
                }
                set("level", entity.player!!.level)
                set("location", entity.location)
                if (dropInv != null) {
                    set("dropInv", dropInv)
                    val evalInv: List<String> = dropInv.map { drop ->
                        val display = drop.getI18nName()
                        val amount = drop.amount
                        "$display *$amount"
                    }
                    set("evalInv", evalInv)
                }
                set("loc", location)
            }.thenAccept {
                if (it != false) result = true
            }
        } catch (e: Exception) {
            e.printKetherErrorMessage()
        }
        return result
    }
}