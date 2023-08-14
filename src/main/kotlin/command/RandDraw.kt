package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.PermData

object RandDraw : CommandHandler {
    override val name = "抽签"

    override fun showTips(groupCode: Long, senderId: Long) = "抽签 选手数量"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val count = runCatching { content.toInt() }.getOrElse {
            return PlainText("指令格式如下：\n抽签 选手数量")
        }
        if (count % 2 != 0)
            return PlainText("选手数量必须为偶数")
        if (count > 32)
            return PlainText("选手数量太多")
        val a = ArrayList<Int>(count)
        (count downTo 1).forEach { a.add(it) }
        val textList = List(count / 2) {
            val a1 = a.removeLast()
            val a2 = a.removeAt(a.indices.random())
            "${a1}号 对阵 ${a2}号"
        }
        return PlainText(textList.joinToString(separator = "\n"))
    }
}