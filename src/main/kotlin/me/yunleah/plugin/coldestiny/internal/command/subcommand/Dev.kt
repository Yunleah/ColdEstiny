package me.yunleah.plugin.coldestiny.internal.command.subcommand

import me.yunleah.plugin.coldestiny.ColdEstiny.KEY
import me.yunleah.plugin.coldestiny.internal.command.Command
import net.minecraft.nbt.NBTTagType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.module.lang.sendWarn
import taboolib.common.platform.function.submit
import taboolib.module.chat.TellrawJson
import taboolib.module.lang.sendLang
import taboolib.module.nms.*
import taboolib.platform.util.hasLore

object Dev {
    val dev = subCommand {
        literal("addLore", optional = true) {
            execute<Player> { sender, _, content ->
                val item = sender.inventory.itemInMainHand
                if (item.type == Material.AIR) return@execute console().sendWarn("plugin-format", KEY, "你手里拿着东西？？？")
                if (item.itemMeta == null) {
                    item.itemMeta = Bukkit.getItemFactory().getItemMeta(item.type)
                }
                val itemMeta = item.itemMeta
                itemMeta!!.lore?.addAll(listOf(
                    content
                ))
                item.itemMeta = itemMeta
                if (item.hasLore(content)) console().sendLang("plugin-format", KEY, "以成功添加Lore！")
                else console().sendWarn("plugin-format", KEY, "添加失败！")
            }
        }
        literal("addNBT", optional = true) {
            execute<Player> { sender, _, content ->
                val item = sender.inventory.itemInMainHand
                if (item.type == Material.AIR) return@execute console().sendWarn("plugin-format", KEY, "你手里拿着东西？？？")
                val itemTag = item.getItemTag()
                itemTag.put("ColdEstiny", content)
                item.setItemTag(itemTag)
                if (item.getItemTag().contains("ColdEstiny")) {
                    val v = item.getItemTag()["ColdEstiny"].toString()
                    if (v == content) console().sendLang("plugin-format", KEY, "以成功添加NBT！")
                    else console().sendWarn("plugin-format", KEY, "添加失败！")
                }
            }
        }
        literal("getLore", optional = true) {
            execute<Player> { sender, _, _ ->
                val item = sender.inventory.itemInMainHand
                val meta = item.itemMeta
                val lore = meta?.lore ?: return@execute console().sendWarn("plugin-format", KEY, "你手里的玩意有Lore？？？")
                sender.sendMessage("§b§m                               §f[ §r§fLore §f]§b§m                               ")
                lore.forEach { sender.sendMessage(it) }
                sender.sendMessage("§b§m                               §f[ §r§fLore §f]§b§m                               ")
            }
        }
        literal("getNBT", optional = true) {
            execute<Player> { sender, _, _ ->
                itemnbtCommandAsync(sender.inventory.itemInMainHand, sender)
            }
        }
    }

    private fun itemnbtCommandAsync (itemStack: ItemStack, sender: Player) {
        submit (async = true) {
            itemnbtCommand(itemStack, sender)
        }
    }

    private fun itemnbtCommand (itemStack: ItemStack, sender: Player) {
        if (itemStack.type != Material.AIR) {
            itemStack.getItemTag().format().sendTo(Command.bukkitAdapter.adaptCommandSender(sender))
        }
    }

    val INDENT = "  "
    val LIST_INDENT = "§b- "

