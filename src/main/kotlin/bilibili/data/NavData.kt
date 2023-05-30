package org.tfcc.bot.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NavData(
    @SerialName("wbi_img")
    val wbiImg: WbiImgData
)

@Serializable
class WbiImgData(
    @SerialName("img_url")
    val imgUrl: String,

    @SerialName("sub_url")
    val subUrl: String
)