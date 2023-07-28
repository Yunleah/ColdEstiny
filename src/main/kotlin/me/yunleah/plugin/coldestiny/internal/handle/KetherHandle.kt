package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.internal.manager.GetManager
import me.yunleah.plugin.coldestiny.util.KetherUtil
import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.kether.printKetherErrorMessage
import java.io.File

object KetherHandle {
    fun runKetherHandle(config: File, event: PlayerDeathEvent, type: String): Boolean {
        var result = ""
        try {
            ToolsUtil.debug("Run $type Kether...")
            val script = GetManager.getKey(config, "Options.Action.$type.Script")
            KetherUtil.stringUtil(script!!).toKetherScript().runActions {
                this.sender = adaptCommandSender(event.entity.player!!)
            }.thenAccept {
                result = it as String
                ToolsUtil.debug(" §5§l‹ ›§r §7Result: §f$it")
            }
        } catch (e: Exception) {
            e.printKetherErrorMessage()
        }
        return when(result) {
            "true" -> { true }

            "false" -> { false }
            else -> { true }
        }
    }
}