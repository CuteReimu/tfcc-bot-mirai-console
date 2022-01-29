package top.enkansakura.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.event.events.GroupMessageEvent
import top.enkansakura.TouhouFreshmanCampRobot

object HelpCommand : SimpleCommand(
    owner = TouhouFreshmanCampRobot,
    "帮助", "?",
    description = "插件帮助"
) {
    @Handler
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.help() {
        subject?.sendMessage("""
            红裙直播BOT使用说明：
            @BOT查看可用功能
            直播间管理：
                直播状态 /直播 状态
                开始直播 /直播 开始
                结束直播 /直播 关闭
                修改标题 /直播 修改标题 <标题>
            权限查询：
                查询某人权限 /查询 <QQ号|At>
                列出白名单 /查看 白名单
                列出管理员 /查看 管理员
            白名单管理：
                增加白名单 /白名单 增加 <QQ号|At>
                删除白名单 /白名单 删除 <QQ号|At>
            管理员管理：
                增加管理员 /管理员 增加 <QQ号|At>
                删除管理员 /管理员 删除 <QQ号|At>
        """.trimIndent())
    }
}