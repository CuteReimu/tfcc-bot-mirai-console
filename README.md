<div align="center">

# 东方Project沙包聚集地机器人

![](https://img.shields.io/github/languages/top/CuteReimu/tfcc-bot-mirai-console "语言")
[![](https://img.shields.io/github/actions/workflow/status/CuteReimu/tfcc-bot-mirai-console/build.yml?branch=main)](https://github.com/CuteReimu/tfcc-bot-mirai-console/actions/workflows/golangci-lint.yml "代码分析")
[![](https://img.shields.io/github/contributors/CuteReimu/tfcc-bot-mirai-console)](https://github.com/CuteReimu/tfcc-bot-mirai-console/graphs/contributors "贡献者")
[![](https://img.shields.io/github/license/CuteReimu/tfcc-bot-mirai-console)](https://github.com/CuteReimu/tfcc-bot-mirai-console/blob/master/LICENSE "许可协议")
</div>

这是东方Project沙包聚集地（以下简称“红群”）的机器人，基于 [Mirai](https://github.com/mamoe/mirai) 编写

## 声明

* 本项目采用`AGPLv3`协议开源。同时**强烈建议**各位开发者遵循以下原则：
    * **任何间接接触本项目的软件也要求使用`AGPLv3`协议开源**
    * **不鼓励，不支持一切商业用途**
* **由于使用本项目提供的接口、文档等造成的不良影响和后果与本人和红群无关**
* 由于本项目的特殊性，可能随时停止开发或删档
* 本项目为开源项目，不接受任何的催单和索取行为

## 编译

```shell
./gradlew buildPlugin
```

在`build/mirai`下可以找到编译好的jar包，即为Mirai插件

## 使用方法

1. 首先了解、安装并启动 [Mirai - Console Terminal](https://github.com/mamoe/mirai/blob/dev/docs/ConsoleTerminal.md) 。
   如有必要，你可能需要修改 `config/Console` 下的 Mirai 相关配置。
   **QQ登录、收发消息相关全部使用 Mirai 框架，若一步出现了问题，请去Mirai的repo或者社区寻找解决方案。**
2. 启动Mirai后，会发现生成了很多文件夹。将编译得到的插件jar包放入 `plugins` 文件夹后，重启Mirai。

## 配置文件：

第一次运行会自动生成配置文件`config/org.tfcc.bot/TFCCConfig.yml`，如下：

```yaml
bilibili:
  area_v2: "236"           # 直播分区，236-主机游戏
  mid: "12345678"          # B站ID
  password: "12345678"     # 密码
  room_id: "12345678"      # B站直播间房间号
  username: "13888888888"  # B站用户名
qq:
  rand_count: 10            # 每天随符卡限制次数
  rand_one_time_limit: 20   # 单次随符卡的数量限制
  super_admin_qq: 12345678  # 主管理员QQ号
  qq_group: # 主要功能的QQ群
    - 12345678
```

修改配置文件后重新启动即可

## 功能一览

- [x] 管理员、白名单
- [x] B站开播、修改直播标题、查询直播状态
- [ ] 随作品、随机体
- [ ] B站视频解析
- [ ] B站视频推送
- [ ] 投票
- [ ] 查新闻
- [ ] 增加预约功能
- [ ] 查询分数表
- [ ] 打断复读
- [ ] 随符卡
- [ ] rep解析
