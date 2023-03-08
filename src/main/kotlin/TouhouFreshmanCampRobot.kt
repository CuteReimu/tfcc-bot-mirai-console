package org.tfcc.bot

import kotlinx.serialization.ExperimentalSerializationApi
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.tfcc.bot.bilibili.Live
import org.tfcc.bot.bilibili.data.BiliPluginConfig
import org.tfcc.bot.bilibili.init
import org.tfcc.bot.command.*


@ExperimentalSerializationApi
object TouhouFreshmanCampRobot : KotlinPlugin(
    JvmPluginDescription(
        id = "org.tfcc.bot.TouhouFreshmanCampRobot",
        version = "1.0.2",
    ) {
        author("EnkanSakura")
        info(
            """
            红裙BOT
        """.trimIndent()
        )
    }
) {

    var username: String = ""
    var password: String = ""
    var mid: Long = 0L
    var roomId: Long = 0L
    var area: Int = 0
    var sessData: String = ""
    var biliJct: String = ""
    var superAdmin: Long = 0L
    var adminList: MutableList<Long> = arrayListOf()
    var whiteList: MutableList<Long> = arrayListOf()

    val live: Live by lazy { Live() }

    override fun onEnable() {
        BiliPluginConfig.reload()
        init()
        HelpCommand.register()
        LiveCommand.register()
        QueryCommand.register()
        ListCommand.register()
        AdminListCommand.register()
        WhiteListCommand.register()
        GroupAtListener.start()

        logger.info { "TouhouFreshmanCampRobot loaded!" }
    }

    override fun onDisable() {
        HelpCommand.unregister()
        LiveCommand.unregister()
        QueryCommand.unregister()
        ListCommand.unregister()
        AdminListCommand.unregister()
        WhiteListCommand.unregister()
    }
}