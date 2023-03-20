package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.RandOperationHistory

object ClearRecords : CommandHandler {
    override val name = "清除全部随机操作记录"

    override fun showTips(groupCode: Long, senderId: Long) = "清除全部随机操作记录"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        RandOperationHistory.clearRecords()
        return PlainText("全部随机操作记录清除成功！")
    }
}