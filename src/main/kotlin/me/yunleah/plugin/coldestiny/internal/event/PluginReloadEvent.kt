package me.yunleah.plugin.coldestiny.internal.event

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import me.yunleah.plugin.coldestiny.internal.module.ConfigModule
import me.yunleah.plugin.coldestiny.internal.module.DropModule
import me.yunleah.plugin.coldestiny.internal.module.RedeemModule
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.type.BukkitProxyEvent
import java.io.File

object PluginReloadEvent : BukkitProxyEvent() {
}
@SubscribeEvent
fun event(event: PluginReloadEvent) {
    ColdEstiny.setting.reload()
    DropModule.loadDropModule(ConfigManager.DropFileList as ArrayList<File>)
    ConfigModule.loadConfigModule(ConfigManager.ConfigFileList as ArrayList<File>)
    RedeemModule.loadConfigModule(ConfigManager.RedeemFileList as ArrayList<File>)
}