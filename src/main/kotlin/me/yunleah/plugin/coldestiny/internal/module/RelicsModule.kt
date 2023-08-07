package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ChestManager
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.relicsFileList
import me.yunleah.plugin.coldestiny.internal.manager.RedeemManager
import me.yunleah.plugin.coldestiny.internal.manager.RedeemManager.redeemMap
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.ItemUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import me.yunleah.plugin.coldestiny.util.ToolsUtil.timing
import me.yunleah.plugin.coldestiny.util.ToolsUtil.toTime
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.cbool
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import java.io.File

object RelicsModule {
    val relicsMap: HashMap<String, Int> = hashMapOf()

    fun loadRelicsFile() {
        val startTime = timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "relics"))
        console().sendLang("Plugin-LoadFile", relicsFileList.size, "Relics", timing(startTime))
    }

    fun checkRelics(managerFile: File): File {
        val relicsKey = SectionUtil.getKey(managerFile, "ManagerGroup.BindGroup.RelicsKey")
        val relicsFile = relicsFileList.filter {
            val key = SectionUtil.getKey(it, "RelicsGroup.GroupKey")
            return@filter key == relicsKey
        } as ArrayList<File>
        return relicsFile.first()
    }

    fun runRelics(file: File, player: Player, dropItem: List<ItemStack>) {
        val fancyRelics = SectionUtil.getKey(file, "RelicsGroup.Options.FancyRelics.Type")
        when (fancyRelics) {
            "drop" -> {
                val location = player.location
                val offsetX = SectionUtil.getKey(file, "RelicsGroup.Options.FancyDrop.offset.x")
                val offsetY = SectionUtil.getKey(file, "RelicsGroup.Options.FancyDrop.offset.y")
                val angleType = SectionUtil.getKey(file, "RelicsGroup.Options.FancyDrop.angle.type")
                ItemUtil.dropItems(dropItem, location, offsetX, offsetY, angleType)
            }
            "chest" -> {
                val key = SectionUtil.getKey(file, "RelicsGroup.GroupKey")
                val timed = SectionUtil.getKey(file, "RelicsGroup.Options.FancyRelics.Timed")?: ""
                val coexistence = SectionUtil.getKey(file, "RelicsGroup.Options.FancyRelics.Coexistence").cint
                val title = SectionUtil.getKey(file, "RelicsGroup.Options.FancyRelics.Title")
                val k = key + "<->" + player.uniqueId.toString()
                debug("Map ->$relicsMap")
                debug("Coexistence -> $coexistence")
                if (relicsMap.keys.contains(k)) {
                    debug("Map包含了$k")
                    val oldOrder = relicsMap[k].cint
                    debug("获取到的Map中的次数 -> $oldOrder")
                    if (oldOrder < coexistence) {
                        debug("Map的次数小于限定的数")
                        relicsMap[k] = oldOrder +1
                    } else {
                        debug("Map次数大于等于限定数，执行原版掉落")
                        ItemUtil.dropItems(dropItem, player.location)
                        return
                    }
                } else {
                    debug("Map 不包含 $k")
                    relicsMap[k] = 1
                }
                debug(relicsMap[k].toString())
                val ownerVisible = SectionUtil.getKey(file, "RelicsGroup.FancyRelics.Owner.Visible").cbool
                val ownerAvailable = SectionUtil.getKey(file,"RelicsGroup.FancyRelics.Owner.Available").cbool
                val custom = SectionUtil.getKey(file,"RelicsGroup.FancyRelics.Custom")?: ""
                ChestManager.createChest(dropItem, player.location, player, ownerVisible, ownerAvailable, timed.toTime(), custom, k, title?: " ")
            }
            "redeem" -> {
                val key = SectionUtil.getKey(file, "RelicsGroup.GroupKey")
                val timed = SectionUtil.getKey(file, "RelicsGroup.Options.FancyRelics.Timed")?: ""
                val coexistence = SectionUtil.getKey(file, "RelicsGroup.Options.FancyRelics.Coexistence").cint
                RedeemManager.addRedeem(player, dropItem, coexistence, timed.toTime())
            }
            else -> throw IllegalArgumentException("Invalid relics type")
        }
    }
}


