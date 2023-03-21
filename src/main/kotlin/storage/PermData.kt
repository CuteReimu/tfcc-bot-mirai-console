package org.tfcc.bot.storage

import net.mamoe.mirai.console.data.*

object PermData : AutoSavePluginData("PermData") {
    @ValueDescription("管理员")
    var admin: List<Long> by value(listOf())

    @ValueName("white_list")
    @ValueDescription("白名单")
    var whiteList: List<Long> by value(listOf())

    fun isAdmin(qq: Long) =
        TFCCConfig.isSuperAdmin(qq) || qq in admin

    fun addAdmin(qq: Long): Boolean {
        synchronized(PermData) {
            if (qq in admin) return false
            admin += qq
            return true
        }
    }

    fun removeAdmin(qq: Long): Boolean {
        synchronized(PermData) {
            if (qq !in admin) return false
            admin -= qq
            return true
        }
    }

    fun listAdmin(): LongArray {
        synchronized(PermData) {
            return longArrayOf(TFCCConfig.qq.superAdminQQ, *admin.toLongArray())
        }
    }

    fun isWhitelist(qq: Long) =
        qq in whiteList

    fun addWhitelist(qq: Long): Boolean {
        synchronized(PermData) {
            if (qq in whiteList) return false
            whiteList += qq
            return true
        }
    }

    fun removeWhitelist(qq: Long): Boolean {
        synchronized(PermData) {
            if (qq !in whiteList) return false
            whiteList -= qq
            return true
        }
    }
}