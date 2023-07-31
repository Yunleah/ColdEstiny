package me.yunleah.plugin.coldestiny.internal.hook.griefdefender

import org.bukkit.OfflinePlayer

/**
 * GriefDefender附属领地挂钩
 */
abstract class GriefDefenderHooker {
    /**
     * 获取玩家所在领地名
     *
     * @param player 待操作玩家
     */
    abstract fun getLocation(player: OfflinePlayer): String?
}