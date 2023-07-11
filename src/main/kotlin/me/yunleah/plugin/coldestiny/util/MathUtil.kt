package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ThreadLocalRandom


object MathUtil {
    fun getPackMath(text: String?, slot: MutableList<Pair<Int, ItemStack>>, type: String, protected: String): MutableList<Pair<Int, ItemStack>>? {
        val protectedList = protected.split(",").map { it.trim().toInt() }.toIntArray()
        slot.shuffle()
        debug("Pack when Type...")
        debug("type -> $type")
        when (type) {
            //百分比
            "per" -> {
                debug("DropPackType -> $type")
                if (!text!!.endsWith("%")) { return null }
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
                val order = text!!.toInt()
                debug("指定数量 -> $order")
                val result = slot.filterIndexed { index, _ -> index <= order }.toMutableList()
                result.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return result
            }
            //范围
            "range" -> {
                debug("DropPackType -> $type")
                if ("~" !in text!!) { return null }
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
                val apList = text!!.split(",").map { it.trim().toInt() }.toIntArray()
                val result = slot.filter { it.first in apList }.toMutableList()
                result.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return  result
            }
            "ALL" -> {
                slot.removeAll { pair: Pair<Int, ItemStack> -> protectedList.contains(pair.first) }
                return slot
            }
            "NULL" -> {
                return mutableListOf()
            }
        }
        return null
    }

    fun getExpMath(text: String?, type: String, event:  PlayerDeathEvent): Int? {
        debug("Exp when Type...")
        debug("type -> $type")
        val exp = event.entity.exp
        debug("玩家经验条占比 -> $exp")
        val expToLevel = event.entity.player!!.expToLevel
        debug("玩家当前等级所需经验 -> $expToLevel")
        val playerExp = (expToLevel * exp).toInt()
        debug("玩家当时实际总经验 -> $playerExp")
        when (type) {
            "per" -> {
                debug("DropExpType -> $type")
                if (!text!!.endsWith("%")) { return null }
                debug("Info合法 -> $text...")
                val per = text.removeSuffix("%").toDouble() / 100.0
                return (playerExp * per).toInt()
            }
            "order" -> {
                debug("DropExpType -> $type")
                debug("Info合法 -> $text...")
                val order = text!!.toInt()
                debug("指定数量 -> $order")
                return if (order >= playerExp) { playerExp } else { order }
            }
            "range" -> {
                debug("DropExpType -> $type")
                if ("~" !in text!!) { return null }
                debug("Info合法 -> $text...")
                val rangePre = text.split("~").first().toInt()
                debug("PreInt -> $rangePre")
                val rangePost = text.split("~").last().toInt()
                debug("Post -> $rangePost")
                val range = ThreadLocalRandom.current().nextInt(rangePre, rangePost + 1)
                return if (range >= playerExp) { playerExp } else { range }
            }
            "ALL" -> {
                return playerExp
            }
            "NULL" -> {
                return 0
            }
        }
        return null
    }
}