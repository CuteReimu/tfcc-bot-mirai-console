package org.tfcc.bot.bilibili.data

// Base
const val LIVE = "https://live.bilibili.com/"

// GET
const val HUMAN_VERIFY = "https://passport.bilibili.com/x/passport-login/captcha?source=main_web"

// GET
const val GET_KEY = "https://passport.bilibili.com/login?act=getkey"

// POST  Para: captchaType=6, username, password, keep=true,
//             key, challenge, validate, seccode
const val LOGIN = "http://passport.bilibili.com/web/login/v2"

// GET
const val GET_QRCODE = "https://passport.bilibili.com/qrcode/getLoginUrl"

// POST
const val LOGIN_WITH_QRCODE = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll?qrcode_key="

// Live
// GET  Para: room_id
const val LIVE_INFO = "https://api.live.bilibili.com/room/v1/Room/get_info?id="

// POST  Para: room_id, platform=pc, area_v2, csrf_token, csrf
const val START_LIVE = "https://api.live.bilibili.com/room/v1/Room/startLive"

// POST  Para: room_id, csrf
const val STOP_LIVE = "https://api.live.bilibili.com/room/v1/Room/stopLive"

// POST  Para: room_id, title, csrf
const val UPDATE_TITLE = "https://api.live.bilibili.com/room/v1/Room/update"


fun LIVE_INFO(roomId: Int): String {
    return LIVE_INFO + roomId
}
