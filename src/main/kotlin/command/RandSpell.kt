package org.tfcc.bot.command

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
        if (content.isEmpty()) return PlainText("请输入要随机的作品与符卡数量，例如：“随符卡 红”或“随符卡 全部 ${oneTimeLimit}”")
        val cmds = content.split(" ", limit = 2)
        val game = cmds[0]
        val count = cmds.getOrNull(1)?.toInt() ?: 1 // 默认抽取一张符卡
        val v = gameMap[game] ?: return null
        if (count > v.size) return PlainText("请输入小于或等于该作符卡数量${v.size}的数字")
        val rCount = increaseRandCount(msg.sender.id)
        val limitCount = TFCCConfig.qq.randCount
        val text =
            if (rCount <= limitCount) v.randSpells(count).joinToString(separator = "\n")
            else if (rCount == limitCount + 1) "随符卡一天只能使用${limitCount}次" else null
        return text?.toPlainText()
    }

    private val mu = Mutex()

    private suspend fun increaseRandCount(qq: Long): Int =
        mu.withLock {
            val m = RandSpellData.randData.toMutableMap()
            val d = m.getOrPut(qq) { RandData() }
            val last = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            last.time = Date(d.lastRandTime)
            val now = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))
            if (!now.isSameDay(last)) d.count = 0
            d.count++
            d.lastRandTime = now.time.time
            RandSpellData.randData = m
            d.count
        }

    private fun Array<String>.randSpells(count: Int): Collection<String> {
        val v1 = toMutableList()
        v1.addAll(v1.filter { it in doubleChance })
        v1.shuffle()
        val v2 = hashSetOf<String>()
        v1.forEach {
            if (v2.size >= count) return@forEach
            v2.add(it)
        }
        return v2
    }

    private val gameMap = (RandGame.games + "全部").flatMap { game ->
        javaClass.getResourceAsStream("/spells/${game}.txt")?.use { `is` ->
            BufferedReader(InputStreamReader(`is`, Charsets.UTF_8)).use { br ->
                val lines = br.readLines().map { it.trim() }.toTypedArray()
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

    private val doubleChance = javaClass.getResourceAsStream("/spells/双倍概率.txt")?.use { `is` ->
        BufferedReader(InputStreamReader(`is`, Charsets.UTF_8)).use { br ->
            br.readLines().map { it.trim() }
        }
    } ?: listOf()

    private fun Calendar.isSameDay(c: Calendar): Boolean =
        arrayOf(Calendar.YEAR, Calendar.MONTH, Calendar.DATE).all { get(it) == c.get(it) }
}