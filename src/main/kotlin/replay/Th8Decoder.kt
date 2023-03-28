package org.tfcc.bot.replay

import java.io.InputStream
import java.nio.charset.Charset

internal fun decodeTh8Replay(fis: InputStream): Th8ReplayInfo {
    // read data size
    var buf = ByteArray(8)
    if (fis.read(buf) != 8) throw ReplayAnalyzeException("decompress failed")
    buf = ByteArray(4)
    if (fis.read(buf) != 4) throw ReplayAnalyzeException("decompress failed")
    // move to fileinfo block.
    val skipBytes = (buf.readInt() - 4 - 8 - 4).toLong()
    if (fis.skip(skipBytes) != skipBytes) throw ReplayAnalyzeException("decompress failed")
    // cut USER????????
    if (fis.skip(12L) != 12L) throw ReplayAnalyzeException("decompress failed")
    buf = fis.readAllBytes()
    return parseJP(buf) ?: parseCN(buf) ?: throw ReplayAnalyzeException("parse failed")
}

private fun parseJP(buf: ByteArray): Th8ReplayInfo? {
    val m = buf.inputStream().use { it.readToMap(Charset.forName("Shift_JIS"), "\t") }
    if ("プレイヤー名" !in m) return null
    return Th8ReplayInfo(
        game = "8",
        player = m["プレイヤー名"] ?: "",
        char = m["キャラ名"] ?: "",
        score = m["スコア"]?.toLong() ?: 0,
        rank = m["難易度"] ?: "",
        stage = m["最終ステージ"] ?: "",
        miss = m["ミス回数"]?.toInt() ?: 0,
        bomb = m["ボム回数"]?.toInt() ?: 0,
        drop = m["処理落ち率"]?.trimEnd('\u0000')?.trim()?.replace("%", "")?.toFloat() ?: 0F
    )
}

private fun parseCN(buf: ByteArray): Th8ReplayInfo? {
    val m = buf.inputStream().use { it.readToMap(Charset.forName("GBK"), "\t") }
    if ("玩家名" !in m) return null
    return Th8ReplayInfo(
        game = "8",
        player = m["玩家名"] ?: "",
        char = m["角色名"] ?: "",
        score = m["分数"]?.toLong() ?: 0,
        rank = m["难易度"] ?: "",
        stage = m["最终面"] ?: "",
        miss = m["miss回数"]?.toInt() ?: 0,
        bomb = m["Bomb回数"]?.toInt() ?: 0,
        drop = m["処理落率"]?.trimEnd('\u0000')?.trim()?.replace("%", "")?.toFloat() ?: 0F
    )
}

private fun InputStream.readToMap(charset: Charset, separator: String) =
    bufferedReader(charset).use {
        it.readLines().mapNotNull { line ->
            val arr = line.split(separator, limit = 2)
            if (arr.size == 2) arr[0].trimEnd('\u0000').trim() to arr[1].trimEnd('\u0000').trim()
            else null
        }
    }.toMap()