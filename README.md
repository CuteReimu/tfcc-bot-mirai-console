# 东方Project沙包聚集地机器人

这是东方Project沙包聚集地（以下简称“红群”）的机器人，基于Mirai Console编写

## 声明

* 本项目采用`AGPLv3`协议开源。同时**强烈建议**各位开发者遵循以下原则：
  * **任何间接接触本项目的软件也要求使用`AGPLv3`协议开源**
  * **不鼓励，不支持一切商业用途**
* **由于使用本项目提供的接口、文档等造成的不良影响和后果与本人和红群无关**
* 由于本项目的特殊性，可能随时停止开发或删档
* 本项目为开源项目，不接受任何的催单和索取行为

## 使用方法

在Release中下载`.jar`文件放入[mcl](https://github.com/iTXTech/mirai-console-loader) 的plugin文件夹内，运行mcl即可

注意: 使用前请确保可以[在聊天环境执行指令](https://github.com/project-mirai/chat-command)

带括号的`/`前缀是可选的

指令需要权限`org.tfcc.bot.touhoufreshmancamprobot:command.*`

## 配置文件：

第一次运行会自动生成配置文件`org.tfcc.bot.TouhouFreshmanCampRobot/BiliPluginConfig.yml`，如下：

```yaml
# 超级管理员
superAdmin: 123456  # 主管理员QQ号
# 管理员
adminList:          # 管理员QQ号，可在聊天环境中配置
  - 114514
# 白名单
whiteList:          # 白名单QQ号，可在聊天环境中配置
  - 222222
  - 333333
  - 444444
# 用户名
username: username  # B站用户名，暂无作用，可不填
# 密码
password: password  # B站密码，暂无作用，可不填
# 用户ID
mid: 0              # B站ID，暂无作用，可不填
# 房间号
roomId: 0           # B站直播间房号
# 直播分区
area: 236           # 直播分区，236-主机游戏
# SESSDATA
sessData: sessdata  # SESSDATA，请求认证用
# bili_jct
biliJct: bili_jct   # bili_jct，请求认证用
```

修改配置文件后重新启动即可

## 功能一览

- [x] 管理员、白名单
- [x] B站直播间管理（开播、下播、查询状态、修改标题）
