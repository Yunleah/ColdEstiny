package me.yunleah.plugin.coldestiny.internal.hook.protocollib.impl

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import me.yunleah.plugin.coldestiny.internal.hook.protocollib.ProtocolLibHooker

/**
 * ProtocolLib附属挂钩
 *
 * @constructor 启用ProtocolLib附属领地挂钩
 */
class ProtocolLibHookerImpl : ProtocolLibHooker() {
    override fun getManager(): ProtocolManager {
        return ProtocolLibrary.getProtocolManager()
    }
}