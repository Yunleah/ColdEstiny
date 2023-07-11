package me.yunleah.plugin.coldestiny.util

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import taboolib.common.platform.function.console
import taboolib.common.util.asList
import taboolib.common5.cint
import taboolib.module.configuration.util.asMap
import taboolib.module.kether.isInt
import taboolib.module.lang.sendError
import java.io.Serializable
import kotlin.random.Random

object MathUtil {
    fun getMath(text: ArrayList<String>, slot: MutableList<Pair<Int, ItemStack>>): MutableList<Pair<Int, ItemStack>>? {
        val result = mutableListOf<Pair<Int, ItemStack>>()
        val percentList = text.filter { it.endsWith("%") }
        val protectedList = text.filter { it.startsWith("</>") && it.endsWith("</>") }
        val allList = text.filter { it == "ALL" }
        val nullList = text.filter { it == "NULL" }
        val rangeList = text.filter { it.startsWith("<~>") && it.endsWith("<~>") }
        val numberList = text.filter { it.startsWith("<=>") && it.endsWith("<=>") }
        val orderList = text.filter { it.startsWith("<->") && it.endsWith("<->") }

        when {
            allList.isNotEmpty() && nullList.isNotEmpty() -> {
                console().sendError("配置出现错误！ -> $text | 同时包含ALL&NULL！")
                return null
            }
            allList.isNotEmpty() -> {
                result.addAll(slot)
            }
            nullList.isNotEmpty() -> {
                return null
            }
            rangeList.isNotEmpty() -> {
                val rangeLast = rangeList.first().split("<~>").getOrNull(1)?.split(",")?.mapNotNull { it.toIntOrNull() }
                val rand = Random.nextInt(rangeLast?.firstOrNull() ?: 0, rangeLast?.lastOrNull() ?: 0)
                result.addAll(slot.take(rand))
            }
            numberList.isNotEmpty() -> {
                val numberLast = numberList.first().split("<=>").getOrNull(1)?.split(",")?.mapNotNull { it.toIntOrNull() }
                val number = numberLast?.firstOrNull() ?: 0
                result.addAll(slot.take(number.coerceAtMost(slot.size)))
            }
            orderList.isNotEmpty() -> {
                val orderLast = orderList.first().split("<->").getOrNull(1)?.split(",")?.mapNotNull { it.toIntOrNull() }
                result.addAll(slot.filter { pair -> !orderLast.isNullOrEmpty() && !orderLast.contains(pair.first) })
                val remainingSlots = slot.filter { pair -> orderLast.isNullOrEmpty() || orderLast.contains(pair.first) }
                val orderedSlots = mutableListOf<Pair<Int, ItemStack>>()
                orderLast?.forEach { index ->
                    remainingSlots.find { it.first == index }?.let { orderedSlots.add(it) }
                }
                result.addAll(orderedSlots)
            }
            percentList.isNotEmpty() -> {
                val percent = percentList.first().removeSuffix("%").toInt()
                val size = slot.size * (percent / 100f)
                result.addAll(slot.take(size.toInt()))
            }
        }

        val protectedSlots = protectedList.firstOrNull()?.split("</>")?.getOrNull(1)?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
        result.removeAll { pair -> protectedSlots.contains(pair.first) }
        return result
    }
}