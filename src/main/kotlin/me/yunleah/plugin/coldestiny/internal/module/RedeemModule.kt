package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.SecuredFile.Companion.loadConfiguration
import taboolib.module.lang.sendLang
import java.io.File
import java.io.InputStreamReader

object RedeemModule {
    fun loadConfigModule(configFile:  ArrayList<File>) {
        val configFileList = configFile.filter { it.name.endsWith("Redeem.yml") }
        console().sendLang("plugin-loadModule", KEY, configFileList.size ,"赎回组")
    }

    fun preRedeemModule(itemList: MutableList<ItemStack>, conf: File, event: PlayerDeathEvent) {
        debug("<->preRedeemModule 运行...")
        debug("itemList -> $itemList")
        debug("以下是预览功能哦~")

        val tt = System.currentTimeMillis()
        val originFile = ColdEstiny.plugin.getResource("data/PlayerRedeemFormat")

        val redeemConf = FileUtil.getFileOrCreate("data/${event.entity.player!!.uniqueId}/setting.yml")

        val originYaml = originFile?.let {
            YamlConfiguration.loadConfiguration(InputStreamReader(it, "UTF-8"))
        } ?: YamlConfiguration()

        redeemConf.writeText(originYaml.saveToString())

        val redeemSettingYaml = loadConfiguration(redeemConf) as ConfigFile
        redeemSettingYaml["Options.Player.Id"] = event.entity.player!!.name
        redeemSettingYaml["Options.Player.UUID"] = event.entity.player!!.uniqueId
        val enable = getKey(conf, "Options.RedeemTime.Enable").toBoolean()
        var time = 0
        if (enable) {
            val timeType = getKey(conf, "Options.RedeemTime.Type")
            val timeS = getKey(conf, "Options.RedeemTime.Time")!!.toInt()
            when (timeType) {
                "S" -> {
                    time = timeS * 20
                }
                "M" -> {
                    time = timeS * 20 * 60
                }
                "H" -> {
                    time = timeS * 20 * 60 * 60
                }
                "D" -> {
                    time = timeS * 20 * 60 * 60 * 60
                }
            }
            debug("当前模式 -> 限时赎回 -> ${time}Tick/$timeS")
        }
        debug("当前模式 -> 不限时赎回 -> 无限时长")
        redeemSettingYaml["Options.Time.Enable"] = enable
        redeemSettingYaml["Options.Time."] = time
        val reList = redeemSettingYaml.getList("Options.List")
        val re = reList.toString().removeSuffix("[").removeSuffix("]").split(",") as ArrayList<String>
        re.add(tt.toString())
        redeemSettingYaml["Options.List"] = re
        redeemSettingYaml.saveToFile(redeemConf)
        val redeemFile = FileUtil.getFileOrCreate("data/${event.entity.player!!.uniqueId}/${tt}.yml")
        mainRedeemModule(redeemFile, itemList)
    }

    private fun mainRedeemModule(file: File, itemList: MutableList<ItemStack>) {
        debug("<->mainRedeemModule 运行...")
        debug("itemList -> $itemList")
        debug("file -> $file")
        debug("赎回还在做哦~")
    }
}