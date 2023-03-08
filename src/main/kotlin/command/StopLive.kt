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
object StopLive : CommandHandler {
    override val name = "关闭直播"

    override fun showTips(groupCode: Long, senderId: Long) = "关闭直播"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isWhitelist(senderId)

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        if (content.isNotEmpty()) return Pair(null, null)
        if (!PermData.isAdmin(msg.sender.id)) {
            val uid = BilibiliData.live
            if (uid != 0L && uid != msg.sender.id)
                return Pair(PlainText("谢绝唐突关闭直播").toMessageChain(), null)
        }
        val roomId = TFCCConfig.bilibili.roomId
        val ret = Bilibili.stopLive(roomId)
        val publicText =
            if (ret.change == 0) "直播间本来就是关闭的"
            else "直播间已关闭"
        return Pair(PlainText(publicText).toMessageChain(), null)
    }
}