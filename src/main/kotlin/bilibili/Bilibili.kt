package org.tfcc.bot.bilibili

import kotlinx.serialization.json.JsonElement
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.tfcc.bot.bilibili.data.*
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.utils.decode
import org.tfcc.bot.utils.json
import java.time.Duration

@ConsoleExperimentalApi
object Bilibili {
    fun getRoomInfo(roomId: Int): LiveInfo {
        return getAndDecode(LIVE_INFO(roomId))
    }

    fun startLive(roomId: Int, area: Int): LiveStartData {
        val biliJct = BilibiliData.cookies.mapNotNull {
            Cookie.parse(STOP_LIVE.toHttpUrl(), it)
        }.find { it.name == "bili_jct" } ?: throw Exception("B站登录过期")
        val postBody = "room_id=${roomId}&platform=pc&area_v2=${area}&csrf=${biliJct.value}"
        return postAndDecode(START_LIVE, postBody)
    }

    fun stopLive(roomId: Int): LiveStopData {
        val biliJct = BilibiliData.cookies.mapNotNull {
            Cookie.parse(STOP_LIVE.toHttpUrl(), it)
        }.find { it.name == "bili_jct" } ?: throw Exception("B站登录过期")
        val postBody = "room_id=${roomId}&csrf=${biliJct.value}"
        return postAndDecode(STOP_LIVE, postBody)
    }

    fun updateTitle(roomId: Int, title: String) {
        val biliJct = BilibiliData.cookies.mapNotNull {
            Cookie.parse(STOP_LIVE.toHttpUrl(), it)
        }.find { it.name == "bili_jct" } ?: throw Exception("B站登录过期")
        val postBody = "room_id=${roomId}&title=${title}&csrf=${biliJct.value}"
        val result = post(UPDATE_TITLE, postBody).decode<ResultData>()
        if (result.code != 0) throw Exception("更新直播间标题失败")
    }


    private const val ua =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.69"
    private var client: OkHttpClient? = null

    fun init() {
        client = OkHttpClient()
            .newBuilder()
            .connectTimeout(Duration.ofMillis(20000))
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return BilibiliData.cookies.map {
                        Cookie.parse(url, it)!!
                    }
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    val oldCookies = BilibiliData.cookies.map { Cookie.parse(url, it)!! }
                    BilibiliData.cookies = oldCookies.plus(cookies).distinctBy { it.name }.map {
                        cookies.find { cookie -> it.name == cookie.name }?.toString()
                            ?: oldCookies.first { cookie -> it.name == cookie.name }.toString()
                    }
                }
            })
            .build()
    }

    private fun sendRequest(request: Request): JsonElement {
        val resp = client!!.newCall(request).execute()
        if (resp.code != 200)
            throw Exception("请求错误，错误码：${resp.code}，返回内容：${resp.message}")
        val body: String = resp.body!!.string()
        return json.parseToJsonElement(body)
    }

    private fun get(url: String): JsonElement {
        val request = Request.Builder().url(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("user-agent", ua)
            .get().build()
        return sendRequest(request)
    }

    private fun post(url: String, postBody: String): JsonElement {
        val media = "application/x-www-form-urlencoded; charset=utf-8"
        val request = Request.Builder().url(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("user-agent", ua)
            .post(postBody.toRequestBody(media.toMediaTypeOrNull())).build()
        return sendRequest(request)
    }

    private inline fun <reified T> getAndDecode(url: String): T {
        val resp = get(url).decode<ResultData>()
        if (resp.code != 0)
            throw Exception("获取信息失败，错误码：${resp.code}，错误信息1：${resp.message}，错误信息2：${resp.msg}")
        return resp.data!!.decode()
    }

    private inline fun <reified T> postAndDecode(url: String, postBody: String): T {
        val resp = post(url, postBody).decode<ResultData>()
        if (resp.code != 0)
            throw Exception("获取信息失败，错误码：${resp.code}，错误信息1：${resp.message}，错误信息2：${resp.msg}")
        return resp.data!!.decode()
    }
}