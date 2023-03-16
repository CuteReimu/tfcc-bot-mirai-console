package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.bilibili.data.LIVE
import org.tfcc.bot.storage.TFCCConfig

object GetLiveState : CommandHandler {
    override val name = "直播状态"

    override fun showTips(groupCode: Long, senderId: Long) = "直播状态"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val liveInfo = Bilibili.getRoomInfo(TFCCConfig.bilibili.roomId)
        val result =
            if (liveInfo.liveStatus == 0) "直播间状态：未开播"
            else "直播间状态：开播\n" +
                    "直播标题：${liveInfo.title}\n" +
                    "人气：${liveInfo.online}\n" +
                    "直播间地址：${LIVE + TFCCConfig.bilibili.roomId.toString()}"
        return PlainText(result)
    }
}