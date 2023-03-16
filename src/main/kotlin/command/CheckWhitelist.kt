package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

object CheckWhitelist : CommandHandler {
    override val name = "查看白名单"

    override fun showTips(groupCode: Long, senderId: Long) = "查看白名单 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val qq = runCatching { content.toLong() }.getOrNull() ?: return PlainText("指令格式如下：\n查看白名单 对方QQ号")
        val result =
            if (PermData.isWhitelist(qq)) "${content}是白名单"
            else "${content}不是白名单"
        return PlainText(result)
    }
}