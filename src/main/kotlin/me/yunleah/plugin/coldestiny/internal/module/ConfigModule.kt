package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.ConfigFileList
import me.yunleah.plugin.coldestiny.internal.manager.GetManager
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getFileKey
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object ConfigModule {
    fun loadConfigModule(configFile: ArrayList<File>) {
        val configFileList = configFile.filter { it.name.endsWith("Config.yml") }
        console().sendLang("plugin-loadModule", KEY, configFileList.size, "配置组")
    }

    fun worldConfigModule(event: PlayerDeathEvent): List<File>? {
        val trueFileList = ToolsUtil.getTrueFileList(ConfigFileList as ArrayList<File>,"Config.yml")
        debug("§l-----------------------WorldConfig.yml-----------------------")
        debug("获取的所有Config.yml文件 -> $trueFileList")
        val fileKeyEnable = getFileKey(trueFileList as ArrayList<File>, "Options.Enable")
        debug("获取的所有[Config.yml | 启用]列表 -> $fileKeyEnable")
        val fileTrueList = fileKeyEnable.filter { it.second.toBoolean() }.map { it.first }
        val fileFalseList = fileKeyEnable.filter { !it.second.toBoolean() }.map {it.first}
        debug("获取的所有启用的Config.yml列表 -> $fileTrueList")
        val fileKeyWorld = getFileKey(fileTrueList as ArrayList<File>, "Options.World.Enable")
        debug("获取的所有[Config.yml | World启用]列表 -> $fileKeyWorld")
        val fileTrueWorldList = fileKeyWorld.filter { it.second.toBoolean() }.map { it.first }
        debug("获取的所有World启用的Config.yml列表 -> $fileTrueWorldList")
        val fileKeyWorldList = getFileKey(fileTrueWorldList as ArrayList<File>, "Options.World.Info")
        debug("获取的所有World Config列表 -> $fileKeyWorldList")
        val deathWorld = event.entity.world
        debug("传入的玩家死亡世界 -> $deathWorld")
        val cFileList = fileKeyWorldList.filter { it.second == deathWorld.name }.map { it.first }
        val wFile = cFileList + fileFalseList
        debug("通过世界判断确定的Config.yml -> $wFile")
        debug("§l-----------------------WorldConfig.yml-----------------------")
        return when (wFile.isEmpty()) {
            true -> { debug("当前世界暂未被本插件托管! -> $deathWorld"); null }
            false -> { wFile as ArrayList<File> }
        }
    }

    fun permConfigModule(fileList: ArrayList<File>, event:PlayerDeathEvent): ArrayList<File>? {
        debug("§l-----------------------PermConfig.yml-----------------------")
        debug("获取的筛选后Config.yml文件 -> $fileList")
        val permFileList = getFileKey(fileList, "Options.Perm.Enable")
        debug("获取的所有[Config.yml | Perm启用]列表 -> $permFileList")
        val fileTruePermList = permFileList.filter { it.second.toBoolean() }.map { it.first }
        val fileFalsePermList = permFileList.filter { !it.second.toBoolean() }.map { it.first }
        debug("获取的所有Perm启用的Config.yml列表 -> $fileTruePermList")
        val fileKeyPermList = getFileKey(fileTruePermList as ArrayList<File>, "Options.Perm.Info").filter { event.entity.player!!.hasPermission(it.second) }.map { it.first }
        val pFile = fileKeyPermList + fileFalsePermList
        debug("通过世界+权限判断确定的Config.yml -> $pFile")
        debug("§l-----------------------WorldConfig.yml-----------------------")
        return when (pFile.isEmpty()) {
            true -> { debug("当前玩家不具有任何权限使得本插件托管!"); null }
            false -> { pFile as ArrayList<File> }
        }
    }
}