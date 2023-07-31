package me.yunleah.plugin.coldestiny.internal.hook.griefdefender.impl


import com.griefdefender.api.GriefDefender
import me.yunleah.plugin.coldestiny.internal.hook.griefdefender.GriefDefenderHooker
import org.bukkit.OfflinePlayer

/**
 * GriefDefender附属领地挂钩
 *
 * @constructor 启用GriefDefender附属领地挂钩
 */
class GriefDefenderHookerImpl : GriefDefenderHooker() {
    override fun getLocation(player: OfflinePlayer): String? {
        val claim = GriefDefender.getCore().getClaimAt(player.player!!.location)
        return claim?.displayName
    }
}