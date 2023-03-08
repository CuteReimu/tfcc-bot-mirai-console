package org.tfcc.bot.command

import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

@ConsoleExperimentalApi
object RemoveWhitelist : CommandHandler {
    override val name = "删除白名单"

    override fun showTips(groupCode: Long, senderId: Long) = "删除白名单 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        val qqNumbers = content.split(" ").map {
            runCatching { it.toLong() }.getOrNull() ?: return Pair(null, null)
        }
        val (succeed, failed) = qqNumbers.partition { PermData.removeWhitelist(it) }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已删除白名单：")
            else failed.joinToString(postfix = "并不是白名单")
        return Pair(PlainText(result).toMessageChain(), null)
    }
}