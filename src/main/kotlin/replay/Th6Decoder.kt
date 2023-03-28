package org.tfcc.bot.replay

import java.io.InputStream

private val th6Char = arrayOf("ReimuA", "ReimuB", "MarisaA", "MarisaB")
private val th6Rank = arrayOf("Easy", "Normal", "Hard", "Lunatic", "Extra")

internal fun decodeTh6Replay(fis: InputStream): ReplayInfo {
    val dat = ByteArray(0x30)
    if (fis.read(dat, 4, dat.size - 4) < dat.size - 4) throw ReplayAnalyzeException("not a replay")
    // Decryption
    val dat2 = ByteArray(dat.size)
    var mask = dat[0x0e]
    System.arraycopy(dat, 0, dat2, 0, 0x0f)
    for (i in 0x0f until dat.size) {
        dat2[i] = (dat[i] - mask).toByte()
        mask = (mask + 0x07).toByte()
    }
    // check
    if (dat2[0x06] !in 0..0x04 || dat2[0x07] !in 0..0x05) throw ReplayAnalyzeException("decrypt th6 replay failed")

    // replay info
    return ReplayInfo(
        game = "6",
        player = String(dat2, 0x19, 8).trimEnd('\u0000'),
        char = th6Char.getOrElse(dat2[0x06].toInt()) { "Unknown" },
        score = dat2.readInt(0x24).toLong(),
        rank = th6Rank.getOrElse(dat2[0x07].toInt()) { "Unknown" },
        drop = dat2.readFloat(0x2c),
    )
}