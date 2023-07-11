package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.DropFileList
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule.verify
import me.yunleah.plugin.coldestiny.internal.module.SpawnModule
import me.yunleah.plugin.coldestiny.util.DropUtil
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.Location
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.dev
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.module.kether.KetherShell
import taboolib.module.lang.sendError
import java.io.File
import java.time.LocalDateTime

object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        debug("<->preHandle开始运行...")
        val redeemFile = FileUtil.getFileOrCreate("data${File.separator}${event.entity.player!!.uniqueId}${File.separator}${LocalDateTime.now()}.yml")
        val worldConfig = ConfigModule.worldConfigModule(event) ?: return debug("玩家暂未被托管")
        val permConfig = ConfigModule.permConfigModule(worldConfig as ArrayList<File>, event)
        if (permConfig == null) {
            return debug("玩家暂未被托管")
        } else if (permConfig.size != 1) {
            return console().sendError("plugin-format","有不止一个配置组符合玩家 -> ${event.entity.player!!.name} | 配置组 -> $permConfig")
        }
        val configPath = permConfig.first().toString()
        val dropPath = DropModule.dropFileModule(configPath, DropFileList as ArrayList<File>)?.first()
            ?: return console().sendError("DropConfig -> Null")
        mainHandle(redeemFile, permConfig, dropPath, event)
    }

    private fun mainHandle(redeem: File, config: ArrayList<File>,drop: File, event: PlayerDeathEvent) {
        debug("<->mainHandle开始运行...")
        debug("redeem -> $redeem")
        debug("config -> $config")
        debug("drop -> $drop")
        val dropItemList = DropModule.preDropModule(event,drop)
        val spawn = SpawnModule.spawnModule(event, config.first())
        postHandle(dropItemList, spawn!!, config.first())
    }

    private fun postHandle(dropItemList: MutableList<Int>, spawn: Location, config: File) {
        //TODO Kether-Action Pre
        val preScriptEnable = getKey(config, "Options.Action.Pre.Enable").toBoolean()
        var result = ""
        if (preScriptEnable) {
            val preScript = getKey(config, "Options.Action.Pre.Script")
            result = ScriptHandle.runActionKE(preScript!!).toString()
        }
        if (!result.toBoolean()) {
            return debug("Pre-Action返回值 -> False")
        }







        //TODO Kether-Action Post
        val postScriptEnable = getKey(config, "Options.Action.Post.Enable").toBoolean()
        val postScript = getKey(config, "Options.Action.Post.Script")
        if (postScriptEnable) {
            ScriptHandle.runActionKE(postScript!!)
        }
        return debug("ColdEstiny -> Finish")
    }
}