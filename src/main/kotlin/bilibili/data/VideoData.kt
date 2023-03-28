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

@Serializable
class Video(

    @SerialName("pic")
    val pic: String?,

    @SerialName("description")
    val desc: String?,

    @SerialName("title")
    val title: String?,

    @SerialName("bvid")
    val bvid: String,

    @SerialName("author")
    val author: String?

)

@Serializable
class VideoList(

    @SerialName("vlist")
    val vlist: Array<Video>

)

@Serializable
class GetUserVideosResult(

    @SerialName("list")
    val list: VideoList

)
