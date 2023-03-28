package org.tfcc.bot.replay

import java.io.InputStream

private val th7Char = arrayOf("ReimuA", "ReimuB", "MarisaA", "MarisaB", "SakuyaA", "SakuyaB")
private val th7Rank = arrayOf("Easy", "Normal", "Hard", "Lunatic", "Extra", "Phantasm")

internal fun decodeTh7Replay(fis: InputStream): ReplayInfo {
    var dat = byteArrayOf(*"T7RP".toByteArray(), *fis.readAllBytes())
    var dat2 = ByteArray(dat.size)
    var mask = dat[0x0d]
    System.arraycopy(dat, 0, dat2, 0, 0x10)
    for (i in 0x10 until dat.size) {
        dat2[i] = (dat[i] - mask).toByte()
        mask = (mask + 0x07).toByte()
    }

    dat = dat2
    // decompress
    var v04 = 0U
    var v1c = 0U
    var v30 = 0U
    var v28 = 0U
    var v34 = 1U
    var v11 = 0x80U
    var v20 = 0U

    for (i in 0 until 4)
        v20 = v20 * 0x100U + dat[0x17 - i].toUByte()
    val v4b = ByteArray(0x16c80)

    val repLength = dat.readInt(0x18)
    dat2 = ByteArray(repLength + 0x54)
    System.arraycopy(dat, 0, dat2, 0, 0x54)
    var index = 0x54
    var i = 0x54
    while (index < repLength) {
        var flStopDoLoop = false
        while (index < repLength) {
            var flFirstRun = true
            var tmpFirst = true
            while (v30 != 0U || tmpFirst) {
                tmpFirst = false
                if (v11 == 0x80U) {
                    v04 = dat[i].toUInt()
                    if (i.toUInt() - 0x54U < v20)
                        i++
                    else
                        v04 = 0U
                    v28 += v04
                }
                if (flFirstRun) {
                    v1c = v04 and v11
                    v11 = v11 shr 1
                    if (v11 == 0U)
                        v11 = 0x80U
                    if (v1c == 0U) {
                        flStopDoLoop = true
                        break
                    }
                    v30 = 0x80U
                    v1c = 0U
                    flFirstRun = false
                } else {
                    if ((v11 and v04) != 0U)
                        v1c = v1c or v30
                    v30 = v30 shr 1
                    v11 = v11 shr 1
                    if (v11 == 0U)
                        v11 = 0x80U
                }
            }
            if (flStopDoLoop)
                break
            dat2[index] = v1c.toByte()
            index++
            v4b[v34.toInt()] = v1c.toByte()
            v34 = (v34 + 1U) and 0x1fffU
        }
        if (index > repLength)
            break
        v30 = 0x1000U
        v1c = 0U
        while (v30 != 0U) {
            if (v11 == 0x80U) {
                v04 = dat[i].toUInt()
                if (i.toUInt() - 0x54U < v20)
                    i++
                else
                    v04 = 0U
                v28 += v04
            }
            if ((v11 and v04) != 0U)
                v1c = v1c or v30
            v30 = v30 shr 1
            v11 = v11 shr 1
            if (v11 == 0U)
                v11 = 0x80U
        }
        val v0c = v1c
        if (v0c == 0U)
            break
        v30 = 8U
        v1c = 0U
        while (v30 != 0U) {
            if (v11 == 0x80U) {
                v04 = dat[i].toUInt()
                if (i.toUInt() - 0x54U < v20)
                    i++
                else
                    v04 = 0U
                v28 += v04
            }
            if ((v11 and v04) != 0U)
                v1c = v1c or v30
            v30 = v30 shr 1
            v11 = v11 shr 1
            if (v11 == 0U)
                v11 = 0x80U
        }
        val v24 = v1c + 2U
        for (v10 in 0U..v24) {
            if (index >= repLength) break
            val v2c = v4b[((v0c + v10) and 0x1fffU).toInt()]
            dat2[index] = v2c
            index++
            v4b[v34.toInt()] = v2c
            v34 = (v34 + 1U) and 0x1fffU
        }
    }

    // replay info
    return ReplayInfo(
        game = "7",
        player = String(dat2, 0x5e, 8).trimEnd('\u0000'),
        char = th7Char.getOrElse(dat2[0x56].toInt()) { "Unknown" },
        score = dat2.readInt(0x6c).toLong() * 10,
        rank = th7Rank.getOrElse(dat2[0x57].toInt()) { "Unknown" },
        drop = dat2.readFloat(0xcc),
    )
}