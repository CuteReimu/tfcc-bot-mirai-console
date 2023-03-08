package org.tfcc.bot

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import org.tfcc.bot.command.ShowTips
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.TFCCConfig

@ConsoleExperimentalApi
internal object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.tfcc.bot",
        name = "Touhou Freshman Camp Bot",
        version = "2.0.1"
    )
) {
    override fun onEnable() {
        TFCCConfig.reload()
        PermData.reload()
        initCommandHandler()
    }

    private fun initCommandHandler() {
        globalEventChannel().subscribeAlways(
            GroupMessageEvent::class,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR,
        ) call@{
            if (group.id !in TFCCConfig.qq.qqGroup)
                return@call

            launch {
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
                CommandHandler.handlers.forEach {
                    if (it.name == cmd && it.checkAuth(this@call.group.id, this@call.sender.id)) {
                        val (groupMsg, privateMsg) = it.execute(this@call, content)
                        if (groupMsg != null)
                            this@call.group.sendMessage(groupMsg)
                        if (privateMsg != null)
                            this@call.sender.sendMessage(privateMsg)
                    }
                }
            }
        }
    }
}
