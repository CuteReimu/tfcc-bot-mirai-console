package org.tfcc.bot.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.event.events.GroupMessageEvent
import org.tfcc.bot.TouhouFreshmanCampRobot

var whiteList =
    mutableListOf<Long>(TouhouFreshmanCampRobot.superAdmin) + TouhouFreshmanCampRobot.adminList + TouhouFreshmanCampRobot.whiteList

object LiveCommand : CompositeCommand(
    owner = TouhouFreshmanCampRobot,
    "直播", "live",
    description = "直播指令"
) {

    @SubCommand("开始", "start")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.start() {

        if (fromEvent.sender.id in whiteList) {
            subject?.sendMessage(
                TouhouFreshmanCampRobot.live.startLive()
            )
            fromEvent.sender.sendMessage(
                TouhouFreshmanCampRobot.live.getRtmpMsg()
            )
        } else {
            subject?.sendMessage(
                "你不在白名单中"
            )
        }
    }

    @SubCommand("停止", "stop")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.stop() {
        subject?.sendMessage(
            if (fromEvent.sender.id in whiteList) {
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
            if (fromEvent.sender.id in whiteList) {
                TouhouFreshmanCampRobot.live.updateTitle(newTitle)
            } else {
                "你不在白名单中"
            }
        )
    }


//    @SubCommand("测试", "test")
//    suspend fun CommandSenderOnMessage<GroupMessageEvent>.test() {
//        if (subject?.id != 490442642L) {
//            return
//        }
//        subject?.sendMessage("""
//            ${BiliPluginConfig.adminList}
//
//            ${BiliPluginConfig.whiteList}
//
//            ${BiliPluginConfig.sessData}
//
//            ${BiliPluginConfig.biliJct}
//
//            ${BiliPluginConfig.roomId}
//        """.trimIndent())
//    }

}

