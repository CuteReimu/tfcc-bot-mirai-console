package top.enkansakura

import kotlinx.serialization.ExperimentalSerializationApi
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import top.enkansakura.TouhouFreshmanCampRobot.adminList
import top.enkansakura.TouhouFreshmanCampRobot.superAdmin
import top.enkansakura.TouhouFreshmanCampRobot.whiteList

@ExperimentalSerializationApi
object GroupAtListener : Service() {

    override suspend fun main() {

        GlobalEventChannel
            .parentScope(TouhouFreshmanCampRobot)
            .context(TouhouFreshmanCampRobot.coroutineContext)
            .subscribeGroupMessages {
                atBot {
                    subject.sendMessage(buildMessageChain {
                        +At(sender.id)
                        +PlainText("\n你可以使用以下功能：\n")
                        +PlainText(
                            when (sender.id)
                            {
                                superAdmin ->   "/直播 开始\n/直播 关闭\n/直播 状态\n/直播 修改标题<标题>\n/查询 <QQ号|At>\n/查看 白名单\n/查看 管理员\n/白名单 增加<QQ号|At>\n/白名单 删除<QQ号|At>\n/管理员 增加<QQ号|At>\n/管理员 删除<QQ号|At>"
                                in adminList -> "/直播 开始\n/直播 关闭\n/直播 状态\n/直播 修改标题<标题>\n/查询 <QQ号|At>\n/查看 白名单\n/查看 管理员\n/白名单 增加<QQ号|At>\n/白名单 删除<QQ号|At>"
                                in whiteList -> "/直播 开始\n/直播 关闭\n/直播 状态\n/直播 修改标题<标题>\n/查询 <QQ号|At>\n/查看 管理员"
                                else ->         "/直播 状态\n/查询 <QQ号|At>\n/查看 管理员"
                            }
                        )
                    })
                }

            }
    }

}
