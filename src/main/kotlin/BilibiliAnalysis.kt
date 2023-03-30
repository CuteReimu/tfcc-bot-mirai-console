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
import org.tfcc.bot.bilibili.data.LIVE
import org.tfcc.bot.storage.TFCCConfig

object BilibiliAnalysis {
    private val avReg = Regex(
        "(?:https?://www\\.bilibili\\.com/video/)?av(\\d+)",
        RegexOption.IGNORE_CASE
    )

    private val bvReg = Regex(
        "(?:https?://www\\.bilibili\\.com/video/|https?://b23\\.tv)?bv([0-9A-Za-z]{10})",
        RegexOption.IGNORE_CASE
    )

    private val shortReg = Regex(
        "https?://b23\\.tv/[0-9A-Za-z]{7}\$",
        RegexOption.IGNORE_CASE
    )

    private val liveReg = Regex(
        "https?://live\\.bilibili\\.com/(\\d+)"
    )

    suspend fun handle(e: GroupMessageEvent) {
        if (e.group.id !in TFCCConfig.qq.qqGroup)
            return
        val content = e.message.content.trim()
        val content2 = tryShortUrl(content) ?: content
        tryVideo(e, content2) || tryLive(e, content2)
    }

    private fun tryShortUrl(content: String): String? {
        try {
            return (1..10).fold(content) { url, _ ->
                val result = shortReg.find(url) ?: return url
                Bilibili.resolveShortUrl(result.value) ?: return null
            }
        } catch (e: Exception) {
            logger.error("解析短链接失败：", e)
        }
        return null
    }

    private suspend fun tryVideo(e: GroupMessageEvent, content: String): Boolean {
        val result = tryAvid(content) ?: tryBvid(content) ?: return false
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
        return true
    }

    private fun tryAvid(content: String): VideoData? {
        val result = avReg.matchAt(content, 0) ?: return null
        val aid = result.groupValues.last()
        return Bilibili.getVideoData("aid=${aid}")
    }

    private fun tryBvid(content: String): VideoData? {
        val result = bvReg.matchAt(content, 0) ?: return null
        val bvid = result.groupValues.last()
        return Bilibili.getVideoData("bvid=${bvid}")
    }

    private suspend fun tryLive(e: GroupMessageEvent, content: String): Boolean {
        val regResult = liveReg.matchAt(content, 0) ?: return false
        val roomId = regResult.groupValues.last()
        val result = Bilibili.getRoomInfo(roomId.toInt())
        val image =
            if (result.cover.isNullOrEmpty()) null
            else {
                runCatching {
                    Bilibili.getPic(result.cover).use { `is` ->
                        `is`.toExternalResource().use { e.group.uploadImage(it) }
                    }
                }.getOrElse {
                    logger.error("获取或上传封面失败", it)
                    null
                }
            }
        val desc = result.desc ?: ""
        val descString = if (desc.length > 100) desc.substring(0, 100) + "。。。" else desc
        val text = PlainText(
            "${result.title}\n" +
                    "${LIVE}${roomId}\n" +
                    "人气：${result.online}\n" +
                    "直播简介：$descString"
        )
        val msg = if (image == null) text.toMessageChain() else messageChainOf(image, text)
        e.group.sendMessage(msg)
        return true
    }

    private val logger: MiraiLogger by lazy {
        MiraiLogger.Factory.create(this::class, this::class.java.name)
    }
}