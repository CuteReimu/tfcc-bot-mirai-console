package org.tfcc.bot

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.command.*
import org.tfcc.bot.storage.TFCCConfig

/**
 * 这是聊天指令处理器的接口，当你想要新增自己的聊天指令处理器时，实现这个接口即可。
 */
@ConsoleExperimentalApi
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
     * @return 要发送的群聊消息和私聊消息，为空就是不发送消息
     */
    fun execute(msg: GroupMessageEvent, content: String): Pair<MessageChain?, MessageChain?>

    companion object {
        val handlers = arrayOf(
            ShowTips,
            AddAdmin, RemoveAdmin, ListAllAdmin,
            AddWhitelist, RemoveWhitelist, CheckWhitelist,
            GetLiveState, StartLive, StopLive, ChangeLiveTitle,
            RandGame,
        )

        @OptIn(DelicateCoroutinesApi::class)
        fun handle(e: GroupMessageEvent) {
            if (e.group.id !in TFCCConfig.qq.qqGroup)
                return
            val message = e.message
            GlobalScope.launch {
                if (message.size <= 1)
                    return@launch
                val isAt = message.getOrNull(1)?.let { it is At } ?: false
                if (!isAt && message.size > 2 || message.size > 3)
                    return@launch
                val msg =
                    if (isAt) (message.getOrNull(2) as? PlainText)?.content ?: ShowTips.name
                    else (message.getOrNull(1) as? PlainText)?.content ?: return@launch
                val msgContent = msg.trim()
                if (msgContent.contains("\n") || msgContent.contains("\r"))
                    return@launch
                val msgSlices = msgContent.split(" ", limit = 2)
                val cmd = msgSlices[0]
                val content = msgSlices.getOrElse(1) { "" }
                handlers.forEach {
                    if (it.name == cmd && it.checkAuth(e.group.id, e.sender.id)) {
                        val (groupMsg, privateMsg) = it.execute(e, content)
                        if (groupMsg != null)
                            e.group.sendMessage(groupMsg)
                        if (privateMsg != null)
                            e.sender.sendMessage(privateMsg)
                    }
                }
            }
        }
    }
}