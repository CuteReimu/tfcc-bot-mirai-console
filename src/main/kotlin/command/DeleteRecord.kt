package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.RandOperationHistory
import org.tfcc.bot.storage.PermData

object DeleteRecord : CommandHandler {
    override val name = "删除随机操作记录"

    override fun showTips(groupCode: Long, senderId: Long) = "删除随机操作记录 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val qqNumbers = content.split(" ").map {
            runCatching { it.toLong() }.getOrNull()
        }
        if (qqNumbers.isEmpty() || qqNumbers[0] == null) {
            return if (RandOperationHistory.deleteRecord(msg.sender.id)) {
                PlainText("QQ${msg.sender.id}的记录删除成功！")
            } else {
                PlainText("QQ${msg.sender.id}没有随机操作记录！")
            }
        } else {
            val succeed = mutableListOf<Long>()
            val failed = mutableListOf<Long>()
            for (qqNumber in qqNumbers) {
                if (qqNumber == null) continue
                if (qqNumber != msg.sender.id && !PermData.isAdmin(msg.sender.id)) continue
                if (RandOperationHistory.deleteRecord(qqNumber)) {
                    succeed.add(qqNumber)
                } else {
                    failed.add(qqNumber)
                }
            }
            val result =
                if (succeed.isNotEmpty()) succeed.joinToString(separator = "\n", prefix = "已清除记录：\n")
                else failed.joinToString(separator = "\n", postfix = "该记录为空或你不是管理员")
            return PlainText(result)
        }
    }
}