package org.tfcc.bot.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyData(

    @SerialName("type")
    val type: String,

    @SerialName("token")
    val token: String,

    @SerialName("geetest")
    val geetest: Geetest

)

@Serializable
data class Geetest(

    @SerialName("challenge")
    val challenge: String,

    @SerialName("gt")
    val gt: String

)
