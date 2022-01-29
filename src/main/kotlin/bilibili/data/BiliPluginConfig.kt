package top.enkansakura.bilibili.data

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object BiliPluginConfig : AutoSavePluginConfig("BiliPluginConfig") {

    @ValueDescription("超级管理员")
    val superAdmin: Long by value(123456L)

    @ValueDescription("管理员")
    val adminList: MutableList<Long> by value(arrayListOf(114514L))

    @ValueDescription("白名单")
    val whiteList: MutableList<Long> by value(arrayListOf(222222L, 333333L, 444444L))

    @ValueDescription("用户名")
    val username: String by value("username")

    @ValueDescription("密码")
    val password: String by value("password")

    @ValueDescription("用户ID")
    val mid: Long by value(0L)

    @ValueDescription("房间号")
    val roomId: Long by value(0L)

    @ValueDescription("直播分区")
    val area: Int by value(236)

    @ValueDescription("SESSDATA")
    val sessData: String by value("sessdata")

    @ValueDescription("bili_jct")
    val biliJct: String by value("bili_jct")

}
