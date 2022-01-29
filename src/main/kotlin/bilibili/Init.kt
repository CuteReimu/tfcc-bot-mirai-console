package top.enkansakura.bilibili

import top.enkansakura.TouhouFreshmanCampRobot
import  top.enkansakura.TouhouFreshmanCampRobot.username
import  top.enkansakura.TouhouFreshmanCampRobot.password
import  top.enkansakura.TouhouFreshmanCampRobot.mid
import  top.enkansakura.TouhouFreshmanCampRobot.roomId
import  top.enkansakura.TouhouFreshmanCampRobot.area
import  top.enkansakura.TouhouFreshmanCampRobot.sessData
import  top.enkansakura.TouhouFreshmanCampRobot.biliJct
import  top.enkansakura.TouhouFreshmanCampRobot.adminList
import  top.enkansakura.TouhouFreshmanCampRobot.whiteList
import  top.enkansakura.TouhouFreshmanCampRobot.live
import top.enkansakura.TouhouFreshmanCampRobot.superAdmin
import top.enkansakura.bilibili.data.BiliPluginConfig

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
