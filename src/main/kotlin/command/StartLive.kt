package org.tfcc.bot.command

import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.bilibili.data.LIVE
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

@ConsoleExperimentalApi
object StartLive : CommandHandler {
    override val name = "开始直播"

    override fun showTips(groupCode: Long, senderId: Long) = "开始直播"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isWhitelist(senderId)

    override fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?> {
        val roomId = TFCCConfig.bilibili.roomId
        val area = TFCCConfig.bilibili.areaV2
        val ret = Bilibili.startLive(roomId, area)
        val addr = "\n直播间地址：${LIVE + TFCCConfig.bilibili.roomId.toString()}\n快来围观吧！"
        val publicText =
            if (ret.change == 0) {
                val uid = BilibiliData.live
                if (uid != 0L && uid != msg.sender.id)
                    return Pair(PlainText("已经有人正在直播了$addr").toMessageChain(), null)
                else
                    BilibiliData.live = msg.sender.id
                "直播间本来就是开启的，推流码已私聊$addr"
            } else {
                BilibiliData.live = msg.sender.id
                "直播间已开启，推流码已私聊，别忘了修改直播间标题哦！$addr"
            }
        val privateText = "RTMP推流地址：${ret.rtmp.addr}\n密钥：${ret.rtmp.code}"
        return Pair(PlainText(publicText).toMessageChain(), PlainText(privateText).toMessageChain())
    }
}