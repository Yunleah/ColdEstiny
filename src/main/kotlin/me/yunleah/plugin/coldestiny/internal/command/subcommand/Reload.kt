package me.yunleah.plugin.coldestiny.internal.command.subcommand

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.event.PluginReloadEvent
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang

object Reload {
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reloadCommand(sender)
        }
    }
    private fun reloadCommand(sender: CommandSender) {
        submit(async = true) {
            PluginReloadEvent.call()
            sender.sendLang("command-reload", KEY)
            ToolsUtil.debug("Debug模式已开启!")
        }
    }
}