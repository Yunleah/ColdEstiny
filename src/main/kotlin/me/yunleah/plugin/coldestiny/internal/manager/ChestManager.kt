package me.yunleah.plugin.coldestiny.internal.manager

import com.comphenix.protocol.PacketType
import me.yunleah.plugin.coldestiny.internal.hook.protocollib.impl.ProtocolLibHookerImpl
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import me.yunleah.plugin.coldestiny.ColdEstiny.bukkitScheduler
import me.yunleah.plugin.coldestiny.ColdEstiny.plugin
import me.yunleah.plugin.coldestiny.internal.module.RelicsModule
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common.util.sync
import taboolib.common5.cint
import taboolib.common5.clong
import taboolib.platform.util.sendLang
import java.lang.reflect.InvocationTargetException

object ChestManager {
    fun createChest(dropItem: List<ItemStack>, location: Location, player: Player, ownerVisible: Boolean, ownerAvailable: Boolean, timed: Int, custom: String, key: String, title: String) {
        // 发包
        val world = location.world
        val block = world!!.getBlockAt(location)
        block.type = Material.CHEST
        val chest = block.state as Chest
        chest.customName = title
        if (custom != "") {
            val dataContainer = chest.persistentDataContainer
            val customKey = NamespacedKey(plugin, custom)
            dataContainer.set(customKey, PersistentDataType.STRING, custom)
            chest.update()
        }
        val inventory = chest.blockInventory
        for (item in dropItem) {
            inventory.addItem(item)
        }
        val protocolManager = ProtocolLibHookerImpl().getManager()
        val packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE)
        packet.blockPositionModifier.write(0, BlockPosition(location.toVector()))
        packet.blockData.write(0, WrappedBlockData.createData(Material.CHEST))
        // 是否仅自己可见
        try {
            if (ownerVisible) {
                protocolManager.sendServerPacket(player, packet)
            } else {
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    protocolManager.sendServerPacket(onlinePlayer, packet)
                }
            }
        } catch (error: InvocationTargetException) {
            throw RuntimeException("Cannot send packet $packet", error)
        }

        // 是否仅自己可用
        if (ownerAvailable) {
            Bukkit.getPluginManager().registerEvents(object : Listener {
                @EventHandler
                fun onPlayerInteract(event: PlayerInteractEvent) {
                    if (event.clickedBlock == block && event.player != player) {
                        event.isCancelled = true
                        event.player.sendLang("ClickChestError", player.name)
                    }
                }
            }, plugin)
        }

        // 是否时间限制
        if (timed != 0) {
            submit(delay = timed.clong) {
                removeChest(location, player, ownerVisible)
                val old = RelicsModule.relicsMap[key]
                RelicsModule.relicsMap[key] = old!! - 1
            }
        }

       // 箱子空了就删掉 不然留着过年吗
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun onInventoryClose(event: InventoryCloseEvent) {
                if (event.inventory.holder is Chest) {
                    val ch = event.inventory.holder as Chest
                    if (ch.block == block && event.inventory.contents.all { it == null }) {
                        removeChest(location, player, ownerVisible)
                    }
                }
            }
        }, plugin)
    }
    private fun removeChest(location: Location, player: Player, ownerVisible: Boolean) {
        val world = location.world
        val block = world!!.getBlockAt(location)
        val chest = block.state as Chest
        val inventory = chest.blockInventory
        inventory.clear()
        bukkitScheduler.runTask(plugin, Runnable {
            synchronized(block) {
                block.type = Material.AIR
            }
        })
        val protocolManager = ProtocolLibHookerImpl().getManager()
        val packet = protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE)
        packet.blockPositionModifier.write(0, BlockPosition(location.toVector()))
        packet.blockData.write(0, WrappedBlockData.createData(Material.AIR))
        try {
            if (ownerVisible) {
                protocolManager.sendServerPacket(player, packet)
            } else {
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                    protocolManager.sendServerPacket(onlinePlayer, packet)
                }
            }
        } catch (error: InvocationTargetException) {
            throw RuntimeException("Cannot send packet $packet", error)
        }
    }
}