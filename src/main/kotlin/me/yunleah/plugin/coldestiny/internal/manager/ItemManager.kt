package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.util.ItemUtils
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
import java.io.File
import java.util.*


/**
 * 将一个物品列表存储到指定文件中
 *
 * @param items 物品列表
 */
fun saveItemsToFile(items: List<ItemStack>, file: File): Int {

    // 创建 YamlConfiguration 实例
    val config = YamlConfiguration()
    var id = 0

    // 遍历物品列表
    for (item in items) {
        id += 1
        ToolsUtil.debug("item->File: $item")
        // 检查物品是否为空气
        if (item.type != Material.AIR) {
            // 创建物品配置节点
            val configSection = config.createSection(id.toString())
            // 设置物品材质
            configSection.set("material", item.type.toString())
            // 设置数量
            configSection.set("amount", item.amount)
            // 设置子 ID / 损伤值
            if (item.durability > 0) {
                configSection.set("damage", item.durability)
                // 如果物品有 ItemMeta
                if (item.hasItemMeta()) {
                    // 获取 ItemMeta 和 NBT
                    val itemMeta = item.itemMeta
                    val itemNBT = item.getItemTag()
                    // 获取显示信息
                    val display = itemNBT["display"]
                    itemNBT.remove("display")
                    // 设置 CustomModelData
                    if (itemMeta?.hasCustomModelData() == true) {
                        configSection.set("custommodeldata", itemMeta.customModelData)
                    }
                    // 设置物品名称
                    if (itemMeta?.hasDisplayName() == true) {
                        configSection.set("name", itemMeta.displayName)
                    }
                    // 设置物品 Lore
                    if (itemMeta?.hasLore() == true) {
                        configSection.set("lore", itemMeta.lore)
                    }
                    // 设置物品是否无法破坏
                    if (itemMeta?.isUnbreakable == true) {
                        configSection.set("unbreakable", itemMeta.isUnbreakable)
                    }
                    // 设置物品附魔
                    if (itemMeta?.hasEnchants() == true) {
                        val enchantSection = configSection.createSection("enchantments")
                        for ((enchant, level) in itemMeta.enchants) {
                            enchantSection.set(enchant.name, level)
                        }
                    }
                    // 设置 ItemFlags
                    itemMeta?.itemFlags?.let{
                        if (it.isNotEmpty()) {
                            configSection.set("hideflags", it.map { flag -> flag.name })
                        }
                    }
                    // 设置物品颜色
                    display?.asCompound()?.let {
                        it["color"]?.asInt()?.let { color ->
                            configSection.set("color", color.toString(16).uppercase())
                        }
                    }
                    // 设置物品 NBT
                    if (!itemNBT.isEmpty()) {
                        configSection.set("nbt", ItemUtils.toMap(itemNBT, ItemUtils.invalidNBT))
                    }
                }
            }
        }
    }

    // 保存配置文件
    config.save(file)
    return id
}
