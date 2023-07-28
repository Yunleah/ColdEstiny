package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getFileKey
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.util.MathUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import taboolib.platform.util.hasLore
import taboolib.platform.util.isNotAir
import taboolib.platform.util.sendLang
import java.io.File

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

    fun preDropModule(event: PlayerDeathEvent, drop: File): Pair<MutableList<Int>, Int?> {

        val itemSlotPair = mutableListOf<Pair<Int, ItemStack>>()
        for (i in 0 until event.entity.inventory.size) {
            val itemStack = event.entity.inventory.getItem(i)
            if (itemStack.isNotAir()) {
                itemSlotPair.add(i to itemStack)
            }
        }
        return mainDropModule(event, itemSlotPair, drop)
    }

    private fun mainDropModule(event: PlayerDeathEvent,slotPair: MutableList<Pair<Int, ItemStack>>, drop: File): Pair<MutableList<Int>, Int?> {
        debug("玩家${event.entity.player!!.name}Inv物品列表 -> $slotPair")
        val dropPackInfo = getKey(drop, "Options.Drop.Pack.Info")
        debug("dropPackInfo -> $dropPackInfo")
        val packType = getKey(drop, "Options.Drop.Pack.Type")
        debug("packType -> $packType")
        val packProtected = getKey(drop, "Options.Drop.Pack.Protected")
        debug("packProtected -> $packProtected")
        val dropExpInfo = getKey(drop, "Options.Drop.Exp.Info")
        debug("dropExpInfo -> $dropExpInfo")
        val itemEnable = getKey(drop, "Options.Item.Enable").toString()
        debug("ItemEnable -> $itemEnable")

        val infoPack = MathUtil.getPackMath(dropPackInfo!!, slotPair, packType!!, packProtected!!)
        val infoExp = MathUtil.getExpMath(dropExpInfo!!, event)
        debug("infoPack -> $infoPack")
        debug("infoExp -> $infoExp")
        debug("ItemEnable -> $itemEnable")
        return postDropModule(infoPack, infoExp, itemEnable.toBoolean(), getKey(drop, "Options.Item.Type"), getKey(drop, "Options.Item.Info"))
    }
    private fun postDropModule(packDrop:  MutableList<Pair<Int, ItemStack>>?, expDrop:  Int?, enable: Boolean, type: String?, info: String?): Pair<MutableList<Int>, Int?> {
        debug("<->postDropModule 运行...")
        debug("infoPack -> $packDrop")
        debug("infoExp -> $expDrop")
        debug("ItemEnable -> $enable")
        //lore
        //nbt
        if (enable) {
            debug("物品lore/nbt判断已开启！")
            debug("判断模式 -> $type")
            debug("判断的nbt/lore -> $info")
            when (type) {
                "nbt" -> {
                    packDrop?.removeIf {
                        it.second.getItemTag().containsKey("ColdEstiny")
                    }
                }
                "lore" -> {
                    packDrop?.removeIf {
                        it.second.hasLore(info)
                    }
                }
            }
        }
        return packDrop!!.map { it.first }.toMutableList() to expDrop
    }
}














































