package org.tfcc.bot

import bilibili.data.VideoData
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiLogger
import org.tfcc.bot.bilibili.Bilibili

object BilibiliVideoAnalysis {
    private val avReg = Regex(
        "(?<![A-Za-z0-9])(?:https?://www\\.bilibili\\.com/video/)?av(\\d+)",
        RegexOption.IGNORE_CASE
    )

    private val bvReg = Regex(
        "(?<![A-Za-z0-9])(?:https?://www\\.bilibili\\.com/video/|https?://b23\\.tv)?bv([0-9A-Za-z]{10})",
        RegexOption.IGNORE_CASE
    )

    private val shortReg = Regex(
        "(?<![A-Za-z0-9])https?://b23\\.tv/[0-9A-Za-z]{7}",
        RegexOption.IGNORE_CASE
    )

    suspend fun handle(e: GroupMessageEvent) {
        val content = e.message.content
        val result = tryAvid(content) ?: tryBvid(content) ?: return
        val image =
            if (result.pic.isNullOrEmpty()) null
            else {
                runCatching {
                    Bilibili.getPic(result.pic).use { `is` ->
                        `is`.toExternalResource().use { e.group.uploadImage(it) }
                    }
                }.getOrElse {
                    logger.error("获取或上传封面失败", it)
                    null
                }
            }
        val desc = result.desc ?: ""
        val descString = if (desc.length > 100) desc.substring(0, 100) + "。。。" else desc
        val up = runCatching { result.owner?.jsonObject?.get("name")?.jsonPrimitive?.content }.getOrNull()
        val text = PlainText(
            "${result.title}\n" +
                    "https://www.bilibili.com/video/${result.bvid}\n" +
                    "UP主：${up}\n" +
                    "视频简介：$descString"
        )
        val msg = if (image == null) text.toMessageChain() else messageChainOf(image, text)
        e.group.sendMessage(msg)
        RepeaterInterruption.clean(e.group.id)
    }

    private fun tryAvid(content: String): VideoData? {
        val result = avReg.matchEntire(content) ?: return null
        val aid = result.groupValues.last()
        return Bilibili.getVideoData("aid=${aid}")
    }

    private fun tryBvid(content: String): VideoData? {
        val result = bvReg.matchEntire(content) ?: return null
        val bvid = result.groupValues.last()
        return Bilibili.getVideoData("bvid=${bvid}")
    }

    private val logger: MiraiLogger by lazy {
        MiraiLogger.Factory.create(this::class, this::class.java.name)
    }
}