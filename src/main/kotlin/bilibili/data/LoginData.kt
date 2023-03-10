package org.tfcc.bot.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class VerifyData(

    @SerialName("type")
    val type: String,

    @SerialName("token")
    val token: String,

    @SerialName("geetest")
    val geetest: Geetest

)

@Serializable
class Geetest(

    @SerialName("challenge")
    val challenge: String,

    @SerialName("gt")
    val gt: String

)

@Serializable
class QRCode(

    @SerialName("url")
    val url: String,

    @SerialName("oauthKey")
    val oauthKey: String

)
