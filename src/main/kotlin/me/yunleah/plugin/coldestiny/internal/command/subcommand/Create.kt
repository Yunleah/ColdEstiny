package me.yunleah.plugin.coldestiny.internal.command.subcommand

import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand

object Create {
    val create = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendMessage("11111")
        }
    }
}