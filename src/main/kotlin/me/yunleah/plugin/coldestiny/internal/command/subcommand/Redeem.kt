package me.yunleah.plugin.coldestiny.internal.command.subcommand

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.event.PluginReloadEvent
import me.yunleah.plugin.coldestiny.internal.handle.ItemHandle
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.RedeemFileList
import me.yunleah.plugin.coldestiny.internal.manager.RedeemManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang

object Redeem {
    val redeem = subCommand {
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<Player> { sender, context, argument ->
                val playerName = context.argument(0)
                val redeemTime =context.argument(1)
                val targetName = Bukkit.getPlayerExact(playerName)
                if (targetName == null) {
                    sender.sendLang("plugin-format", KEY, "玩家 $playerName 不存在！")
                    return@execute
                }
                val redeemI = RedeemManager.getRedeemList(targetName).find { it.second == redeemTime }
                if (redeemI == null) {
                    sender.sendLang("plugin-format", KEY, "赎回YML $redeemTime 不存在或不属于该玩家！")
                    return@execute
                }
                ItemHandle.preItemHandle(RedeemFileList.find { it.name == redeemTime }!!, targetName)
            }
        }
        dynamic {
            suggestion<Player>(uncheck = true) { sender, _ ->
                RedeemManager.getRedeemList(sender).map { it.second }
            }
        }
    }
}