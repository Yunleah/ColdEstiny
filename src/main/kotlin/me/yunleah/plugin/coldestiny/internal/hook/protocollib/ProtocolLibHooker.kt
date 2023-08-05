package me.yunleah.plugin.coldestiny.internal.hook.protocollib

import com.comphenix.protocol.ProtocolManager

/**
 * ProtocolLib附属挂钩
 */
abstract class ProtocolLibHooker {
    /**
     * 获取ProtocolLib Manager
     */
    abstract fun getManager(): ProtocolManager
}