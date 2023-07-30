package me.yunleah.plugin.coldestiny.internal.hook.residence.impl

import com.bekvon.bukkit.residence.Residence
import com.bekvon.bukkit.residence.protection.ClaimedResidence
import me.yunleah.plugin.coldestiny.internal.hook.residence.ResidenceHooker
import org.bukkit.OfflinePlayer


/**
 * Residence附属经济挂钩
 *
 * @constructor 启用Residence附属经济挂钩
 */
class ResidenceHookerImpl : ResidenceHooker() {
    override fun getLocation(player: OfflinePlayer): String? {
        val resManager = Residence.getInstance().residenceManager
        val res: ClaimedResidence? = resManager.getByLoc(player.player!!.location)
        return res?.name
    }
}