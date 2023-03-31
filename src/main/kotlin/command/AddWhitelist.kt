package org.tfcc.bot.command

import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

object AddWhitelist : CommandHandler {
    override val name = "增加白名单"

    override fun showTips(groupCode: Long, senderId: Long) = "增加白名单 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message? {
        val qqNumbers = content.split(" ").map {
            runCatching { it.toLong() }.getOrNull() ?: return null
        }
        if (qqNumbers.isEmpty()) return null
        qqNumbers.forEach { if (it !in msg.group) return PlainText("${it}不是群成员") }
        val (succeed, failed) = qqNumbers.partition { PermData.addWhitelist(it) }
        val qqNumberToString = { qqNumber: Long ->
            msg.group[qqNumber]?.nameCardOrNick?.let { name -> "${name}($qqNumber)" } ?: qqNumber.toString()
        }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已增加白名单：", transform = qqNumberToString)
            else failed.joinToString(postfix = "已经是白名单了", transform = qqNumberToString)
        return PlainText(result)
    }
}