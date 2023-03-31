package org.tfcc.bot.command

import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

object RemoveAdmin : CommandHandler {
    override val name = "删除管理员"

    override fun showTips(groupCode: Long, senderId: Long) = "删除管理员 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = TFCCConfig.isSuperAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message? {
        val qqNumbers = content.split(" ").map {
            runCatching { it.toLong() }.getOrNull() ?: return null
        }
        if (TFCCConfig.qq.superAdminQQ in qqNumbers)
            return PlainText("你不能删除自己")
        val (succeed, failed) = qqNumbers.partition { PermData.removeAdmin(it) }
        val qqNumberToString = { qqNumber: Long ->
            msg.group[qqNumber]?.nameCardOrNick?.let { name -> "${name}($qqNumber)" } ?: qqNumber.toString()
        }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已删除管理员：", transform = qqNumberToString)
            else failed.joinToString(postfix = "并不是管理员", transform = qqNumberToString)
        return PlainText(result)
    }
}