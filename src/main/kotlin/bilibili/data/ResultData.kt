package org.tfcc.bot.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class ResultData(

    @SerialName("code")
    val code: Int,

    @SerialName("message")
    val message: String? = null,

    @SerialName("msg")
    val msg: String? = null,

    @SerialName("data")
    val data: JsonElement? = null

)
