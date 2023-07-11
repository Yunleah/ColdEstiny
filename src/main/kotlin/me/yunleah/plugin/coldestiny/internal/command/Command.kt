package me.yunleah.plugin.coldestiny.internal.command

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.command.subcommand.Reload
import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.expansion.createHelper
import taboolib.module.kether.printKetherErrorMessage
import taboolib.platform.util.sendLang

@CommandHeader(
    name = "ColdEstiny",
    aliases = ["ce"],
    description = "ColdEstiny Main Command"
)
object Command {
    @CommandBody
    val main = mainCommand { createHelper() }
    @CommandBody
    val help = subCommand { createHelper() }
    @CommandBody
    val world = subCommand {
        execute<CommandSender> { sender, _, _ ->
            val worldList = plugin.server.worlds
            sender.sendLang("plugin-format", KEY, "${worldList.map { it.name }}")
            sender.sendLang("plugin-format", KEY, "当前Server共有 ${worldList.size} 个世界")
        }
    }
    @CommandBody
    val eval = subCommand {
        dynamic {
            execute<CommandSender> { sender, _, content ->
                try {
                    val script = if (content.startsWith("def")) {
                        content
                    } else {
                        "def main = { $content }"
                    }

                    script.toKetherScript().runActions {
                        this.sender = adaptCommandSender(sender)
                        if (sender is Player) {
                            set("player", sender)
                            set("hand", sender.equipment?.itemInMainHand)
                        }
                    }.thenAccept {
                        sender.sendMessage(" §5§l‹ ›§r §7Result: §f$it")
                    }
                } catch (e: Exception) {
                    e.printKetherErrorMessage()
                }
            }
        }
    }
    @CommandBody
    val reload = Reload.reload
}