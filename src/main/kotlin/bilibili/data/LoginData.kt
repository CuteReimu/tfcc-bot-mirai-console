package top.enkansakura.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.Challenge

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
