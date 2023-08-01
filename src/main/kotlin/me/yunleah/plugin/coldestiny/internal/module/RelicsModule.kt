package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.relicsFileList
import me.yunleah.plugin.coldestiny.internal.module.KetherModule.runKether
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.ItemUtil
import me.yunleah.plugin.coldestiny.util.SectionUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.common.platform.function.getDataFolder
import taboolib.common5.cint
import taboolib.module.lang.sendLang
import java.io.File

object RelicsModule {
    fun loadRelicsFile() {
        val startTime = ToolsUtil.timing()
        FileUtil.createFolder(File(getDataFolder(), File.separator + "workspace" + File.separator + "relics"))
        console().sendLang("Plugin-LoadFile", relicsFileList.size, "Relics", ToolsUtil.timing(startTime))
    }

    fun checkRelics(managerFile: File): File {
        val relicsKey = SectionUtil.getKey(managerFile, "ManagerGroup.BindGroup.RelicsKey")
        val relicsFile = relicsFileList.filter {
            val key = SectionUtil.getKey(it, "RelicsGroup.GroupKey")
            return@filter key == relicsKey
        } as ArrayList<File>
        return relicsFile.first()
    }

    fun runRelics(file: File, type: String, event: PlayerDeathEvent, dropItem: List<ItemStack>) {
        when (type) {
            "original" -> {
                val location = event.entity.location
                val offsetX = SectionUtil.getKey(file, "RelicsGroup.Options.FancyDrop.offset.x")
                val offsetY = SectionUtil.getKey(file, "RelicsGroup.Options.FancyDrop.offset.y")
                val angleType = SectionUtil.getKey(file, "RelicsGroup.Options.FancyDrop.angle.type")
                ItemUtil.dropItems(dropItem, location, offsetX, offsetY, angleType)
            }
            "timed" -> {
                createTimed()
            }
            "permanent"-> {
                createPermanent()
            }
            "redeem" -> {
                createRedeem()
            }
        }
    }

    private fun createTimed() {
        // TODO
        throw IllegalArgumentException("Invalid relics type")
    }
    private fun createPermanent() {
        // TODO
        throw IllegalArgumentException("Invalid relics type")
    }
    private fun createRedeem() {
        // TODO
        throw IllegalArgumentException("Invalid relics type")
    }
}