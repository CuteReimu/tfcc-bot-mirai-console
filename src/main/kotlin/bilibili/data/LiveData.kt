package top.enkansakura.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveInfo(

    @SerialName("live_status")
    val live_status: Int,

    @SerialName("title")
    val title: String,

    @SerialName("online")
    val online: Int

)


@Serializable
data class LiveStartData(

    @SerialName("change")
    val change: Int,

    @SerialName("status")
    val status: String,

    @SerialName("rtmp")
    val rtmp: Rtmp

)

@Serializable
data class Rtmp(

    @SerialName("addr")
    val addr: String,

    @SerialName("code")
    val code: String

)

@Serializable
data class LiveStopData(

    @SerialName("change")
    val change: Int,

    @SerialName("status")
    val status: String

)
