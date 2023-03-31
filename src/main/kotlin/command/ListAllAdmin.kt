package org.tfcc.bot.command

import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

object ListAllAdmin : CommandHandler {
    override val name = "查看管理员"

    override fun showTips(groupCode: Long, senderId: Long) = "查看管理员"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val result = PermData.listAdmin().map { msg.group[it]?.nameCardOrNick ?: it }
            .joinToString(separator = "\n", prefix = "管理员列表：\n")
        return PlainText(result)
    }
}