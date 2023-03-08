package org.tfcc.bot

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.ChatCommandConfig.enabled

internal object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.tfcc.bot",
        name = "Touhou Freshman Camp Bot",
        version = "2.0.1"
    )
) {
    override fun onEnable() {
        ChatCommandConfig.reload()
        globalEventChannel().subscribeAlways(
            GroupMessageEvent::class,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR,
        ) call@{
            if (!enabled) return@call
            val sender = runCatching {
                this.toCommandSender()
            }.getOrNull() ?: return@call

            launch {
                if (message.size <= 1)
                    return@launch
                val isAt = message.getOrNull(1)?.let { it is At } ?: false
                if (!isAt && message.size > 2 || message.size > 3)
                    return@launch
                val msg = message[if (isAt) 2 else 1] as? PlainText ?: return@launch
                val msgContent = msg.content
                if (msgContent.contains("\n") || msgContent.contains("\r"))
                    return@launch
                val msgSlices = msgContent.split(" ", limit = 2)
                val cmd = msgSlices[0]
                val content = msgSlices.getOrElse(1) { "" }
                CommandHandler.handlers.forEach {
                    if (it.name == cmd && it.checkAuth(this@call.group.id, this@call.sender.id)) {
                        val (groupMsg, _) = it.execute(this@call, content)
                        if (groupMsg != null) {
                            sender.sendMessage(groupMsg)
                        }
                    }
                }
            }
        }
    }
}
