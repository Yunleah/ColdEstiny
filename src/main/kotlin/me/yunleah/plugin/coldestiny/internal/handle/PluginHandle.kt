package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.DropFileList
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.dev
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendError
import java.io.File
import java.time.LocalDateTime

object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        debug("<->preHandle开始运行...")
        val redeemFile = FileUtil.getFileOrCreate("data${File.separator}${event.entity.player!!.uniqueId}${File.separator}${LocalDateTime.now()}.yml")
        val worldConfig = ConfigModule.worldConfigModule(event)
        val permConfig = ConfigModule.permConfigModule(worldConfig as ArrayList<File>, event)
        if (permConfig == null) {
            return debug("玩家暂未被托管")
        } else if (permConfig.size != 1) {
            return console().sendError("plugin-format","有不止一个配置组符合玩家 -> ${event.entity.player!!.name} | 配置组 -> $permConfig")
        }
        val configPath = permConfig.first().toString()
        val dropPath = DropModule.dropFileModule(configPath, DropFileList as ArrayList<File>)?.first()
        mainHandle(redeemFile, permConfig, dropPath)
    }

    private fun mainHandle(redeem: File, config: ArrayList<File>,drop: File?) {
        debug("<->mainHandle开始运行...")
        debug("redeem -> $redeem")
        debug("config -> $config")
        debug("drop -> $drop")
        val dropInfo = drop?.let { getKey(it, "Options.Drop.Info") }
        var dropSetting: IntArray? = null
        submit {
            dropSetting = dropInfo?.let { DropModule.getSetting(it) }
        }
        postHandle(dropSetting,drop)
    }

    private fun postHandle(setting: IntArray?, drop:File?) {
        if (setting == null) {
            return console().sendError("配置文件存在错误：同时存在ALL和NULL -> ${drop?.name}")

        }
    }
}