package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.util.FileUtil
import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object RedeemManager {
    fun getRedeemList(player: Player): List<Pair<File, String>> {
        val uUID = player.uniqueId.toString()
        val file = FileUtil.getFileOrCreate("data/$uUID")
        val fileList = FileUtil.getAllFiles(file)
        return fileList.map { it to it.name }.toCollection(ArrayList())
    }
}