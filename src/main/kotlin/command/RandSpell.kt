package org.tfcc.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toPlainText
import org.tfcc.bot.CommandHandler
import org.tfcc.bot.storage.RandSpellData
import org.tfcc.bot.storage.RandSpellData.RandData
import org.tfcc.bot.storage.TFCCConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object RandSpell : CommandHandler {
    override val name = "随符卡"

    override fun showTips(groupCode: Long, senderId: Long) = "随符卡"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message? {
        val oneTimeLimit = TFCCConfig.qq.randOneTimeLimit
        if (content.isEmpty())
            return PlainText("请输入要随机的作品与符卡数量，例如：“随符卡 红”或“随符卡 全部 ${oneTimeLimit}”")
        val cmds = content.split(" ", limit = 2)
        val game = cmds[0]
        val count = cmds.getOrNull(1)?.toInt() ?: 1 // 默认抽取一张符卡
        val v = gameMap[game] ?: return null
        if (count > v.size)
            return PlainText("请输入小于或等于该作符卡数量${v.size}的数字")
        val text: String?
        synchronized(RandSpellData) {
            val m = RandSpellData.randData.toMutableMap()
            val d = m.getOrPut(msg.sender.id) { RandData() }
            val last = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            last.time = Date(d.lastRandTime)
            val now = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            if (now.get(Calendar.YEAR) != last.get(Calendar.YEAR)
                || now.get(Calendar.MONTH) != last.get(Calendar.MONTH)
                || now.get(Calendar.DATE) != last.get(Calendar.DATE)
            ) d.count = 0
            d.count++
            val limitCount = TFCCConfig.qq.randCount
            text = if (d.count <= limitCount) {
                synchronized(v) {
                    v.shuffle()
                    v.copyOfRange(0, count)
                }.joinToString(separator = "\n")
            } else if (d.count == limitCount + 1) "随符卡一天只能使用${limitCount}次" else null
            d.lastRandTime = now.time.time
            RandSpellData.randData = m
        }
        return text?.toPlainText()
    }

    private val gameMap = (RandGame.games + "全部").flatMap { game ->
        javaClass.getResourceAsStream("/spells/${game}.txt")?.use { `is` ->
            BufferedReader(InputStreamReader(`is`, Charsets.UTF_8)).use { br ->
                var line: String?
                val ls = ArrayList<String>()
                while (true) {
                    line = br.readLine()
                    if (line == null) break
                    ls.add(line.trim())
                }
                val lines = ls.toTypedArray()
                if (game.length < 5) listOf(game to lines)
                else listOf(
                    game to lines,
                    game.substring(2, 3) to lines,
                    game.substring(4, 5) to lines,
                    game.substring(2, 5) to lines,
                )
            }
        } ?: listOf()
    }.toMap()
}