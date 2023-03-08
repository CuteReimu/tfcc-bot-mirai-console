package org.tfcc.bot.bilibili

import org.tfcc.bot.TouhouFreshmanCampRobot
import org.tfcc.bot.TouhouFreshmanCampRobot.adminList
import org.tfcc.bot.TouhouFreshmanCampRobot.area
import org.tfcc.bot.TouhouFreshmanCampRobot.biliJct
import org.tfcc.bot.TouhouFreshmanCampRobot.mid
import org.tfcc.bot.TouhouFreshmanCampRobot.password
import org.tfcc.bot.TouhouFreshmanCampRobot.roomId
import org.tfcc.bot.TouhouFreshmanCampRobot.sessData
import org.tfcc.bot.TouhouFreshmanCampRobot.superAdmin
import org.tfcc.bot.TouhouFreshmanCampRobot.username
import org.tfcc.bot.TouhouFreshmanCampRobot.whiteList
import org.tfcc.bot.bilibili.data.BiliPluginConfig

fun init() {
    username = BiliPluginConfig.username
    password = BiliPluginConfig.password
    mid = BiliPluginConfig.mid
    roomId = BiliPluginConfig.roomId
    area = BiliPluginConfig.area
    sessData = "SESSDATA=${BiliPluginConfig.sessData}"
    biliJct = BiliPluginConfig.biliJct
    superAdmin = BiliPluginConfig.superAdmin
    adminList = BiliPluginConfig.adminList
    whiteList = BiliPluginConfig.whiteList


    if (sessData == "" || biliJct == "") {
        TouhouFreshmanCampRobot.logger.error("Cookie错误！")
    }
}
