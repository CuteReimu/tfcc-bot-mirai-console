package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import kotlin.random.Random

object RandCharacter : CommandHandler {
    override val name = "随机体"

    override fun showTips(groupCode: Long, senderId: Long) = "随机体"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message? {
        if (content.isEmpty())
            return PlainText("请输入要随机的作品，例如：“随机体 红”")
        val v = gameMap[content] ?: return null
        val result = v.joinToString(separator = "") { it[Random.nextInt(it.size)] }
        return PlainText(result)
    }

    private val gameMap = mapOf(
        "东方红魔乡" to arrayOf(arrayOf("灵梦", "魔理沙"), arrayOf("A", "B")),
        "东方妖妖梦" to arrayOf(arrayOf("灵梦", "魔理沙", "咲夜"), arrayOf("A", "B")),
        "东方永夜抄" to arrayOf(
            arrayOf(
                "结界组", "咏唱组", "红魔组", "幽冥组",
                "灵梦", "紫", "魔理沙", "爱丽丝", "咲夜", "蕾米莉亚", "妖梦", "幽幽子",
            )
        ),
        "东方风神录" to arrayOf(arrayOf("灵梦", "魔理沙"), arrayOf("A", "B", "C")),
        "东方地灵殿" to arrayOf(arrayOf("灵梦", "魔理沙"), arrayOf("A", "B", "C")),
        "东方星莲船" to arrayOf(arrayOf("灵梦", "魔理沙", "早苗"), arrayOf("A", "B")),
        "东方神灵庙" to arrayOf(arrayOf("灵梦", "魔理沙", "早苗", "妖梦")),
        "东方辉针城" to arrayOf(arrayOf("灵梦", "魔理沙", "咲夜"), arrayOf("A", "B")),
        "东方绀珠传" to arrayOf(arrayOf("灵梦", "魔理沙", "早苗", "铃仙")),
        "东方天空璋" to arrayOf(
            arrayOf("灵梦", "琪露诺", "射命丸文", "魔理沙"),
            arrayOf("（春）", "（夏）", "（秋）", "（冬）")
        ),
        "东方鬼形兽" to arrayOf(arrayOf("灵梦", "魔理沙", "妖梦"), arrayOf("（狼）", "（獭）", "（鹰）")),
        "东方虹龙洞" to arrayOf(arrayOf("灵梦", "魔理沙", "早苗", "咲夜")),
    ).flatMap {
        listOf(
            it.toPair(),
            it.key.substring(2, 3) to it.value,
            it.key.substring(4, 5) to it.value,
            it.key.substring(2, 5) to it.value,
        )
    }.toMap()
}