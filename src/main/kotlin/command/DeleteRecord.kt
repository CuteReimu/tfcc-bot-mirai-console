package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.RandOperationHistory

object DeleteRecord : CommandHandler {
    override val name = "删除随机操作记录"

    override fun showTips(groupCode: Long, senderId: Long) = "删除随机操作记录 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val qqNumbers = content.split(" ").mapNotNull {
            runCatching { it.toLong() }.getOrNull()
        }
        if (qqNumbers.isEmpty()) {
            return if (RandOperationHistory.deleteRecord(msg.sender.id)) {
                PlainText("QQ${msg.sender.id}的记录删除成功！")
            } else {
                PlainText("QQ${msg.sender.id}没有随机操作记录！")
            }
        } else {
            val (succeed, failed) = qqNumbers
                .filter { it == msg.sender.id || PermData.isAdmin(msg.sender.id) }
                .partition { RandOperationHistory.deleteRecord(it) }
            val result =
                if (succeed.isNotEmpty()) succeed.joinToString(separator = "\n", prefix = "已清除记录：\n")
                else failed.joinToString(separator = "\n", postfix = "该记录为空或你不是管理员")
            return PlainText(result)
        }
    }
}