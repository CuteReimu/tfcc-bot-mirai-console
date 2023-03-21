package org.tfcc.bot.bilibili

import bilibili.data.VideoData
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.serialization.json.JsonElement
import net.mamoe.mirai.utils.MiraiLogger
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.tfcc.bot.bilibili.data.*
import org.tfcc.bot.storage.BilibiliData
import org.tfcc.bot.utils.decode
import org.tfcc.bot.utils.json
import java.io.InputStream
import java.time.Duration

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

    private fun getQRCode(): QRCode {
        return getAndDecode(GET_QRCODE)
    }

    private fun loginWithQRCode(qrCode: QRCode) {
        val result = get(LOGIN_WITH_QRCODE + qrCode.oauthKey).decode<ResultData>()
        if (result.code != 0) throw Exception("登录bilibili失败，错误信息${result.message}")
    }

    fun getVideoData(opt: String): VideoData {
        return getAndDecode("${VIDEO_INFO}?$opt")
    }

    fun getPic(url: String): InputStream {
        val request = Request.Builder().url(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("user-agent", ua)
            .get().build()
        val resp = client!!.newCall(request).execute()
        if (resp.code != 200)
            throw Exception("请求错误，错误码：${resp.code}，返回内容：${resp.message}")
        return resp.body!!.byteStream()
    }

    fun resolveShortUrl(url: String): String? {
        val request = Request.Builder().url(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("user-agent", ua)
            .get().build()
        val resp = client!!.newCall(request).execute()
        if (resp.code != 302) {
            resp.close()
            throw Exception("解析短链接失败，错误码：${resp.code}，返回内容：${resp.message}")
        }
        return resp.use { it.headers("Location").firstOrNull() }
    }

    private val logger: MiraiLogger by lazy {
        MiraiLogger.Factory.create(this::class, this::class.java.name)
    }

    private const val ua =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36 Edg/97.0.1072.69"
    private var client: OkHttpClient? = null

    fun init() {
        client = OkHttpClient().newBuilder().followRedirects(false)
            .connectTimeout(Duration.ofMillis(20000)).cookieJar(CookieJarWithData).build()
        if (!BilibiliData.cookies.any { it.startsWith("bili_jct") }) {
            val qrCode = getQRCode()
            val bits = QRCodeWriter().encode(qrCode.url, BarcodeFormat.QR_CODE, 26, 19)
            val s = bits.toString("\u001B[48;5;0m  \u001B[0m", "\u001B[48;5;7m  \u001B[0m")
            logger.info("\n$s")
            logger.info("B站登录过期，请扫码登录B站后按回车")
            readlnOrNull()
            loginWithQRCode(qrCode)
        }
    }

    private fun sendRequest(request: Request): JsonElement {
        val resp = client!!.newCall(request).execute()
        if (resp.code != 200) {
            resp.close()
            throw Exception("请求错误，错误码：${resp.code}，返回内容：${resp.message}")
        }
        val body = resp.body!!.string()
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

    private object CookieJarWithData : CookieJar {
        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return BilibiliData.cookies.mapNotNull {// 单独的读和写本身由底层保证并发安全
                Cookie.parse(url, it)
            }
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            synchronized(BilibiliData) { // 这里既读又写需要保证是原子操作，需要加锁
                val oldCookies = BilibiliData.cookies.mapNotNull { Cookie.parse(url, it) }
                BilibiliData.cookies = (oldCookies + cookies).distinctBy { it.name }.map {
                    cookies.find { cookie -> it.name == cookie.name }?.toString()
                        ?: oldCookies.first { cookie -> it.name == cookie.name }.toString()
                }
            }
        }
    }
}