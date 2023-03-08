package org.tfcc.bot.command

import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.CommandHandler.Companion.handlers

@ConsoleExperimentalApi
object ShowTips : CommandHandler {
    override val name = "查看帮助"

    override fun showTips(groupCode: Long, senderId: Long) = null

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        val tips = handlers.filter { it.checkAuth(msg.group.id, msg.sender.id) }
            .mapNotNull { it.showTips(msg.group.id, msg.sender.id) }
        val result = tips.joinToString(separator = "\n", prefix = "你可以使用以下功能：\n")
        return Pair(PlainText(result).toMessageChain(), null)
    }
}