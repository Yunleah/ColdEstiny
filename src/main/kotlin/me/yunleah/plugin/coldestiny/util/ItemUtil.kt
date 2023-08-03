package me.yunleah.plugin.coldestiny.util

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.util.ToolsUtil.debug
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.util.Vector
import taboolib.common5.cdouble
import taboolib.common5.clong
import taboolib.module.nms.spawnEntity
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.cos
import kotlin.math.sin

object ItemUtil {
    /**
     * 根据信息进行物品掉落
     *
     * @param dropItems 掉落物列表
     * @param location 掉落位置
     * @param offsetXString 发射横向偏移量
     * @param offsetYString 发射纵向偏移量
     * @param angleType 发射角度类型
     */
    @JvmStatic
    fun dropItems(
        dropItems: List<ItemStack>,
        location: Location,
        offsetXString: String? = null,
        offsetYString: String? = null,
        angleType: String? = null,
    ) {
        if (offsetXString != null && offsetYString != null && angleType != null) {
            val offsetX: Double = if (offsetXString.contains("-")) {
                val index = offsetXString.indexOf("-")
                val min = offsetXString.substring(0, index).toDoubleOrNull()
                val max = offsetXString.substring(index+1).toDoubleOrNull()
                when {
                    min != null && max != null -> ThreadLocalRandom.current().nextDouble(min, max)
                    else -> 0.1
                }
            } else {
                offsetXString.toDoubleOrNull() ?: 0.1
            }
            val offsetY: Double = if (offsetYString.contains("-")) {
                val index = offsetYString.indexOf("-")
                val min = offsetYString.substring(0, index).toDoubleOrNull()
                val max = offsetYString.substring(index+1).toDoubleOrNull()
                when {
                    min != null && max != null -> ThreadLocalRandom.current().nextDouble(min, max)
                    else -> 0.1
                }
            } else {
                offsetYString.toDoubleOrNull() ?: 0.1
            }
            dropItems.forEachIndexed { index, itemStack ->
                val world = location.world
                ColdEstiny.bukkitScheduler.callSyncMethod(ColdEstiny.plugin) {
                    world!!.dropItem(location, itemStack).let { item ->
                        val vector = Vector(offsetX, offsetY, 0.0)
                        if (angleType == "random") {
                            val angleCos = cos(Math.PI * 2 * ThreadLocalRandom.current().nextDouble())
                            val angleSin = sin(Math.PI * 2 * ThreadLocalRandom.current().nextDouble())
                            val x = angleCos * vector.x + angleSin * vector.z
                            val z = -angleSin * vector.x + angleCos * vector.z
                            vector.setX(x).z = z
                        } else if (angleType == "round") {
                            val angleCos = cos(Math.PI * 2 * index / dropItems.size)
                            val angleSin = sin(Math.PI * 2 * index / dropItems.size)
                            val x = angleCos * vector.x + angleSin * vector.z
                            val z = -angleSin * vector.x + angleCos * vector.z
                            vector.setX(x).z = z
                        }
                        item.velocity = vector
                    }
                }
            }
        } else {
            for (itemStack in dropItems) {
                location.world!!.dropItem(location, itemStack)
            }
        }
    }
}