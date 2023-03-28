package org.tfcc.bot.storage

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.ValueName
import net.mamoe.mirai.console.data.value

object BilibiliData : AutoSavePluginData("BilibiliData") {
    @ValueDescription("Set-Cookies")
    var cookies: List<String> by value(listOf())

    @ValueName("live")
    var live: Long by value(0L)

    @ValueName("latest_video_id")
    var lastVideoId: String by value("")
}