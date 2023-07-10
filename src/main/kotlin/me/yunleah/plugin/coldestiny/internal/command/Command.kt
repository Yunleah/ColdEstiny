package me.yunleah.plugin.coldestiny.internal.command

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.command.subcommand.Reload
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import me.yunleah.plugin.coldestiny.internal.manager.GetManager
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule.worldConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.command.CommandSender
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.info
import taboolib.expansion.createHelper
import taboolib.platform.util.sendLang
import java.io.File

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
    val reload = Reload.reload
}