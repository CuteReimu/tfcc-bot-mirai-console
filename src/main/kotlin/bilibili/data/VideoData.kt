package bilibili.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
class VideoData(

    @SerialName("pic")
    val pic: String?,

    @SerialName("desc")
    val desc: String?,

    @SerialName("title")
    val title: String?,

    @SerialName("bvid")
    val bvid: String?,

    @SerialName("owner")
    val owner: JsonElement?

)