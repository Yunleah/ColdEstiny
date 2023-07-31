package me.yunleah.plugin.coldestiny.internal.hook.worldguard

import org.bukkit.OfflinePlayer


/**
 * WorldGuard附属领地挂钩
 */
abstract class WorldGuardHooker {
    /**
     * 获取玩家所在领地名
     *
     * @param player 待操作玩家
     */
    abstract fun getLocation(player: OfflinePlayer): String?
}