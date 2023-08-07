package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.dropFileList
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import me.yunleah.plugin.coldestiny.util.ToolsUtil.parsePercent
import me.yunleah.plugin.coldestiny.util.ToolsUtil.toMaterial
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.cbool
import taboolib.common5.cfloat
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.hasLore
import taboolib.platform.util.isNotAir
import java.io.File
import java.util.concurrent.ThreadLocalRandom

object DropModule {
    fun loadDropFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "drop"))
        console().sendLang("Plugin-LoadFile", dropFileList.size, "Drop", ToolsUtil.timing(startTime))
    }

    fun checkDrop(managerFile: File): File? {
        val dropKey = SectionUtil.getKey(managerFile, "ManagerGroup.BindGroup.DropKey") ?: return null
        val dropFile = dropFileList.filter {
            val key = SectionUtil.getKey(it, "DropGroup.GroupKey")
            return@filter key == dropKey
        } as ArrayList<File>
        return dropFile.first()
    }

    private fun checkInv(player: Player): List<Pair<Int, ItemStack>> {
        return player.inventory.withIndex()
            .filter { (_, itemStack) -> itemStack.isNotAir() }
            .map { (index, itemStack) -> index to itemStack }
    }

    fun checkDropInv(file: File?, player: Player): ArrayList<Int> {
        if (file == null) return arrayListOf()
        val dropEnable = SectionUtil.getKey(file, "DropGroup.Options.Item.Enable").cbool
        if (!dropEnable) return arrayListOf()
        val dropProtectedEnable = SectionUtil.getKey(file, "DropGroup.Options.Item.Protected.Enable").cbool
        val protectedIndo = SectionUtil.getList(file, "DropGroup.Options.Item.Protected.Info")
        val playerInventory = checkInv(player)
        val protectedSlot: ArrayList<Int> = arrayListOf()

        // 处理保护格逻辑
        if (dropProtectedEnable) {
            protectedIndo.forEach { info ->
                val str = info.toString().split("<", ">")
                val filteredStr = str.filter { it.isNotEmpty() }
                val (right, setting,lift) = filteredStr
                if (right == lift) {
                    when (right) {
                        "slot" -> {
                            protectedSlot += setting.split(",").map(String::cint)
                                .filter { playerInventory.map { i -> i.first }.contains(it) }
                        }
                        "material" -> {
                            val material = setting.toMaterial()
                            protectedSlot += playerInventory
                                .filter { (_, item) -> item.type == material }
                                .map { (index, _) -> index }
                        }
                        "lore" -> {
                            protectedSlot += playerInventory
                                .filter { (_, item) -> item.hasLore(setting) }
                                .map { (index, _) -> index }
                        }
                        "nbt" -> {
                            protectedSlot += playerInventory
                                .filter { (_, item) -> item.getItemTag()["ColdEstiny"] == ItemTagData(setting) }
                                .map { (index, _) -> index }
                        }
                    }
                }
            }
            val result = protectedSlot.distinct().sortedDescending()
            protectedSlot.clear()
            protectedSlot.addAll(result)
        }
        debug("获取到的保护格Slot列表 -> $protectedSlot")

        // 处理玩家Inventory
        val newInventory = playerInventory.filterNot { (index, _) -> protectedSlot.contains(index) }
        debug("获得处理后的玩家Slot列表 -> $newInventory")

        // 处理玩家Drop列表
        val dropType = SectionUtil.getKey(file, "DropGroup.Options.Item.Type")
        val setting = SectionUtil.getList(file, "DropGroup.Options.Item.Info")
        val dropSlot: ArrayList<Int> = arrayListOf()
        when (dropType) {
            "percentage" -> {
                // 百分比 -> 50%
                dropSlot += setting.map { it.toString().parsePercent() }
                    .map { percent -> newInventory.shuffled().take((newInventory.size * percent).toInt()) }
                    .flatten().map { it.first }
            }
            "range" -> {
                // 范围 -> 1..2
                dropSlot += setting.flatMap { range ->
                    val (setLeft, setRight) = range.toString().split("..").map { it.cint }
                    val result = ThreadLocalRandom.current().nextInt(setLeft, setRight + 1)
                    newInventory.shuffled().take(result).map { it.first }
                }
            }
            "slot" -> {
                // 指定格 -> 1
                dropSlot += setting.mapNotNull { slotIndex ->
                    newInventory.find { it.first == slotIndex.cint }?.first
                }
            }
            "amount" -> {
                // 指定数
                dropSlot += setting.flatMap { amount ->
                    newInventory.shuffled().take(amount.cint).map { it.first }
                }
            }
            "material" -> {
                // 指定Material
                val material = setting.toString().toMaterial()
                dropSlot += newInventory
                    .filter { (_, item) -> item.type == material }
                    .map { (index, _) -> index }
            }
            "lore" -> {
                // 指定Lore
                dropSlot += newInventory
                    .filter { (_, item) -> item.hasLore(setting.toString()) }
                    .map { (index, _) -> index }
            }
            "nbt" -> {
                // 指定Nbt
                dropSlot += newInventory
                    .filter { (_, item) -> item.getItemTag()["ColdEstiny"] == ItemTagData(setting.toString()) }
                    .map { (index, _) -> index }
            }
            "all" -> {
                // 全掉落
                dropSlot += newInventory.map { it.first }
            }
            "none" -> {
                // 不掉落
                // 宝 你在看什么？
                dropSlot += arrayListOf()
            }
            else -> throw IllegalArgumentException("Invalid drop type")
        }
        val result = dropSlot.distinct().sortedDescending()
        dropSlot.clear()
        dropSlot.addAll(result)
        debug("获取最终玩家掉落Slot列表 -> $dropSlot")
        return dropSlot
    }

    fun checkDropExp(file: File?, player: Player): Int {
        debug("-----Exp-----")
        if (file == null) return 0
        val dropEnable = SectionUtil.getKey(file, "DropGroup.Options.Exp.Enable").cbool
        if (!dropEnable) return 0
        val dropType = SectionUtil.getKey(file, "DropGroup.Options.Exp.Type")
        debug("获取到的type -> $dropType")
        val setting = SectionUtil.getKey(file, "DropGroup.Options.Exp.Info").toString()
        debug("获取到的Info -> $setting")
        val didnt = SectionUtil.getKey(file, "DropGroup.Options.Exp.Didnt").cint
        debug("获取到的扣除下限 -> $didnt")
        var result: Int
        val level = player.level
        debug("玩家等级")
        if (didnt > level) return 0
        when (dropType) {
            "percentage" -> {
                debug("百分比")
                val info = setting.removeSuffix("%").cfloat
                val per = info / 100.0
                result = (level * per).cint
            }
            "range" -> {
                debug("范围")
                val (setLeft, setRight) = setting.split("..").map { it.cint }
                result = ThreadLocalRandom.current().nextInt(setLeft, setRight + 1)
            }
            "amount" -> {
                debug("指定")
                result = setting.cint
            }
            "none" -> {
                debug("不掉")
                result = 0
            }
            "all" -> {
                debug("全掉")
                result = level
            }
            else -> throw IllegalArgumentException("Invalid drop type")
        }
        if (result > level) result = level
        debug("获取最终玩家掉落Exp -> $result")
        return  result
    }
}