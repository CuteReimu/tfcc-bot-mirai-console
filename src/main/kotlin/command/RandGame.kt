package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import org.tfcc.bot.CommandHandler
import kotlin.random.Random

object RandGame : CommandHandler {
    override val name = "随作品"

    override fun showTips(groupCode: Long, senderId: Long) = "随作品"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override fun execute(msg: GroupMessageEvent, content: String): Pair<String?, String?> {
        return Pair(games[Random.nextInt(games.size)], null)
    }

    private val games = arrayOf(
        "东方红魔乡",
        "东方妖妖梦",
        "东方永夜抄",
        "东方风神录",
        "东方地灵殿",
        "东方星莲船",
        "东方神灵庙",
        "东方辉针城",
        "东方绀珠传",
        "东方天空璋",
        "东方鬼形兽",
        "东方虹龙洞"
    )
}