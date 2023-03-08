package org.tfcc.bot.bilibili

import org.tfcc.bot.TouhouFreshmanCampRobot
import org.tfcc.bot.bilibili.data.*
import org.tfcc.bot.utils.HttpUtils
import org.tfcc.bot.utils.decode


internal val logger by TouhouFreshmanCampRobot::logger

class Live() {

    private val roomId: Long = TouhouFreshmanCampRobot.roomId
    private val sessData: String = TouhouFreshmanCampRobot.sessData
    private val biliJct: String = TouhouFreshmanCampRobot.biliJct

    private val httpUtils: HttpUtils by lazy { HttpUtils() }

    private var rtmpAddr: String = ""
    private var rtmpCode: String = ""

    fun liveStatus(): String {
        val liveInfo = httpUtils.getAndDecode<LiveInfo>(LIVE_INFO(roomId))

        return if (liveInfo.live_status == 0) {
            "直播间状态：未开播"
        } else {
            "直播间状态：开播\n直播标题：${liveInfo.title}\n人气：${liveInfo.online}\n直播间地址：${LIVE + roomId.toString()}"
        }
    }

    fun startLive(): String {
        val postBody = "room_id=${roomId}&platform=pc&area_v2=236&csrf_token=${biliJct}&csrf=${biliJct}"
        val liveStartData = httpUtils.postAndDecode<LiveStartData>(START_LIVE, postBody)

        rtmpAddr = liveStartData.rtmp.addr
        rtmpCode = liveStartData.rtmp.code

        return if (liveStartData.change == 0) {
            "直播间本来就是开启的，推流码已私聊\n直播间地址：${LIVE + roomId}\n快来围观吧！"
        } else {
            "直播间已开启，推流码已私聊，别忘了修改直播间标题哦！\n直播间地址：${LIVE + roomId}\n快来围观吧！"
        }
    }

    fun stopLive(): String {
        val postBody = "room_id=${roomId}&csrf=${biliJct}"
        val liveStopData = httpUtils.postAndDecode<LiveStopData>(STOP_LIVE, postBody)

        return if (liveStopData.change == 0) {
            "直播间本来就是关闭的"
        } else {
            "直播间已关闭"
        }
    }

    fun updateTitle(title: String): String {
        val postBody = "room_id=${roomId}&title=${title}&csrf=${biliJct}"
        val resp = httpUtils.post(UPDATE_TITLE, postBody).decode<ResultData>()

        return if (resp.code == 0) {
            "直播间标题已修改为：${title}"
        } else {
            "修改直播间标题失败，请联系管理员"
        }
    }

    fun getRtmpMsg(): String {
        return "服务器地址：${rtmpAddr}\n\n串流密钥：${rtmpCode}"
    }

}