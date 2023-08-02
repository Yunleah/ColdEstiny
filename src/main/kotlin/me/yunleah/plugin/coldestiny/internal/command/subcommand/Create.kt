package me.yunleah.plugin.coldestiny.internal.command.subcommand

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.util.FileUtil
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.sendLang
import java.io.*

object Create {
    val create = subCommand {
        literal("manager", optional = true) {
            dynamic {
                execute<CommandSender> { sender, _, context ->
                    val result = createConfig("manager", context, "Manager")
                    if (result == null) {
                        sender.sendLang("Command-Create-True")
                    }else {
                        sender.sendLang("Command-Create-False", result)
                    }
                }
            }
        }
        literal("drop", optional = true) {
            dynamic {
                execute<CommandSender> { sender, _, context ->
                    val result = createConfig("drop", context, "Drop")
                    if (result == null) {
                        sender.sendLang("Command-Create-True")
                    }else {
                        sender.sendLang("Command-Create-False", result)
                    }
                }
            }
        }
        literal("region", optional = true) {
            dynamic {
                execute<CommandSender> { sender, _, context ->
                    val result = createConfig("region", context, "Region")
                    if (result == null) {
                        sender.sendLang("Command-Create-True")
                    }else {
                        sender.sendLang("Command-Create-False", result)
                    }
                }
            }
        }
        literal("relics" ,optional = true) {
            dynamic {
                execute<CommandSender> { sender, _, context ->
                    val result = createConfig("relics", context, "Relics")
                    if (result == null) {
                        sender.sendLang("Command-Create-True")
                    }else {
                        sender.sendLang("Command-Create-False", result)
                    }
                }
            }
        }
        literal("select", optional = true) {
            dynamic {
                execute<CommandSender> { sender, _, context ->
                    val result = createConfig("select", context, "Select")
                    if (result == null) {
                        sender.sendLang("Command-Create-True")
                    }else {
                        sender.sendLang("Command-Create-False", result)
                    }
                }
            }
        }
    }

    private fun createConfig(type: String, name: String, group: String): String? {
        var len: Int
        val buf = ByteArray(1024)
        val outFile = File(bukkitPlugin.dataFolder, "workspace" + File.separator + type + File.separator + name + ".yml")
        val inputStream = ColdEstiny.plugin.getResource("workspace${File.separator}${type}${File.separator}Example${group}.yml")
        if (outFile.exists()) { return "File already exists" }
        else {
            FileUtil.getFileOrCreate("workspace" + File.separator + type + File.separator + name + ".yml")
        }
        try {
            val fileOutputStream = FileOutputStream(outFile)
            while (inputStream!!.read(buf).also { len = it } > 0) {
                fileOutputStream.write(buf, 0, len)
            }
            fileOutputStream.close()
            inputStream.close()
        } catch (error: IOException) {
            return error.cause.toString()
        }
        return null
    }
}