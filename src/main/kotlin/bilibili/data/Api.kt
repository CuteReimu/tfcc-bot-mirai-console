package org.tfcc.bot.bilibili.data

// Base
const val LIVE = "https://live.bilibili.com/"

// GET
const val GET_QRCODE = "https://passport.bilibili.com/qrcode/getLoginUrl"

// POST
const val LOGIN_WITH_QRCODE = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll?qrcode_key="

// GET
const val VIDEO_INFO = "https://api.bilibili.com/x/web-interface/view"

// GET
const val USER_VIDEOS = "https://api.bilibili.com/x/space/wbi/arc/search"

// Live
// GET  Para: room_id
const val LIVE_INFO = "https://api.live.bilibili.com/room/v1/Room/get_info?id="

// POST  Para: room_id, platform=pc, area_v2, csrf_token, csrf
const val START_LIVE = "https://api.live.bilibili.com/room/v1/Room/startLive"

// POST  Para: room_id, csrf
const val STOP_LIVE = "https://api.live.bilibili.com/room/v1/Room/stopLive"

// POST  Para: room_id, title, csrf
const val UPDATE_TITLE = "https://api.live.bilibili.com/room/v1/Room/update"

// GET
const val NAV = "https://api.bilibili.com/x/web-interface/nav"

// GET
const val AREA_LIST = "https://api.live.bilibili.com/room/v1/Area/getList"

fun LIVE_INFO(roomId: Int): String {
    return LIVE_INFO + roomId
}
