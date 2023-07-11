package me.yunleah.plugin.coldestiny.internal.handle

import taboolib.common.platform.function.submit
import taboolib.module.kether.KetherShell
import java.util.concurrent.CompletableFuture

object ScriptHandle {
    fun runActionKE(script: String): CompletableFuture<Any?> {
         return KetherShell.eval(script)
    }
}