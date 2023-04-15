package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Dice
import net.mamoe.mirai.message.data.Message
import org.tfcc.bot.CommandHandler

object RandDice : CommandHandler {
    override val name = "随机骰子"

    override fun showTips(groupCode: Long, senderId: Long) = null

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        return Dice.random()
    }
}