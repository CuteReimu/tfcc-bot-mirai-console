package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

object ChangeLiveArea : CommandHandler {
    override val name = "修改直播分区"

    override fun showTips(groupCode: Long, senderId: Long) = "修改直播分区 新分区"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message {
        val name = content.trim()
        if (name.isEmpty())
            return PlainText("指令格式如下：\n修改直播分区 新分区")
        var id = 0
        val areaList = try {
            Bilibili.getAreaList()
        } catch (e: Exception) {
            return PlainText(e.message ?: "获取直播分区信息失败")
        }
        if (!areaList.any { area ->
                area.list.find { it.name == name }?.also { id = it.id } ?: return@any false
                true
            }) return PlainText("没有这个分区")
        TFCCConfig.bilibili = TFCCConfig.bilibili.copy(areaV2 = id)
        return PlainText("直播分区已修改为$name")
    }
}