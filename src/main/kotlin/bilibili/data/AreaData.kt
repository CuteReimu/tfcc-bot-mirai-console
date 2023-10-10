package org.tfcc.bot.bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SubAreaData(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String
)

@Serializable
class AreaData(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("list")
    val list: List<SubAreaData>
)