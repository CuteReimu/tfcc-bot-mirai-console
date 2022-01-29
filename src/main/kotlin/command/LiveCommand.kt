package top.enkansakura.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.event.events.GroupMessageEvent
import top.enkansakura.TouhouFreshmanCampRobot
import top.enkansakura.bilibili.data.BiliPluginConfig

object LiveCommand : CompositeCommand(
    owner = TouhouFreshmanCampRobot,
    "直播", "live",
    description = "直播指令"
) {

    @SubCommand("开始", "start")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.start() {
        subject?.sendMessage(
            if (fromEvent.sender.id in TouhouFreshmanCampRobot.whiteList) {
                TouhouFreshmanCampRobot.live.startLive()
            } else {
                "你不在白名单中"
            }
        )
    }

    @SubCommand("关闭", "stop")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.stop() {
        subject?.sendMessage(
            if (fromEvent.sender.id in TouhouFreshmanCampRobot.whiteList) {
                TouhouFreshmanCampRobot.live.stopLive()
            } else {
                "你不在白名单中"
            }
        )
    }

    @SubCommand("状态", "status")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.status() {
        subject?.sendMessage(
            TouhouFreshmanCampRobot.live.liveStatus()
        )
    }

    @SubCommand("修改标题", "title")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.title(newTitle: String) {
        subject?.sendMessage(
            if (fromEvent.sender.id in TouhouFreshmanCampRobot.whiteList) {
                TouhouFreshmanCampRobot.live.updateTitle(newTitle)
            } else {
                "你不在白名单中"
            }
        )
    }


    @SubCommand("测试", "test")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.test() {
        if (subject?.id != 490442642L) {
            return
        }
        subject?.sendMessage("""
            ${BiliPluginConfig.adminList}

            ${BiliPluginConfig.whiteList}

            ${BiliPluginConfig.sessData}

            ${BiliPluginConfig.biliJct}

            ${BiliPluginConfig.roomId}
        """.trimIndent())
    }

}

