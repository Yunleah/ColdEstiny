package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ChestManager
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.relicsFileList
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
                val coexistence = SectionUtil.getKey(file, "RelicsGroup.FancyRelics.Coexistence").cint
                val k = key + "<->" + player.uniqueId.toString()
                if (relicsMap.keys.contains(k)) {
                    val oldOrder = relicsMap[k].cint
                    if (oldOrder < coexistence) {
                        relicsMap[k] = oldOrder +1
                    } else {
                        ItemUtil.dropItems(dropItem, player.location)
                        return
                    }
                } else {
                    relicsMap[k] = 1
                }
                val ownerVisible = SectionUtil.getKey(file, "RelicsGroup.FancyRelics.Owner.Visible").cbool
                val ownerAvailable = SectionUtil.getKey(file,"RelicsGroup.FancyRelics.Owner.Available").cbool
                val custom = SectionUtil.getKey(file,"RelicsGroup.FancyRelics.Custom")?: ""
                val breakBlock = SectionUtil.getKey(file,"RelicsGroup.FancyRelics.Owner.BreakBlock").cbool
                ChestManager.createChest(dropItem, player.location, player, ownerVisible, ownerAvailable, timed.toTime(), custom, k, breakBlock)
            }
        }
    }
}


