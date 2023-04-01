package org.tfcc.bot

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.FileMessage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.tfcc.bot.replay.decodeReplay
import org.tfcc.bot.storage.TFCCConfig
import java.time.Duration

object ReplayAnalyze {
    suspend fun handle(e: GroupMessageEvent) {
        if (e.group.id !in TFCCConfig.qq.qqGroup)
            return
        for (elem in e.message) {
            if (elem !is FileMessage) continue
            if (elem.name.endsWith(".rpy") && elem.size <= 10 * 1024 * 1024) {
                val file = elem.toAbsoluteFile(e.group) ?: continue
                val url = file.getUrl() ?: continue
                val request = Request.Builder().url(url).get().build()
                val resp = client.newCall(request).execute()
                val text = resp.body!!.byteStream().use { decodeReplay(it) }.toString()
                e.group.sendMessage(text)
                RepeaterInterruption.clean(e.group.id)
            }
        }
    }

    private val client = OkHttpClient().newBuilder().connectTimeout(Duration.ofMillis(20000)).build()
}