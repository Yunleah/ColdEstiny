package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getFileKey
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.util.MathUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import net.minecraft.world.item.Items
import org.bukkit.entity.Item
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common5.cint
import taboolib.module.kether.inferType
import taboolib.module.lang.sendError
import taboolib.module.lang.sendLang
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import java.io.File
import kotlin.random.Random

object DropModule {
    var verify: Boolean = false
    fun loadDropModule(dropFile:  ArrayList<File>) {
        val dropFileList = dropFile.filter { it.name.endsWith("Drop.yml") }
        console().sendLang("plugin-loadModule", KEY, dropFileList.size ,"掉落组")
    }

    fun dropFileModule(configPath: String, dropFile: ArrayList<File>): List<File>? {
        val config = File(configPath)
        val dropEnable = getKey(config, "Options.Drop.Enable").toBoolean()

        return if (dropEnable) {
            val dropKey = getKey(config, "Options.Drop.Bind")
            val fileKey = getFileKey(dropFile.filter { it.name.endsWith("Drop.yml")} as ArrayList<File>,"Options.Key")
            fileKey.filter { it.second == dropKey }.map { it.first }
        } else {
            null
        }
    }

    fun preDropModule(event: PlayerDeathEvent, drop: File) {

        val itemSlotPair = mutableListOf<Pair<Int, ItemStack>>()
        for (i in 0 until event.entity.inventory.size) {
            val itemStack = event.entity.inventory.getItem(i)
            if (itemStack.isNotAir()) {
                itemSlotPair.add(i to itemStack)
            }
        }
        mainDropModule(event, itemSlotPair, drop)
    }

    private fun mainDropModule(event: PlayerDeathEvent,slotPair: MutableList<Pair<Int, ItemStack>>, drop: File): Unit? {
        ToolsUtil.debug("玩家${event.entity.player!!.name}Inv物品列表 -> $slotPair")
        val dropPackInfo = getKey(drop, "Options.Drop.Pack.Info")!!.map { it.toString() } as ArrayList<String>
        val dropExpInfo = getKey(drop, "Options.Drop.Exp.Info")!!.map { it.toString() } as ArrayList<String>
        val loreItemInfo = getKey(drop, "Options.Lore").toString()
        val infoPackInt = MathUtil.getMath(dropPackInfo, slotPair)
            ?: return null


        return null
    }
}














































