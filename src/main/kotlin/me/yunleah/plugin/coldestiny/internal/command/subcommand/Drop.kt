package me.yunleah.plugin.coldestiny.internal.command.subcommand

import me.yunleah.plugin.coldestiny.internal.manager.DropManager
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit

object Drop {
    val runDrop = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                runDrop(sender)
            }
        }
    }

    private fun runDrop(player: Player) {
        DropManager.runDrop(null, player, false)
    }
}