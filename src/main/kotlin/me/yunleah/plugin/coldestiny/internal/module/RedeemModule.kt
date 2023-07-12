package me.yunleah.plugin.coldestiny.internal.module

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.util.ToolsUtil
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import java.io.File

object RedeemModule {
    fun loadConfigModule(configFile:  ArrayList<File>) {
        val configFileList = configFile.filter { it.name.endsWith("Redeem.yml") }
        console().sendLang("plugin-loadModule", KEY, configFileList.size ,"赎回组")
    }

    fun preRedeemModule(itemList: MutableList<ItemStack>) {
        ToolsUtil.debug("<->preRedeemModule 运行...")
        ToolsUtil.debug("itemList -> $itemList")

    }
}