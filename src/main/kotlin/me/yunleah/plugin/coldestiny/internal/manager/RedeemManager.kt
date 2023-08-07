package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.module.KetherModule.runRedeem
import me.yunleah.plugin.coldestiny.util.ItemUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.startExecutor
import taboolib.common.platform.function.submit
import taboolib.common5.cint
import taboolib.common5.util.replace
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName
import taboolib.module.ui.openMenu
import taboolib.platform.util.sendLang
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RedeemManager {
    val redeemMap = mutableMapOf<String, MutableList<Pair<String, List<ItemStack>>>>()

    fun addRedeem(player: Player, dropItems: List<ItemStack>, coe: Int, timed: Int) {
        val filteredItems = dropItems.filter { item ->
            item.getItemTag().containsKey(ColdEstiny.setting.getString("Options.Redeem.Price.NBTKey"))
        }
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒")
        val key = currentDateTime.format(formatter)
        val redeems = redeemMap.getOrPut(player.name) { mutableListOf() }
        if (redeems.size < coe) {
            redeems.add(key to filteredItems)
        } else {
            ItemUtil.dropItems(filteredItems, player.location)
            return
        }
        submit(delay = timed.toLong()) {
            redeems.removeIf { it.first == key }
        }
    }



    @SubscribeEvent
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        if (event.message == "/redeem") {
            event.isCancelled = true
            val player = event.player
            val redeems = redeemMap.getOrPut(player.name) { mutableListOf() }
            val inventory = Bukkit.createInventory(player, 9 * 6, ColdEstiny.setting.getString("Option.RedeemTitle") ?: "")
            if (redeems.isEmpty()) {
                player.sendLang("RedeemError")
                return
            }
            redeems.forEachIndexed { index, pair ->
                val item = ItemStack(Material.CHEST)
                val meta = item.itemMeta
                meta?.setDisplayName(pair.first)
                meta?.lore = listOf(ColdEstiny.setting.getString("Options.Redeem.Lore")?.replace("{玩家}", player.name)?.replace("{时间}", pair.first))
                item.itemMeta = meta
                inventory.setItem(index, item)
            }
            event.player.openInventory(inventory)
        }
    }

    @SubscribeEvent
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val inventory = event.inventory
        val clickedItem = event.currentItem
        if (inventory.holder == player && event.view.title == (ColdEstiny.setting.getString("Option.RedeemTitle") ?: "")) {
            event.isCancelled = true
            if (clickedItem != null) {
                val name = clickedItem.itemMeta?.displayName ?: return
                val itemList = redeemMap[player.name]?.firstOrNull { it.first == name }?.second ?: return
                val inv = Bukkit.createInventory(null, 9 * 6, ColdEstiny.setting.getString("Option.RedeemTitle") ?: "")
                itemList.forEachIndexed { index, itemStack ->
                    inv.setItem(index, itemStack)
                }
                player.openInventory(inv)
            }
        }
        if ((inventory.holder == null && event.view.title == (ColdEstiny.setting.getString("Option.RedeemTitle") ?: ""))) {
            event.isCancelled = true
            if (clickedItem != null) {
                val nbtKey = ColdEstiny.setting.getString("Options.Redeem.Price.NBTKey") ?: return
                val v = clickedItem.getItemTag()[nbtKey].cint
                val script = ColdEstiny.setting.getString("Options.Redeem.runAction") ?: return
                val result = script.runRedeem(player, v)
                if (result) {
                    inventory.setItem(event.slot, ItemStack(Material.AIR))
                    if (player.inventory.firstEmpty() == -1) {
                        player.location.world?.dropItem(player.location, clickedItem)
                    } else {
                        player.inventory.setItem(player.inventory.firstEmpty(), clickedItem)
                    }
                    redeemMap[player.name]?.firstOrNull { it.first == clickedItem.itemMeta?.displayName }?.second?.toMutableList()?.removeIf { it == clickedItem }
                }
            }
        }
    }




}