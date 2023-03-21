package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.RandOperationHistory

object GetAllRecords : CommandHandler {
    override val name = "查询全部随机操作记录"

    override fun showTips(groupCode: Long, senderId: Long) = "查询全部随机操作记录 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val qqNumbers = content.split(" ").mapNotNull {
            runCatching { it.toLong() }.getOrNull()
        }
        if (qqNumbers.isEmpty()) {
            val result = RandOperationHistory.getAllRecords(msg.sender.id)
            if (result.isNullOrEmpty()) return PlainText("未查询到记录")
            return PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
        } else {
            val result = qqNumbers.mapNotNull { qqNumber ->
                RandOperationHistory.getAllRecords(qqNumber)?.let { listOf("$qqNumber:", *it.toTypedArray()) }
            }.flatten()
            if (result.isEmpty()) return PlainText("未查询到记录")
            return PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
        }
    }
}