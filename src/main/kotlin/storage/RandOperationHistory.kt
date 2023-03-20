package org.tfcc.bot.storage

import net.mamoe.mirai.console.data.*
import org.tfcc.bot.PluginMain.save

object RandOperationHistory : AutoSavePluginData("RandOperationHistory") {
    @ValueName("history")
    @ValueDescription("随机操作历史记录")
    var history: MutableMap<Long, MutableList<String>> by value(mutableMapOf())

    fun addRecord(qq: Long, record: String) {
        synchronized(RandOperationHistory) {
            if (qq !in history.keys)
                history[qq] = mutableListOf(record)
            else
                history[qq]!!.add(record)
            RandOperationHistory.save()
        }
    }

    fun deleteRecord(qq: Long): Boolean {
        synchronized(RandOperationHistory) {
            if (qq !in history.keys) {
                return false
            } else {
                history.remove(qq)
                RandOperationHistory.save()
                return true
            }
        }
    }

    fun clearRecords() {
        synchronized(RandOperationHistory) {
            history = mutableMapOf()
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
