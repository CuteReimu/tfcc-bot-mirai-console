package org.tfcc.bot.storage

import net.mamoe.mirai.console.data.*

object RandOperationHistory : AutoSavePluginData("RandOperationHistory") {
    @ValueName("history")
    @ValueDescription("随机操作历史记录")
    var history: MutableMap<Long, MutableList<String>> by value(mutableMapOf())

    fun addRecord(qq: Long, record: String) {
        synchronized(RandOperationHistory) {
            if (qq !in history.keys) {
                history[qq] = mutableListOf(record)
            } else {
                history[qq]!!.add(record)
            }
        }
    }

    fun deleteRecord(qq: Long): Boolean {
        synchronized(RandOperationHistory) {
            if (qq !in history.keys) {
                return false
            } else {
                history.remove(qq)
                return true
            }
        }
    }

    fun clearRecords() {
        synchronized(RandOperationHistory) {
            history.clear()
        }
    }

    fun getRecord(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            return history[qq]?.toList()
        }
    }

    fun getAllRecords(): List<String> {
        synchronized(RandOperationHistory) {
            val result = mutableListOf<String>()
            for ((qq, records) in history) {
                result.add("$qq:")
                result.addAll(records)
            }
            return result
        }
    }
}