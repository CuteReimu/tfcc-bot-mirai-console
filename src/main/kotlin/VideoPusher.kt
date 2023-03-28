package org.tfcc.bot

import bilibili.data.Video
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.messageChainOf
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiLogger
import org.tfcc.bot.bilibili.Bilibili
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.storage.TFCCConfig

object VideoPusher {
    suspend fun push() {
        val result = getNewVideo() ?: return
        for (groupId in TFCCConfig.videoPush.qqGroup) {
            val group = getGroup(groupId) ?: continue
            val image =
                if (result.pic.isNullOrEmpty()) null
                else {
                    runCatching {
                        Bilibili.getPic(result.pic).use { `is` ->
                            `is`.toExternalResource().use { group.uploadImage(it) }
                        }
                    }.getOrElse {
                        logger.error("获取或上传封面失败", it)
                        null
                    }
                }
            val desc = result.desc ?: ""
            val descString = if (desc.length > 100) desc.substring(0, 100) + "。。。" else desc
            val up = result.author ?: ""
            val text = PlainText(
                "${result.title}\n" +
                        "https://www.bilibili.com/video/${result.bvid}\n" +
                        "UP主：${up}\n" +
                        "视频简介：$descString"
            )
            val msg = if (image == null) text.toMessageChain() else messageChainOf(image, text)
            group.sendMessage(msg)
            RepeaterInterruption.clean(groupId)
        }
    }

    fun getNewVideo(): Video? {
        val videoList = Bilibili.getUserVideos(TFCCConfig.bilibili.mid)
        val latestVideo = videoList.list.vlist.firstOrNull() ?: return null
        if (BilibiliData.lastVideoId == latestVideo.bvid) return null
        BilibiliData.lastVideoId = latestVideo.bvid
        return latestVideo
    }

    private fun getGroup(groupId: Long): Group? {
        Bot.instances.forEach { return it.getGroup(groupId) ?: return@forEach }
        return null
    }

    private val logger: MiraiLogger by lazy {
        MiraiLogger.Factory.create(this::class, this::class.java.name)
    }
}