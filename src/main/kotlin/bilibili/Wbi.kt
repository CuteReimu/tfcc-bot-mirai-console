package org.tfcc.bot.bilibili

import org.tfcc.bot.utils.md5
import java.net.URLEncoder
import kotlin.math.roundToLong

val mixinKeyEncTab = intArrayOf(
    46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
    33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
    61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
    36, 20, 34, 44, 52
)

/**
 * 对 imgKey 和 subKey 进行字符顺序打乱编码
 */
fun getMixinKey(orig: String) =
    mixinKeyEncTab.fold("") { s, i -> s + orig[i] }.substring(0, 32)

/**
 * 为请求参数进行 wbi 签名
 */
fun encWbi(params: MutableMap<String, String>, imgKey: String, subKey: String): List<Pair<String, String>> {
    val mixinKey = getMixinKey(imgKey + subKey)
    val currTime = (System.currentTimeMillis() / 1000.0).roundToLong()
    params["wts"] = currTime.toString() // 添加 wts 字段
    val paramsList = params.toList().sortedBy { it.first }.map { (k, v) ->
        // 过滤 value 中的 "!'()*" 字符
        k to v.filter { chr -> chr !in "!'()*" }
    }.toMutableList()
    val query = URLEncoder.encode(paramsList.joinToString("&") { "${it.first}=${it.second}" }, Charsets.UTF_8)
    val wbiSign = md5(query + mixinKey) // 计算 w_rid
    paramsList.add("w_rid" to wbiSign)
    return paramsList
}

/**
 * 获取最新的 img_key 和 sub_key
 */
fun getWbiKeys(): Pair<String, String> {
    val result = Bilibili.nav()
    val imgKey = result.wbiImg.imgUrl.substringAfterLast("/").split(".")[0]
    val subKey = result.wbiImg.subUrl.substringAfterLast("/").split(".")[0]
    return Pair(imgKey, subKey)
}
