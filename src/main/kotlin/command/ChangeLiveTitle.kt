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

object ChangeLiveTitle : CommandHandler {
    override val name = "修改直播标题"

    override fun showTips(groupCode: Long, senderId: Long) = "修改直播标题 新标题"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isWhitelist(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        if (content.isEmpty())
            return PlainText("指令格式如下：\n修改直播标题 新标题")
        if (content.length > 80)
            return PlainText("直播标题过长")
        if (!PermData.isAdmin(msg.sender.id)) {
            val uid = BilibiliData.live
            if (uid != 0L && uid != msg.sender.id)
                return PlainText("谢绝唐突修改直播标题")
        }
        val roomId = TFCCConfig.bilibili.roomId
        val text =
            try {
                Bilibili.updateTitle(roomId, content)
                "直播间标题已修改为：$content"
            } catch (e: Exception) {
                logger.error(e)
                "修改直播间标题失败，${e.message}"
            }
        return PlainText(text)
    }

    private val logger: MiraiLogger by lazy {
        MiraiLogger.Factory.create(this::class, this::class.java.name)
    }
}