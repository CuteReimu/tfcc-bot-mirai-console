package org.tfcc.bot

import net.mamoe.mirai.console.data.*

object ChatCommandConfig : AutoSavePluginConfig("ChatCommand") {
    @ValueDescription("插件是否启用. 设置 false 时禁用插件.")
    val enabled: Boolean by value(true)
}