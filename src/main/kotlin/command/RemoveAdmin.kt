package org.tfcc.bot.command

import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

@ConsoleExperimentalApi
object RemoveAdmin : CommandHandler {
    override val name = "删除管理员"

    override fun showTips(groupCode: Long, senderId: Long) = "删除管理员 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = TFCCConfig.isSuperAdmin(senderId)

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        val qqNumbers = content.split(" ").map {
            runCatching { it.toLong() }.getOrNull() ?: return Pair(null, null)
        }
        if (TFCCConfig.qq.superAdminQQ in qqNumbers)
            return Pair(PlainText("你不能删除自己").toMessageChain(), null)
        val (succeed, failed) = qqNumbers.partition { PermData.removeAdmin(it) }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已删除管理员：")
            else failed.joinToString(postfix = "并不是管理员")
        return Pair(PlainText(result).toMessageChain(), null)
    }
}