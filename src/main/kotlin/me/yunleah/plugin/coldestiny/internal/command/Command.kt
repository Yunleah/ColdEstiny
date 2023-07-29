package me.yunleah.plugin.coldestiny.internal.command

import me.yunleah.plugin.coldestiny.internal.command.subcommand.Reload
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper
import taboolib.platform.BukkitAdapter


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
}