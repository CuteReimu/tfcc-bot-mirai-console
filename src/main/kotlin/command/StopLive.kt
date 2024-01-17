package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiLogger
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

object StopLive : CommandHandler {
    override val name = "关闭直播"

    override fun showTips(groupCode: Long, senderId: Long) = "关闭直播"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isWhitelist(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message? {
        if (content.isNotEmpty()) return null
        if (!PermData.isAdmin(msg.sender.id)) {
            val uid = BilibiliData.live
            if (uid != 0L && uid != msg.sender.id)
                return PlainText("谢绝唐突关闭直播")
        }
        val roomId = TFCCConfig.bilibili.roomId
        val ret = try {
            Bilibili.stopLive(roomId)
        } catch (e: Exception) {
            logger.error(e)
            return PlainText("关闭直播失败，${e.message}")
        }
        BilibiliData.live = 0
        val publicText =
            if (ret.change == 0) "直播间本来就是关闭的"
            else "直播间已关闭"
        return PlainText(publicText)
    }

    private val logger: MiraiLogger by lazy {
        MiraiLogger.Factory.create(this::class, this::class.java.name)
    }
}