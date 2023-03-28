package org.tfcc.bot.replay

import java.io.IOException
import java.io.InputStream

/**
 * decodeReplay 解析录像rpy文件
 *
 * @param fis 传入的[InputStream]。本函数不会关闭[InputStream]，调用者请在使用后自行关闭。
 * @return 解析的录像文件信息
 */
@Throws(IOException::class, ReplayAnalyzeException::class)
fun decodeReplay(fis: InputStream): ReplayInfo {
    val buf = ByteArray(4)
    if (fis.read(buf) != 4) throw ReplayAnalyzeException("not a replay")
    return when (String(buf)) {
        "T6RP" -> decodeTh6Replay(fis)
        "T7RP" -> decodeTh7Replay(fis)
        "T8RP" -> decodeTh8Replay(fis)
        else -> decodeNewReplay(fis, String(buf))
    }
}

internal fun ByteArray.readInt(offset: Int = 0): Int =
    (get(offset).toInt() and 0xFF).or((get(offset + 1).toInt() and 0xFF) shl 8)
        .or((get(offset + 2).toInt() and 0xFF) shl 16).or((get(offset + 3).toInt() and 0xFF) shl 24)

internal fun ByteArray.readFloat(offset: Int = 0): Float = java.lang.Float.intBitsToFloat(readInt(offset))

class ReplayAnalyzeException(msg: String) : Exception(msg)