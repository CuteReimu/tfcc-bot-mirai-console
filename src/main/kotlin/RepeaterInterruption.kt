package org.tfcc.bot

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.events.GroupMessageEvent
import org.tfcc.bot.storage.TFCCConfig
import java.lang.System.currentTimeMillis

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

    @OptIn(DelicateCoroutinesApi::class)
    fun handle(e: GroupMessageEvent) {
        val data = this.data[e.group.id] ?: return
        if (e.message.isEmpty()) return
        val m = e.message.subList(1, e.message.size).joinToString(separator = "")
        GlobalScope.launch {
            var text: String? = null
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
                            text = "打断复读~~ (^-^)"
                            if (text!! in data.lastMessage)
                                text = """(*/ω\*)"""
                            data.counter = 1
                            data.lastTrigger = now
                        }
                    }
                }
            }
            if (text != null) e.group.sendMessage(text!!)
        }
    }

    private class RepeaterData(
        var lastMessage: String = "",
        var counter: Int = 0,
        var lastTrigger: Long = 0,
    )
}