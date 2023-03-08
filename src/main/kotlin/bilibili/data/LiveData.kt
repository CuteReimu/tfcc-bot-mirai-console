package org.tfcc.bot.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LiveInfo(
    @SerialName("live_status")
    val liveStatus: Int,

    @SerialName("title")
    val title: String,

    @SerialName("online")
    val online: Int
)


@Serializable
class LiveStartData(
    @SerialName("change")
    val change: Int,

    @SerialName("status")
    val status: String,

    @SerialName("rtmp")
    val rtmp: Rtmp
)

@Serializable
class Rtmp(
    @SerialName("addr")
    val addr: String,

    @SerialName("code")
    val code: String
)

@Serializable
class LiveStopData(
    @SerialName("change")
    val change: Int,

    @SerialName("status")
    val status: String
)
