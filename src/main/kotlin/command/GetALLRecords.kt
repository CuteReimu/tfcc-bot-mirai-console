package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.RandOperationHistory

object GetALLRecords : CommandHandler {
    override val name = "查询全部随机操作记录"

    override fun showTips(groupCode: Long, senderId: Long) = "查询全部随机操作记录"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val result = RandOperationHistory.getAllRecords()
        if (result.isEmpty()) return PlainText("未查询到记录")
        return PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
    }
}