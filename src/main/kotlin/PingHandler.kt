package org.tfcc.bot

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText

object PingHandler {
    suspend fun handle(e: GroupMessageEvent) {
        val message = e.message
        if (message.size <= 2)
            return
        val isAt = message.getOrNull(1)?.let { it is At && it.target == e.bot.id } ?: false
        if (!isAt)
            return
        val msgContent = (message.getOrNull(2) as? PlainText)?.content?.trim() ?: ""
        if (msgContent != "ping")
            return
        e.group.sendMessage("pong")
    }
}