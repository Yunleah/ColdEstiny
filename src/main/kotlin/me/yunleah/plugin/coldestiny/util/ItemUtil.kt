package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.util.ItemUtils.parseValue
import me.yunleah.plugin.coldestiny.util.ItemUtils.toMap
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagType
import taboolib.platform.util.giveItem

/**
 * 物品相关工具类
 */
object ItemUtils {
    val invalidNBT by lazy { arrayListOf("Enchantments","VARIABLES_DATA","ench","Damage","HideFlags","Unbreakable", "CustomModelData") }

    /**
     * ItemTagData 解析
     *
     * @return 解析结果
     */
    @JvmStatic
    fun ItemTagData.parseValue(): Any {
        return when (this.type) {
            ItemTagType.BYTE -> "(Byte) ${this.asString()}"
            ItemTagType.SHORT ->  "(Short) ${this.asString()}"
            ItemTagType.INT ->  "(Int) ${this.asString()}"
            ItemTagType.LONG ->  "(Long) ${this.asString()}"
            ItemTagType.FLOAT ->  "(Float) ${this.asString()}"
            ItemTagType.DOUBLE ->  "(Double) ${this.asString()}"
            ItemTagType.STRING ->  this.asString()
            ItemTagType.BYTE_ARRAY -> this.asByteArray().toList().map { "(Byte) $it" }
            ItemTagType.INT_ARRAY -> this.asIntArray().toList().map { "(Int) $it" }
            ItemTagType.COMPOUND -> this.asCompound().toMap()
            ItemTagType.LIST -> this.asList().map { it.parseValue() }
            else -> this.asString()
        }
    }


    /**
     * 将给定的 ItemTag 对象转换为一个 HashMap<String, Any> 对象，并返回结果。
     * 在转换过程中忽略 invalidNBT 中指定的标签。
     */
    fun toMap(itemTag: ItemTag, invalidNBT: List<String>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        for ((key, value) in itemTag) {
            if (!invalidNBT.contains(key)) {
                hashMap[key] = value.parseValue()
            }
        }
        return hashMap
    }

}