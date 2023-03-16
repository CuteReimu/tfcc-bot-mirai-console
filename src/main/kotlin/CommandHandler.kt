package org.tfcc.bot

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.command.*
import org.tfcc.bot.storage.TFCCConfig

/**
 * 这是聊天指令处理器的接口，当你想要新增自己的聊天指令处理器时，实现这个接口即可。
 */
interface CommandHandler {
    /**
     * 群友输入聊天指令时，第一个空格前的内容
     */
    val name: String

    /**
     * 在【帮助列表】中应该如何显示这个命令。null 表示不显示
     */
    fun showTips(groupCode: Long, senderId: Long): String?

    /**
     * 是否有权限执行这个指令
     */
    fun checkAuth(groupCode: Long, senderId: Long): Boolean

    /**
     * 执行指令
     * @param content 除开指令名（第一个空格前的部分）以外剩下的所有内容
     * @return 要发送的群聊消息，为空就是不发送消息
     */
    suspend fun execute(msg: GroupMessageEvent, content: String): Message?

    companion object {
        val handlers = arrayOf(
            ShowTips,
            AddAdmin, RemoveAdmin, ListAllAdmin,
            AddWhitelist, RemoveWhitelist, CheckWhitelist,
            GetLiveState, StartLive, StopLive, ChangeLiveTitle,
            RandGame, RandCharacter, RandSpell,
        )

        suspend fun handle(e: GroupMessageEvent) {
            if (e.group.id !in TFCCConfig.qq.qqGroup)
                return
            val message = e.message
            if (message.size <= 1)
                return
            val isAt = message.getOrNull(1)?.let { it is At && it.target == e.bot.id } ?: false
            if (!isAt && message.size > 2 || message.size > 3)
                return
            val msg =
                if (isAt) (message.getOrNull(2) as? PlainText)?.content?.trim()
                else (message.getOrNull(1) as? PlainText)?.content?.trim()
            val msgContent = if (!msg.isNullOrEmpty()) msg else if (isAt) ShowTips.name else return
            if (msgContent.contains("\n") || msgContent.contains("\r"))
                return
            val msgSlices = msgContent.split(" ", limit = 2)
            val cmd = msgSlices[0]
            val content = msgSlices.getOrElse(1) { "" }
            handlers.forEach {
                if (it.name == cmd && it.checkAuth(e.group.id, e.sender.id)) {
                    val groupMsg = it.execute(e, content) ?: return@forEach
                    e.group.sendMessage(groupMsg)
                    RepeaterInterruption.clean(e.group.id)
                }
            }
        }
    }
}