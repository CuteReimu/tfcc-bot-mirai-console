package org.tfcc.bot

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.globalEventChannel
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.PermData
import org.tfcc.bot.storage.RandSpellData
import org.tfcc.bot.storage.TFCCConfig
import java.util.*
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
        // initHandler(GroupMessageEvent::class, RepeaterInterruption::handle)
        initHandler(GroupMessageEvent::class, BilibiliAnalysis::handle)
        initHandler(NewFriendRequestEvent::class, ::handleNewFriendRequest)
        initHandler(GroupMessageEvent::class, ReplayAnalyze::handle)
        // startVideoPusher()
        checkQQGroups()
        checkWhitelist()
    }

    private fun <E : Event> initHandler(eventClass: KClass<out E>, handler: suspend (E) -> Unit) {
        globalEventChannel().subscribeAlways(
            eventClass,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR,
        ) {
            launch { handler(this@subscribeAlways) }
        }
    }

    private suspend fun handleNewFriendRequest(e: NewFriendRequestEvent) {
        if (e.bot.groups.any { e.fromId in it.members })
            e.accept()
        else
            e.reject()
    }

    private fun checkWhitelist() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                launch {
                    Bot.instances.forEach { bot ->
                        val memberIds = bot.groups.flatMap {
                            if (it.id !in TFCCConfig.qq.qqGroup) emptyList()
                            else it.members.map { m -> m.id }
                        }.toSet()
                        val needDelete = bot.friends.filterNot { it.id == bot.id || it.id in memberIds }
                        needDelete.forEach {
                            try {
                                it.delete()
                            } catch (e: Exception) {
                                logger.error("删除好友${it.id}失败", e)
                            }
                        }
                        if (needDelete.isNotEmpty()) logger.info("已删除${needDelete.size}个好友")
                        val needDelete2: List<Long>
                        synchronized(PermData) {
                            val result = PermData.whiteList.partition { it in memberIds }
                            PermData.whiteList = result.first
                            needDelete2 = result.second
                        }
                        if (needDelete2.isNotEmpty()) logger.info("已删除${needDelete2.size}个白名单")
                    }
                }
            }
        }, 300000, 300000)
    }

    private fun checkQQGroups() {
        if (TFCCConfig.checkQQGroups.isNotEmpty()) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    launch {
                        Bot.instances.forEach { bot ->
                            bot.groups.forEach {
                                if (it.id !in TFCCConfig.checkQQGroups) {
                                    it.quit()
                                }
                            }
                        }
                    }
                }
            }, 30000, 30000)
        }
    }

    private fun startVideoPusher() {
        val delay = TFCCConfig.videoPush.delay
        if (delay > 0 && TFCCConfig.videoPush.qqGroup.isNotEmpty()) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    launch {
                        VideoPusher.push()
                    }
                }
            }, delay * 1000, delay * 1000)
        }
    }
}
