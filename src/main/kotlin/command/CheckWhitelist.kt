package org.tfcc.bot.command

import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

@ConsoleExperimentalApi
object CheckWhitelist : CommandHandler {
    override val name = "查看白名单"

    override fun showTips(groupCode: Long, senderId: Long) = "查看白名单 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        val qq = runCatching { content.toLong() }.getOrNull()
            ?: return Pair(PlainText("指令格式如下：\n查看白名单 对方QQ号").toMessageChain(), null)
        val result =
            if (PermData.isWhitelist(qq)) "${content}是白名单"
            else "${content}不是白名单"
        return Pair(PlainText(result).toMessageChain(), null)
    }
}