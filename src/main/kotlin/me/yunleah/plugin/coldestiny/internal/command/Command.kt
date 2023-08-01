package me.yunleah.plugin.coldestiny.internal.command

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.command.subcommand.Create
import me.yunleah.plugin.coldestiny.internal.command.subcommand.Dev
import me.yunleah.plugin.coldestiny.internal.command.subcommand.Reload
import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.pluginVersion
import taboolib.expansion.createHelper
import taboolib.module.kether.printKetherErrorMessage
import taboolib.platform.BukkitAdapter
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.sendLang


@CommandHeader(
    name = "ColdEstiny",
    aliases = ["ce", "death"],
    description = "ColdEstiny Main Command"
)
object Command {
    val bukkitAdapter = BukkitAdapter()
    @CommandBody
    val main = mainCommand { createHelper() }
    @CommandBody
    val help = subCommand { createHelper() }
    @CommandBody
    val reload = Reload.reload
    @CommandBody
    val version = subCommand {
        execute<CommandSender> { sender, _, _ ->
            val pluginVersion = pluginVersion
            sender.sendLang("Command-Version", pluginVersion)
        }
    }
    @CommandBody
    val keep = subCommand {
        execute<CommandSender> { sender, _, _ ->
            val worldList = Bukkit.getWorlds()
            worldList.forEach {world ->
                world.setGameRule(GameRule.KEEP_INVENTORY, true)
            }
            sender.sendLang("Command-Keep")
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
                        sender.sendMessage(" §3§l‹ ›§r §bResult: §f$it")
                    }
                } catch (e: Exception) {
                    e.printKetherErrorMessage()
                }
            }
        }
    }
    @CommandBody
    val dev = Dev.dev
    @CommandBody
    val create = Create.create
}