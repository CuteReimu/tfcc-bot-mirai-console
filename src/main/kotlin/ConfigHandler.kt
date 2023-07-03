package org.tfcc.bot

import net.mamoe.mirai.event.events.FriendMessageEvent
import org.tfcc.bot.storage.TFCCConfig

object ConfigHandler {
    suspend fun handle(e: FriendMessageEvent) {
        if (!TFCCConfig.isSuperAdmin(e.sender.id))
            return
        val content = e.message.contentToString()
        if (content.startsWith("增加QQ群 ")) {
            runCatching {
                val qqGroup = content.substring(6).toLong()
                if (qqGroup in TFCCConfig.checkQQGroups) {
                    e.sender.sendMessage("该QQ群已存在")
                } else {
                    TFCCConfig.checkQQGroups += qqGroup
                    e.sender.sendMessage("增加QQ群成功")
                }
            }
        } else if (content.startsWith("删除QQ群 ")) {
            runCatching {
                val qqGroup = content.substring(6).toLong()
                if (qqGroup !in TFCCConfig.checkQQGroups) {
                    e.sender.sendMessage("该QQ群不存在")
                } else {
                    TFCCConfig.checkQQGroups = TFCCConfig.checkQQGroups.filterNot { it == qqGroup }.toLongArray()
                    e.sender.sendMessage("删除QQ群成功")
                }
            }
        }
    }
}