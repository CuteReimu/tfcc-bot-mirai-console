package org.tfcc.bot

import net.mamoe.mirai.event.events.GroupMessageEvent
import org.tfcc.bot.command.RandGame

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
     * @return 要发送的群聊消息和私聊消息，为空就是不发送消息
     */
    fun execute(msg: GroupMessageEvent, content: String): Pair<String?, String?>

    companion object {
        val handlers = arrayOf(
            RandGame,
        )
    }
}