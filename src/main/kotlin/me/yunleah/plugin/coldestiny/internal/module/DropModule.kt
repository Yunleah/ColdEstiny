package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getFileKey
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import taboolib.common.platform.function.console
import taboolib.common.util.random
import taboolib.module.lang.sendError
import taboolib.module.lang.sendLang
import java.io.File
import kotlin.random.Random

object DropModule {
    fun loadDropModule(dropFile:  ArrayList<File>) {
        val dropFileList = dropFile.filter { it.name.endsWith("Drop.yml") }
        console().sendLang("plugin-loadModule", KEY, dropFileList.size ,"掉落组")
    }

    fun dropFileModule(configPath: String, dropFile: ArrayList<File>): List<File>? {
        val config = File(configPath)
        val dropEnable = getKey(config, "Options.Drop.Enable").toBoolean()

        return if (dropEnable) {
            val dropKey = getKey(config, "Options.Drop.Bind")
            val fileKey = getFileKey(dropFile.filter { it.name.endsWith("Drop.yml")} as ArrayList<File>,"Options.Key")
            fileKey.filter { it.second == dropKey }.map { it.first }
        } else {
            null
        }
    }

    fun getSetting(info: String): IntArray? {
        debug("开始处理随机掉落组Setting...")
        val setting = info.removeSurrounding("[", "]").split(",")

        var hasAll = false
        var hasNull = false
        val protectedCells = mutableListOf<Int>()

        setting.forEach { set ->
            when (set) {
                "ALL" -> hasAll = true
                "NULL" -> hasNull = true
                else -> {
                    if (set.startsWith("<_>") && set.endsWith("<_>")) {
                        //保护格
                        val s = set.removeSurrounding("<_>").splitToSequence(",").map { it.toInt() }.toList()
                        protectedCells.addAll(s)
                    }
                }
            }
        }

        if (hasAll && hasNull) {
            return null
        }

        val allCells = IntArray(36) { it + 1 }
        var ret = when {
            hasAll -> allCells
            hasNull -> intArrayOf()
            else -> allCells.filter { !protectedCells.contains(it) }.toIntArray()
        }

        if (hasAll || hasNull) {
            return ret
        }
        //^ ALL NULL实现

        var percentEnable: Boolean = false
        val allCellsPercent = IntArray(30) { it + 1 }
        setting.forEach { set ->
            if (set.endsWith("%")) {
                percentEnable = true
                val percent = set.removeSuffix("%").toInt() / 100.0
                val order = (allCellsPercent.size * percent).toInt()
                val numbers = List(allCellsPercent.size) { it + 1 }.shuffled().take(order)
                ret = allCellsPercent.filter { !protectedCells.contains(it) && it in numbers }.toIntArray()

            }
        }

        if (percentEnable) {
            return ret
        }

        //^ Percent实现

        val allCellsOrder = IntArray(30) { it + 1 }
        ret = intArrayOf()
        setting.forEach { set ->
            if (set.startsWith("<->") && set.endsWith("<=>")) {
                val order = set.removePrefix("<=>").split("~").map { it.toInt() }.toIntArray()
                val preOrder = order.first()
                val postOrder = order.last()
                val or = Random.nextInt(preOrder, postOrder+1)
                val numbersRange = List(allCellsOrder.size) { it + 1 }.shuffled().take(or)
                ret += numbersRange.filter { !protectedCells.contains(it) }
            } else if (set.startsWith("<=>") && set.endsWith("<=>")) {
                val numbersOrder = set.removePrefix("<=>").split(",").map { it.toInt() }.toIntArray()
                ret += numbersOrder.filter { !protectedCells.contains(it) }
            }
        }
        return ret

        //^ Order+Range实现
    }
}