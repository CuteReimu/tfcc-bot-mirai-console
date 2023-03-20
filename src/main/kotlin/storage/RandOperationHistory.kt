package org.tfcc.bot.storage

import net.mamoe.mirai.console.data.*

object RandOperationHistory : AutoSavePluginData("RandOperationHistory") {
    @ValueName("history")
    @ValueDescription("随机操作历史记录")
    var history: MutableMap<Long, MutableList<String>> by value(mutableMapOf())

    fun addRecord(qq: Long, record: String) {
        synchronized(RandOperationHistory) {
            val copy = history.toMutableMap()
            if (qq !in history.keys)
                copy[qq] = mutableListOf(record)
            else
                copy[qq]!!.add(record)
            history = copy
        }
    }

    fun deleteRecord(qq: Long): Boolean {
        synchronized(RandOperationHistory) {
            val copy = history.toMutableMap()
            if (qq !in history.keys) {
                return false
            } else {
                copy.remove(qq)
                history = copy
                return true
            }
        }
    }

    fun getRecord(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            val result = history[qq] ?: return null
            return result.subList((result.size) - TFCCConfig.randOperation.limit, result.size)
        }
    }

    fun getAllRecords(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            return history[qq]
        }
    }
}