    @JvmStatic
    fun ItemTag.format(): TellrawJson {
        val result = TellrawJson().append("§b§m                               §f[ §r§fNBT §f]§b§m                               ")
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            iterator.next().let { (key, value) ->
                result.append(
                    TellrawJson()
                        .append("§6$key${value.type.asPostfix()}§b: §f")
                        .hoverText(key)
                        .suggestCommand(key)
                )
                if (value.type == ItemTagType.COMPOUND) {
                    result.append("\n").append(INDENT)
                }
                result.append(value.asValueString(1))
                if (iterator.hasNext()) result.append("\n")
            }
        }
        return result.append("§b§m                               §f[ §r§fNBT §f]§b§m                               ")
    }

    fun ItemTagType.asPostfix(): String {
        return when (this) {
            ItemTagType.BYTE -> " §6(§bByte§6)"
            ItemTagType.SHORT ->  " §6(§bShort§6)"
            ItemTagType.INT ->  " §6(§bInt§6)"
            ItemTagType.LONG ->  " §6(§bLong§6)"
            ItemTagType.FLOAT ->  " §6(§bFloat§6)"
            ItemTagType.DOUBLE ->  " §6(§bDouble§6)"
            ItemTagType.STRING ->  " §6(§bString§6)"
            ItemTagType.BYTE_ARRAY -> " §6(§bByteArray§6)"
            ItemTagType.INT_ARRAY -> " §6(§bIntArray§6)"
            ItemTagType.COMPOUND -> " §6(§bCompound§6)"
            ItemTagType.LIST -> " §6(§bList§6)"
            else -> " §6(§b妖魔鬼怪§6)"
        }
    }

    @JvmStatic
    fun ItemTagData.asValueString(level: Int): TellrawJson {
        return when (this.type) {
            ItemTagType.BYTE,
            ItemTagType.SHORT,
            ItemTagType.INT,
            ItemTagType.LONG,
            ItemTagType.FLOAT,
            ItemTagType.DOUBLE,
            ItemTagType.STRING -> {
                this.asString().let {
                    TellrawJson()
                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
                        .hoverText(it)
                        .suggestCommand(it)
                }
            }
            ItemTagType.BYTE_ARRAY -> {
                TellrawJson().also { result ->
                    result.append("\n")
                    val iterator = this.asByteArray().iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { byte ->
                            result.append(
                                with (TellrawJson()) {
                                    repeat (level-1) { append(INDENT) }
                                    append("$LIST_INDENT§f$byte")
                                    hoverText(byte)
                                    suggestCommand(byte)
                                }
                            )
                        }
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
            }
            ItemTagType.INT_ARRAY -> {
                TellrawJson().also { result ->
                    result.append("\n")
                    val iterator = this.asIntArray().iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { int ->
                            result.append(
                                with (TellrawJson()) {
                                    repeat (level-1) { append(INDENT) }
                                    append("$LIST_INDENT§f$int")
                                    hoverText(int)
                                    suggestCommand(int)
                                }
                            )
                        }
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
            }
            ItemTagType.LIST -> {
                TellrawJson().also { result ->
                    result.append("\n")
                    val iterator = this.asList().iterator()
                    while (iterator.hasNext()) {
                        iterator.next().asValueString(level).let {
                            result.append(
                                TellrawJson().also { json ->
                                    repeat (level-1) { json.append(INDENT) }
                                    json.append(LIST_INDENT)
                                    json.append(it)
                                    if (iterator.hasNext()) json.append("\n")
                                }
                            )
                        }
                    }
                }
            }
            ItemTagType.COMPOUND -> {
                val result = TellrawJson()
                val iterator = this.asCompound().iterator()
                var first = true
                while (iterator.hasNext()) {
                    iterator.next().let { (key, value) ->
                        result.append(
                            TellrawJson().also { json ->
                                if (first) {
                                    first = false
                                } else {
                                    repeat (level) { json.append(INDENT) }
                                }
                                json.append("§6$key${value.type.asPostfix()}§b: §f")
                                json.hoverText(key)
                                json.suggestCommand(key)
                            }
                        )
                        if (value.type == ItemTagType.COMPOUND) {
                            result.append("\n")
                            repeat (level+1) { result.append(INDENT) }
                        }
                        result.append(value.asValueString(level+1))
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
                result
            }
            else -> TellrawJson().append("妖魔鬼怪")
        }
    }
}