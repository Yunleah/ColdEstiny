package me.yunleah.plugin.coldestiny.util

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object SectionUtil {
    fun getFileKey(files: ArrayList<File>, key: String): List<Pair<File, String>> {
        val preKey = key.split('.').dropLast(1).joinToString(".")
        val postKey = key.split('.').last()
        val result = mutableListOf<Pair<File, String>>()

        for (file in files) {
            val config = YamlConfiguration.loadConfiguration(file)
            val optionKey = config.getConfigurationSection(preKey) ?: continue
            val value = optionKey.getString(postKey) ?: continue
            result.add(file to value)
        }
        return result
    }

    fun getKey(file: File, key:String): String? {
        val preKey = key.split('.').dropLast(1).joinToString(".")
        val postKey = key.split('.').last()
        val config = YamlConfiguration.loadConfiguration(file)
        val optionKey = config.getConfigurationSection(preKey)
        return optionKey?.getString(postKey)
    }

    fun getList(file: File , key: String): ArrayList<*> {
        val preKey = key.split('.').dropLast(1).joinToString(".")
        val postKey = key.split('.').last()
        val config = YamlConfiguration.loadConfiguration(file)
        val optionKey = config.getConfigurationSection(preKey)
        return optionKey?.getList(postKey) as ArrayList<*>
    }
}