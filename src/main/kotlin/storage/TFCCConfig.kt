package org.tfcc.bot.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.*

object TFCCConfig : AutoSavePluginConfig("TFCCConfig") {
    @Serializable
    class BilibiliConfig(
        /** B站用户名 */
        val username: String,

        /** 密码 */
        val password: String,

        /** B站ID */
        val mid: Int,

        @SerialName("room_id")
        /** B站直播间房间号 */
        val roomId: Int,

        @SerialName("area_v2")
        /** 直播分区 */
        val areaV2: Int,
    )

    @ValueDescription("B站相关配置")
    val bilibili: BilibiliConfig by value(
        BilibiliConfig(
            username = "13888888888",
            password = "12345678",
            mid = 12345678,
            roomId = 12345678,
            areaV2 = 236, // 236-主机游戏
        )
    )

    @Serializable
    class QQConfig(
        @SerialName("rand_count")
        /** 每天随符卡限制次数 */
        val randCount: Int,

        @SerialName("rand_one_time_limit")
        /** 单次随符卡的数量限制 */
        val randOneTimeLimit: Int,

        @SerialName("super_admin_qq")
        /** 主管理员QQ号 */
        val superAdminQQ: Long,

        @SerialName("qq_group")
        /** 主要功能的QQ群 */
        val qqGroup: LongArray,
    )

    @ValueDescription("QQ相关配置")
    val qq: QQConfig by value(
        QQConfig(
            randCount = 10,
            randOneTimeLimit = 20,
            superAdminQQ = 12345678,
            qqGroup = longArrayOf(12345678)
        )
    )

    fun isSuperAdmin(qq: Long) =
        qq == this.qq.superAdminQQ

    @Serializable
    class RepeaterInterruptionConfig(
        /** 打断复读功能限制的复读次数 */
        val allowance: Int,

        @SerialName("cool_down")
        /** 打断复读冷却时间（秒） */
        val coolDown: Long,

        @SerialName("qq_group")
        /** 打断复读的QQ群 */
        val qqGroup: LongArray,
    )

    @ValueName("repeater_interruption")
    @ValueDescription("打断复读相关配置")
    val repeaterInterruption: RepeaterInterruptionConfig by value(
        RepeaterInterruptionConfig(
            allowance = 5,
            coolDown = 3,
            qqGroup = longArrayOf(12345678)
        )
    )

    @Serializable
    class RandOperationConfig(
        /** 随机操作的次数 */
        val number: Int,
    )

    @ValueDescription("随机操作的次数配置")
    val randOperation: RandOperationConfig by value(
        RandOperationConfig(
            number = 10
        )
    )
}