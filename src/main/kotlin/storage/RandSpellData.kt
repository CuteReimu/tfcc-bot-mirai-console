package org.tfcc.bot.storage

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object RandSpellData : AutoSavePluginData("RandSpellData") {
    @Serializable
    class RandData(
        var lastRandTime: Long = 0,
        var count: Int = 0,
    )

    var randData: Map<Long, RandData> by value(mapOf())
}