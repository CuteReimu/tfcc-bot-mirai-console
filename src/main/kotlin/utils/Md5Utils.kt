package org.tfcc.bot.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun md5(text: String): String {
    try {
        //获取md5加密对象
        val instance = MessageDigest.getInstance("MD5")
        //对字符串加密，返回字节数组
        val digest = instance.digest(text.toByteArray())
        val sb = StringBuffer()
        for (b in digest) {
            //获取低八位有效值
            val i = b.toInt() and 0xff
            //将整数转化为16进制
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                //如果是一位的话，补0
                hexString = "0$hexString"
            }
            sb.append(hexString)
        }
        return sb.toString()
    } catch (_: NoSuchAlgorithmException) {
    }
    return text
}
