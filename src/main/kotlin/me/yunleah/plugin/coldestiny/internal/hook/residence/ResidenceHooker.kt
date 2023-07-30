package me.yunleah.plugin.coldestiny.internal.hook.residence

import org.bukkit.OfflinePlayer

/**
 * Residence附属经济挂钩
 */
abstract class ResidenceHooker {
    /**
     * 获取玩家所在领地名
     *
     * @param player 待操作玩家
     */
    abstract fun getLocation(player: OfflinePlayer): String?
}