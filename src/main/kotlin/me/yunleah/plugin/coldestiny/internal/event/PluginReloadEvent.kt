package me.yunleah.plugin.coldestiny.internal.event

import me.yunleah.plugin.coldestiny.ColdEstiny
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.type.BukkitProxyEvent

object PluginReloadEvent : BukkitProxyEvent() {
}
@SubscribeEvent
fun event(event: PluginReloadEvent) {
    ColdEstiny.setting.reload()
}