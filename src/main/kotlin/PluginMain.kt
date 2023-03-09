package org.tfcc.bot

import kotlinx.coroutines.CoroutineExceptionHandler
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.RandSpellData
import org.tfcc.bot.storage.TFCCConfig
import kotlin.reflect.KClass

internal object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.tfcc.bot",
        name = "Touhou Freshman Camp Bot",
        version = "2.0.1"
    )
) {
    override fun onEnable() {
        BilibiliData.reload()
        TFCCConfig.reload()
        PermData.reload()
        RandSpellData.reload()
        Bilibili.init()
        RepeaterInterruption.init()
        initHandler(GroupMessageEvent::class, CommandHandler::handle)
        initHandler(GroupMessageEvent::class, RepeaterInterruption::handle)
    }

    private fun <E : Event> initHandler(eventClass: KClass<out E>, handler: (E) -> Unit) {
        globalEventChannel().subscribeAlways(
            eventClass,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR,
        ) {
            handler(this)
        }
    }
}
