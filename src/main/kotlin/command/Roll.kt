package org.tfcc.bot.command

import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import kotlin.random.Random

object Roll : CommandHandler {
    override val name = "roll"

    override fun showTips(groupCode: Long, senderId: Long) = null

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        return PlainText("${msg.sender.nameCardOrNick} roll: ${Random.nextInt(0, 100)}")
    }
}