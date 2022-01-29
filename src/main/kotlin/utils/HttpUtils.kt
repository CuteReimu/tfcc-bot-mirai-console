package top.enkansakura.utils

import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import top.enkansakura.TouhouFreshmanCampRobot
import top.enkansakura.bilibili.data.ResultData
import java.time.Duration

class HttpUtils {

    private val cookie: String = TouhouFreshmanCampRobot.sessData
    private val ua = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.69"
    )
    private var client: OkHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(Duration.ofMillis(20000))
        .build()

    private fun sendRequest(request: Request): JsonElement {
        val resp = client.newCall(request).execute()
        if (resp.code != 200) {
            TouhouFreshmanCampRobot.logger.error(
                "请求错误，错误码：${resp.code}，返回内容：${resp.message}"
            )
            throw Exception()
        }
        val body: String = resp.body!!.string()
        return json.parseToJsonElement(body)
    }

    fun get(url: String): JsonElement {
        val request = Request.Builder().url(url)
            .header("cookie", cookie)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("user-agent", ua.random())
            .get().build()
        return sendRequest(request)
    }

    fun post(url: String, postBody: String): JsonElement {
        val media = "application/x-www-form-urlencoded; charset=utf-8"
        val request = Request.Builder().url(url)
            .header("cookie", cookie)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("user-agent", ua.random())
            .post(postBody.toRequestBody(media.toMediaTypeOrNull())).build()
        return sendRequest(request)
    }

    inline fun <reified T> getAndDecode(url: String): T {
        val resp = get(url).decode<ResultData>()
        if (resp.code != 0) {
            TouhouFreshmanCampRobot.logger.error(
                "获取信息失败，错误码：${resp.code}，错误信息1：${resp.message}，错误信息2：${resp.msg}"
            )
            throw Exception()
        }
        return resp.data!!.decode()
    }

    inline fun <reified T> postAndDecode(url: String, postBody: String): T {
        val resp = post(url, postBody).decode<ResultData>()
        if (resp.code != 0) {
            TouhouFreshmanCampRobot.logger.error(
                "获取信息失败，错误码：${resp.code}，错误信息1：${resp.message}，错误信息2：${resp.msg}"
            )
            throw Exception()
        }
        return resp.data!!.decode()
    }

}