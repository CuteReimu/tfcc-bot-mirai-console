package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.CommandHandler.Companion.handlers

object ShowTips : CommandHandler {
    override val name = "查看帮助"

    override fun showTips(groupCode: Long, senderId: Long) = null

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val tips = handlers.filter { it.checkAuth(msg.group.id, msg.sender.id) }
            .mapNotNull { it.showTips(msg.group.id, msg.sender.id) }
        val result = tips.joinToString(separator = "\n", prefix = "你可以使用以下功能：\n")
        return PlainText(result)
    }
}