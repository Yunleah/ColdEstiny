package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.inventory.ItemStack
import taboolib.common.util.random
import taboolib.common5.cint
import taboolib.module.kether.isInt
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random


object MathUtil {
    fun getPackMath(text: String, slot: MutableList<Pair<Int, ItemStack>>, type: String, protected: String): MutableList<Pair<Int, ItemStack>>? {
        val protectedList = protected.split(",").map { it.trim().toInt() }.toIntArray()
        slot.shuffle()
        debug("when Type...")
        debug("type -> $type")
        when (type) {
            //百分比
            "per" -> {
                debug("DropPackType -> $type")
                if (!text.endsWith("%")) { return null }
                debug("Info合法 -> $text...")
                val per = text.removeSuffix("%").toDouble() / 100.0
                debug("换算后per -> $per")
                val slotSize = slot.size
                debug("玩家背包物品格数量 -> $slotSize")
                val slotOrder = (slotSize * per).toInt()
                debug("物品格掉落数量 -> $slotOrder")
                val result = slot.filterIndexed { index, _ -> index <= slotOrder }.toMutableList()
                result.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return result
            }
            //个数
            "order" -> {
                debug("DropPackType -> $type")
                debug("Info合法 -> $text...")
                val order = text.toInt()
                debug("指定数量 -> $order")
                val result = slot.filterIndexed { index, _ -> index <= order }.toMutableList()
                result.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return result
            }
            //范围
            "range" -> {
                debug("DropPackType -> $type")
                if ("~" !in text) { return null }
                debug("Info合法 -> $text...")
                val rangePre = text.split("~").first().toInt()
                debug("PreInt -> $rangePre")
                val rangePost = text.split("~").last().toInt()
                debug("Post -> $rangePost")
                val range = ThreadLocalRandom.current().nextInt(rangePre, rangePost + 1)
                val result = slot.filterIndexed { index, _ -> index <= range }.toMutableList()
                result.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return result
            }
            //指定物品格
            "ap" -> {
                debug("DropPackType -> $type")
                val apList = text.split(",").map { it.trim().toInt() }.toIntArray()
                val result = slot.filter { it.first in apList }.toMutableList()
                result.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return  result
            }
        }
        return null
    }

    fun getExpMath(text: String, type: String) {

    }
}