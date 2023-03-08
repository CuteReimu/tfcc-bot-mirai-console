package org.tfcc.bot.command

import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

@ConsoleExperimentalApi
object ChangeLiveTitle : CommandHandler {
    override val name = "修改直播标题"

    override fun showTips(groupCode: Long, senderId: Long) = "修改直播标题 新标题"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isWhitelist(senderId)

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        if (content.isEmpty())
            return Pair(PlainText("指令格式如下：\n修改直播标题 新标题").toMessageChain(), null)
        if (content.length > 20)
            return Pair(null, null)
        if (!PermData.isAdmin(msg.sender.id)) {
            val uid = BilibiliData.live
            if (uid != 0L && uid != msg.sender.id)
                return Pair(PlainText("谢绝唐突修改直播标题").toMessageChain(), null)
        }
        val roomId = TFCCConfig.bilibili.roomId
        val text =
            if (kotlin.runCatching { Bilibili.updateTitle(roomId, content) }.isFailure)
                "修改直播间标题失败，请联系管理员"
            else
                "直播间标题已修改为：$content"
        return Pair(PlainText(text).toMessageChain(), null)
    }
}