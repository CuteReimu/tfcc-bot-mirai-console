package top.enkansakura.command

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.event.events.GroupMessageEvent
import top.enkansakura.TouhouFreshmanCampRobot
import top.enkansakura.TouhouFreshmanCampRobot.adminList
import top.enkansakura.TouhouFreshmanCampRobot.whiteList
import top.enkansakura.bilibili.data.BiliPluginConfig.superAdmin


object  QueryCommand : SimpleCommand(
    owner = TouhouFreshmanCampRobot,
    "查询", "query"
) {

    @Handler
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.list(target: String) {
        val t = target.replace(Regex("\\D"), "").toLong()
        subject?.sendMessage(
            when (t) {
                superAdmin -> "${t}是管理员"
                in adminList -> "${t}是管理员"
                in whiteList -> "${t}是白名单"
                else -> "${t}不是白名单"
            }
        )
    }
}

object ListCommand : CompositeCommand(
    owner = TouhouFreshmanCampRobot,
    "查看", "list",
    description = "查看权限指令"
) {

    @SubCommand("白名单", "white")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.white() {
        val sender = fromEvent.sender.id
        subject?.sendMessage(
            if (sender in adminList || sender == superAdmin) {
                "白名单列表：\n" +
                        superAdmin + "\n" +
                        adminList.toString()
                        .replace(Regex("\\[|]"), "")
                        .replace(Regex(", *"), "\n") + "\n" +
                        whiteList.toString()
                            .replace(Regex("\\[|]"), "")
                            .replace(Regex(", *"), "\n")
            } else {
                "权限不足，@我 可以查看权限"
            }
        )
    }

    @SubCommand("管理员", "admin")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.admin() {
        subject?.sendMessage(
            "管理员列表：\n" +
                    superAdmin + "\n" +
                    adminList
                        .toString()
                        .replace(Regex("\\[|]"), "")
                        .replace(Regex(", *"), "\n")
        )
    }

}

object WhiteListCommand : CompositeCommand(
    owner = TouhouFreshmanCampRobot,
    "白名单", "white",
    description = "白名单指令"
) {

    @SubCommand("增加", "add")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.add(target: String) {
        val t = target.replace(Regex("\\D"), "").toLong()
        val sender = fromEvent.sender.id
        subject?.sendMessage(
            if (sender in adminList || sender == superAdmin) {
                when (t) {
                    in whiteList, in adminList -> "${t}已经是白名单"
                    sender -> "你不能增加你自己"
                    else -> {
                        whiteList.add(t)
                        "已增加白名单：${t}"
                    }
                }
            } else {
                "权限不足，@我 可以查看权限"
            }
        )
    }

    @SubCommand("删除", "remove")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.remove(target: String) {
        val t = target.replace(Regex("\\D"), "").toLong()
        val sender = fromEvent.sender.id
        subject?.sendMessage(
            if (sender in adminList || sender == superAdmin) {
                when (t) {
                    in whiteList, in adminList -> {
                        whiteList.remove(t)
                        "已删除白名单：${t}"
                    }
                    sender -> "你不能删除你自己"
                    else -> "${t}并不是白名单"
                }
            } else {
                "权限不足，@我 可以查看权限"
            }
        )
    }

}

object AdminListCommand : CompositeCommand(
    owner = TouhouFreshmanCampRobot,
    "管理员", "admin",
    description = "管理员指令"
) {

    @SubCommand("增加", "add")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.add(target: String) {
        val t = target.replace(Regex("\\D"), "").toLong()
        val sender = fromEvent.sender.id
        subject?.sendMessage(
            if (sender == superAdmin) {
                if (t in adminList) {
                    "${t}已经是管理员"
                } else {
                    adminList.add(t)
                    if (t in whiteList) {
                        whiteList.remove(t)
                    }
                    "已增加管理员：${t}"
                }
            } else {
                "权限不足，@我 可以查看权限"
            }
        )
    }

    @SubCommand("删除", "remove")
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.remove(target: String) {
        val t = target.replace(Regex("\\D"), "").toLong()
        val sender = fromEvent.sender.id
        subject?.sendMessage(
            if (sender in adminList || sender == superAdmin) {
                when (t) {
                    in adminList -> {
                        adminList.remove(t)
                        "已删除管理员：${t}"
                    }
                    sender -> "你不能删除你自己"
                    else -> "${t}并不是管理员"
                }
            } else {
                "权限不足，@我 可以查看权限"
            }
        )
    }

}