package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.module.lang.sendError
import java.io.File
import java.time.LocalDateTime

object PluginHandle {
    fun preHandle(event: PlayerDeathEvent) {
        val redeemFile = FileUtil.getFileOrCreate("data${File.separator}${event.entity.player!!.uniqueId}${LocalDateTime.now()}.yml")
        val worldConfig = ConfigModule.worldConfigModule(event)
        val permConfig = ConfigModule.permConfigModule(worldConfig as ArrayList<File>, event)
        if (permConfig!!.size != 1) {
            return console().sendError("plugin-format","有不止一个配置组符合玩家 -> ${event.entity.player!!.name} | 配置组 -> $permConfig")
        }
        val preConfigFile = permConfig[0].toString()
        mainHandle(preConfigFile, redeemFile)
    }

    private fun mainHandle(file: String, redeem: File) {

        postHandle()
    }

    private fun postHandle() {

    }
}