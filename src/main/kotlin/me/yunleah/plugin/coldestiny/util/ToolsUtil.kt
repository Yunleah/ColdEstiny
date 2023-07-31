package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.internal.manager.ConfigManager
import org.bukkit.Material
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.module.lang.sendLang
import taboolib.platform.util.killer

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
        when (damageEvent?.cause) {
            EntityDamageEvent.DamageCause.CONTACT -> return "contact"
            EntityDamageEvent.DamageCause.ENTITY_ATTACK -> return "entity_attack"
            EntityDamageEvent.DamageCause.PROJECTILE -> return "projectile"
            EntityDamageEvent.DamageCause.SUFFOCATION -> return "suffocation"
            EntityDamageEvent.DamageCause.FALL -> return "fall"
            EntityDamageEvent.DamageCause.FIRE -> return "fire"
            EntityDamageEvent.DamageCause.FIRE_TICK -> return "fire_tick"
            EntityDamageEvent.DamageCause.LAVA -> return "lava"
            EntityDamageEvent.DamageCause.DROWNING -> return "drowning"
            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION -> return "block_explosion"
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION -> return "entity_explosion"
            EntityDamageEvent.DamageCause.VOID -> return "void"
            EntityDamageEvent.DamageCause.LIGHTNING -> return "lightning"
            EntityDamageEvent.DamageCause.SUICIDE -> return "suicide"
            EntityDamageEvent.DamageCause.STARVATION -> return "starvation"
            EntityDamageEvent.DamageCause.POISON -> return "poison"
            EntityDamageEvent.DamageCause.MAGIC -> return "magic"
            EntityDamageEvent.DamageCause.WITHER -> return "wither"
            EntityDamageEvent.DamageCause.FALLING_BLOCK -> return "falling_block"
            EntityDamageEvent.DamageCause.THORNS -> return "thorns"
            EntityDamageEvent.DamageCause.CUSTOM -> return "custom"
            else -> return "unknown"
        }
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
        val str = this.replace(" ", "_")
        return Material.getMaterial(str.uppercase()) ?: throw IllegalArgumentException("Invalid material name: $this")
    }

    fun String.parsePercent(): Float {
        return this.removeSuffix("%").toFloat() / 100.0f
    }
}