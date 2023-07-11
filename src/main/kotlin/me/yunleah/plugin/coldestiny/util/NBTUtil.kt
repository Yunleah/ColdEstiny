package me.yunleah.plugin.coldestiny.util

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag


object NBTUtil {
    fun hasInfoTag(itemStack: ItemStack, info:String): Boolean {
        val itemTag = itemStack.getItemTag()
        val coldEstiny = itemTag["ColdEstiny"]?.asString()
        return coldEstiny == info
    }

    fun addInfoTagToItemInHand(player: Player, info: String) {
        val itemInHand = player.inventory.itemInMainHand
        val itemNBT = itemInHand.getItemTag()
        itemNBT.put("ColdEstiny", info)
        itemInHand.setItemTag(itemNBT)
    }
}