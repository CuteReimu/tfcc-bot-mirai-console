package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

object ListAllAdmin : CommandHandler {
    override val name = "查看管理员"

    override fun showTips(groupCode: Long, senderId: Long) = "查看管理员"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        val result = PermData.listAdmin().joinToString(separator = "\n", prefix = "管理员列表：\n")
        return Pair(PlainText(result).toMessageChain(), null)
    }
}