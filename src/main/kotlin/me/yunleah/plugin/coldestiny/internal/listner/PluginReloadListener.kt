package me.yunleah.plugin.coldestiny.internal.listner

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.internal.event.PluginReloadEvent
import me.yunleah.plugin.coldestiny.internal.module.*
import taboolib.common.platform.event.SubscribeEvent

object PluginReloadListener {
    @SubscribeEvent
    fun listener(event: PluginReloadEvent) {
        ColdEstiny.setting.reload()
        DropModule.loadDropFile()
        ManagerModule.loadManagerFile()
        RegionModule.loadRegionFile()
        RelicsModule.loadRelicsFile()
        SelectModule.loadSelectFile()
    }
}