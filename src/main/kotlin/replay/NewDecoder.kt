package org.tfcc.bot.replay

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

internal fun decodeNewReplay(fis: InputStream, magic: String): NewReplayInfo {
    val game = getNewReplayGame(magic)
    // read data size
    var buf = ByteArray(8)
    if (fis.read(buf) != 8) throw ReplayAnalyzeException("decompress failed")
    buf = ByteArray(4)
    if (fis.read(buf) != 4) throw ReplayAnalyzeException("decompress failed")
    // move to fileinfo block.
    val skipBytes = (buf.readInt() - 4 - 8 - 4).toLong()
    if (fis.skip(skipBytes) != skipBytes) throw ReplayAnalyzeException("decompress failed")

    return BufferedReader(InputStreamReader(fis)).use {
        // retrieve replay info
        // line1: USER????????
        it.readLine()
        // line2: Version (version)\r\n
        it.readLine()
        // line3: Name (name)\r\n
        val player = it.readLine().getValue("Name ") ?: ""
        // line4: Date yy/mm/dd hh24:mi
        it.readLine()
        // line5: Chara (char)\r\n
        val char = it.readLine().getValue("Chara ") ?: ""
        // line6: ;Rank (rank)\r\n
        val rank = it.readLine().getValue("Rank ") ?: ""
        // line7: Extra|Stage (stage)
        val stage = it.readLine().let { s -> s.getValue("Stage ") ?: s.getValue("Extra ") } ?: ""
        // line8: Score (score)\r\n
        val score = it.readLine().getValue("Score ")!!.toLong() * 10L
        // line9: Slow Rate (rate)\r\n
        val drop = it.readLine().getValue("Slow Rate ")!!.toFloat()
        NewReplayInfo(
            game = game,
            // line3: Name (name)\r\n
            player = player,
            char = char,
            score = score,
            rank = rank,
            stage = stage,
            drop = drop
        )
    }
}

private fun getNewReplayGame(magic: String) =
    when (magic) {
        "t95r" -> "95"
        "t125" -> "125"
        "128r" -> "128"
        "t156" -> "165"
        "al1r" -> "ALCO"
        else -> {
            val game = magic.substring(1, 3)
            if (game < "10" || game > "18") throw ReplayAnalyzeException("not a replay")
            else if (magic[3] != 'r' && (game != "18" || magic[3] != 't')) throw ReplayAnalyzeException("not a replay")
            else game
        }
    }

private fun String.getValue(key: String): String? {
    val line = this.trimEnd('\u0000').trim()
    if (line.indexOf(key) != 0) return null
    return line.substring(key.length)
}