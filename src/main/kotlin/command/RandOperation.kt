package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.RandOperationHistory
import org.tfcc.bot.storage.TFCCConfig
import java.util.*
import kotlin.random.Random

object RandOperation : CommandHandler {
    override val name = "随机操作"

    override fun showTips(groupCode: Long, senderId: Long) = "\n" +
        """
            随机操作
            此功能为防止作弊而开发。
            机器人将生成十次随机方向操作指示，如↑，↓，←，→，…。
            在请求指示后，玩家需即刻在游戏内输入这些操作以证明游戏录像的真实性。
        """.trimIndent() + "\n"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        var record = ""
        for (i in 1..TFCCConfig.randOperation.number)
            record += randOperations[Random.nextInt(randOperations.size)]
        val text = record
        val now = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
        val time = "${now.get(Calendar.YEAR)}年${now.get(Calendar.MONTH)}月${now.get(Calendar.DATE)}日 "+
                "${now.get(Calendar.HOUR_OF_DAY)}:${now.get(Calendar.MINUTE)}:${now.get(Calendar.SECOND)}"
        record += "\n$time"
        RandOperationHistory.addRecord(msg.sender.id, record)
        return PlainText(text)
    }

    private val randOperations = arrayOf("↑", "↓", "←", "→")
}