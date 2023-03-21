package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.bilibili.data.LIVE
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

object StartLive : CommandHandler {
    override val name = "开始直播"

    override fun showTips(groupCode: Long, senderId: Long) = "开始直播"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isWhitelist(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val roomId = TFCCConfig.bilibili.roomId
        val area = TFCCConfig.bilibili.areaV2
        val ret = Bilibili.startLive(roomId, area)
        val addr = "\n直播间地址：${LIVE + TFCCConfig.bilibili.roomId.toString()}\n快来围观吧！"
        var text =
            if (ret.change == 0) {
                val uid = BilibiliData.live
                if (uid != 0L && uid != msg.sender.id)
                    return PlainText("已经有人正在直播了$addr")
                else
                    BilibiliData.live = msg.sender.id
                "直播间本来就是开启的，"
            } else {
                BilibiliData.live = msg.sender.id
                "直播间已开启，别忘了修改直播间标题哦！"
            }
        val friend = msg.bot.getFriend(msg.sender.id)
        if (friend == null) {
            text += "如需推流码，请加我为好友后重新开播"
        } else {
            text += "推流码已私聊"
            val privateText = "RTMP推流地址：${ret.rtmp.addr}\n密钥：${ret.rtmp.code}"
            friend.sendMessage(privateText)
        }
        return PlainText(text + addr)
    }
}