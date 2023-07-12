package me.yunleah.plugin.coldestiny.internal.handle

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.event.event
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager.DropFileList
import me.yunleah.plugin.coldestiny.internal.manager.GetManager.getKey
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule.verify
import me.yunleah.plugin.coldestiny.internal.module.SpawnModule
import me.yunleah.plugin.coldestiny.util.DropUtil
import me.yunleah.plugin.coldestiny.util.FileUtil
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import me.yunleah.plugin.coldestiny.util.KetherUtil
import me.yunleah.plugin.coldestiny.util.KetherUtil.runActions
import me.yunleah.plugin.coldestiny.util.KetherUtil.toKetherScript
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.*
import taboolib.module.kether.KetherShell
import taboolib.module.kether.printKetherErrorMessage
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
        postHandle(dropItemList, spawn, config.first(), event)
    }

    private fun postHandle(dropItemList: MutableList<Int>, spawn: Location?, config: File, event: PlayerDeathEvent) {
        val preScriptEnable = getKey(config, "Options.Action.Pre.Enable").toBoolean()
        var result = ""
        if (preScriptEnable) {
            try {
                debug("Run Pre Kether...")
                val script = getKey(config, "Options.Action.Pre.Script")
                KetherUtil.stringUtil(script!!).toKetherScript().runActions {
                    this.sender = adaptCommandSender(event.entity.player!!)
                }.thenAccept {
                    result = it as String
                    debug(" §5§l‹ ›§r §7Result: §f$it")
                }
            } catch (e: Exception) {
                e.printKetherErrorMessage()
            }
            if (!result.toBoolean()) {
                return debug("§5§l‹ ›§r §7Result: §f$result")
            }
        }
        //Pre Action




        //Post Action
        val postScriptEnable = getKey(config, "Options.Action.Post.Enable").toBoolean()
        if (postScriptEnable) {
            try {
                debug("Run Post Kether...")
                val script = getKey(config, "Options.Action.Post.Script")
                KetherUtil.stringUtil(script!!).toKetherScript().runActions {
                    this.sender = adaptCommandSender(event.entity.player!!)
                }.thenAccept {
                    debug(" §5§l‹ ›§r §7Result: §f$it")
                }
            } catch (e: Exception) {
                e.printKetherErrorMessage()
            }
        }
        return debug("ColdEstiny -> Finish")
    }
}