package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import org.bukkit.Material
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang
import taboolib.platform.util.killer
import java.util.*

object ToolsUtil {
    fun debug(text: String) {
        if (ConfigManager.settingDebugEnable) {
          return console().sendLang("Plugin-Debug", text)
        }
    }

    fun timing(): Long {
        return System.nanoTime()
    }

    fun timing(start: Long): Double {
        return Coerce.format((System.nanoTime() - start).div(1000000.0))
    }

    fun causeDeath(event: PlayerDeathEvent): String {
        val killer = event.entity.killer
        val killers = event.killer
        val damageEvent = event.entity.lastDamageCause
        //判断是否为玩家
        if (killer != null) { return killer.name }
        //判断是否为其他实体
        if (killers != null) { return killers.name }
        //判断意外死亡
        //判断意外死亡
        damageEvent?.cause?.let { cause ->
            return when (cause) {
                in EntityDamageEvent.DamageCause.values() -> cause.name.lowercase(Locale.getDefault())
                else -> "unknown"
            }
        } ?: return "unknown"
    }
    fun type(event: PlayerDeathEvent): String? {
        val killer = event.entity.killer
        val killers = event.killer

        //判断是否为玩家
        if (killer != null) { return "player" }
        //判断是否为其他实体
        if (killers != null) { return "mob" }
        return null
    }

    fun String.toMaterial(): Material {
        return Material.getMaterial(this) ?: throw IllegalArgumentException("Invalid material name: $this")
    }

    fun String.parsePercent(): Float {
        return this.removeSuffix("%").toFloat() / 100.0f
    }
}