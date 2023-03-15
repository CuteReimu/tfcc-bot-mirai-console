package org.tfcc.bot

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.tfcc.bot.storage.TFCCConfig
import java.lang.System.currentTimeMillis
import kotlin.random.Random

object RepeaterInterruption {
    /** QQ群号对 RepeaterData 的映射 */
    private val data = HashMap<Long, RepeaterData>()

    fun init() {
        TFCCConfig.repeaterInterruption.qqGroup.forEach { data[it] = RepeaterData() }
    }

    fun clean(groupCode: Long) {
        data[groupCode]?.let {
            synchronized(it) {
                it.lastMessage = ""
                it.counter = 0
            }
        }
    }

    suspend fun handle(e: GroupMessageEvent) {
        val data = this.data[e.group.id] ?: return
        if (e.message.isEmpty()) return
        val m = e.message.subList(1, e.message.size).joinToString(separator = "")
        var interrupt = false
        synchronized(data) {
            if (m != data.lastMessage) {
                data.counter = 1
                data.lastMessage = m
            } else {
                data.counter++
                if (data.counter >= TFCCConfig.repeaterInterruption.allowance) {
                    val now = currentTimeMillis()
                    val coolDown = TFCCConfig.repeaterInterruption.coolDown
                    if (now > data.lastTrigger + coolDown * 1000L) {
                        interrupt = true
                        data.counter = 1
                        data.lastTrigger = now
                    }
                }
            }
        }
        if (interrupt) {
            if (Random.nextBoolean()) {
                javaClass.getResourceAsStream("/ddfd.png")?.use { `is` ->
                    `is`.toExternalResource().use { e.group.uploadImage(it) }
                }?.let {
                    e.group.sendMessage(it)
                    return
                }
            }
            var text = "打断复读~~ (^-^)"
            if (text in data.lastMessage)
                text = """(*/ω\*)"""
            e.group.sendMessage(text)
        }
    }

    private class RepeaterData(
        var lastMessage: String = "",
        var counter: Int = 0,
        var lastTrigger: Long = 0,
    )
}