package org.tfcc.bot.replay

import org.junit.Assert
import org.junit.Test

class ReplayTest {
    @Test
    fun testTh6() {
        val replayInfo = javaClass.getResourceAsStream("/th6_01.rpy")!!.use { decodeReplay(it) }
        Assert.assertEquals(
            "TH6 Lunatic ReimuA\n机签：HIMAJIN@\n分数：1.61亿\n处理落率：0.42%",
            replayInfo.toString()
        )
    }

    @Test
    fun testTh7() {
        val replayInfo = javaClass.getResourceAsStream("/th7_02.rpy")!!.use { decodeReplay(it) }
        Assert.assertEquals(
            "TH7 Lunatic SakuyaA\n机签：HDZ LNB\n分数：7.12亿\n处理落率：0.15%",
            replayInfo.toString()
        )
    }

    @Test
    fun testTh8() {
        val replayInfo = javaClass.getResourceAsStream("/th8_01.rpy")!!.use { decodeReplay(it) }
        Assert.assertEquals(
            "TH8 Lunatic Stage 6-Kaguya 妖夢＆幽々子\n机签：David Lu\n10 Miss 35 Bomb\n分数：13.05亿\n处理落率：0.00%",
            replayInfo.toString()
        )
    }

    @Test
    fun testTh8CN() {
        val replayInfo = javaClass.getResourceAsStream("/th8_02.rpy")!!.use { decodeReplay(it) }
        Assert.assertEquals(
            "TH8 Lunatic Stage 6-Kaguya 妖夢＆幽々子\n机签：David Lu\n10 Miss 35 Bomb\n分数：13.05亿\n处理落率：0.00%",
            replayInfo.toString()
        )
    }

    @Test
    fun testTh18() {
        val replayInfo = javaClass.getResourceAsStream("/th18_01.rpy")!!.use { decodeReplay(it) }
        Assert.assertEquals(
            "TH18 Lunatic All Clear Sanae\n机签：David Lu\n分数：10.63亿\n处理落率：0.10%",
            replayInfo.toString()
        )
    }
}