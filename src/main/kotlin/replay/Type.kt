package org.tfcc.bot.replay

open class ReplayInfo(
    val game: String,
    val player: String,
    val char: String,
    val score: Long,
    val rank: String,
    val drop: Float,
) {
    override fun toString(): String = "TH$game $rank $char\n" +
            "机签：${player.trim()}\n" +
            "分数：${formatScore(score)}\n" +
            "处理落率：%.2f%%".format(drop)

    protected fun formatScore(score: Long): String {
        if (score >= 100000000) return "%.2f亿".format(score.toDouble() / 100000000)
        if (score >= 10000) return "%.1f万".format(score.toDouble() / 10000)
        return score.toString()
    }
}

class Th8ReplayInfo(
    game: String,
    player: String,
    char: String,
    score: Long,
    rank: String,
    drop: Float,
    stage: String,
    private val miss: Int,
    private val bomb: Int,
) : NewReplayInfo(game, player, char, score, rank, drop, stage) {
    override fun toString() = "TH$game $rank $stage $char\n" +
            "机签：${player.trim()}\n" +
            "${miss.toNo()} Miss ${bomb.toNo()} Bomb\n" +
            "分数：${formatScore(score)}\n" +
            "处理落率：%.2f%%".format(drop)

    private fun Int.toNo() = if (this == 0) "No" else toString()
}

open class NewReplayInfo(
    game: String,
    player: String,
    char: String,
    score: Long,
    rank: String,
    drop: Float,
    val stage: String,
) : ReplayInfo(game, player, char, score, rank, drop) {
    override fun toString() = "TH$game $rank $stage $char\n" +
            "机签：${player.trim()}\n" +
            "分数：${formatScore(score)}\n" +
            "处理落率：%.2f%%".format(drop)
}
