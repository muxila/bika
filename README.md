代码很烂，功能还不全 <br>
UI采用MD3, 项目框架使用的是[wzqjava/MVVMSmart](https://github.com/wzqjava/MVVMSmart)， 个人能力有限，没完全理解mvvm，让我改的稀巴烂。<br>
官方app的图片会重复加载，我的修改了缓存key，加载更快更省流量 <br>

#感谢

[wzqjava/MVVMSmart](https://github.com/wzqjava/MVVMSmart) <br>
[material-components/material-components-android](https://github.com/material-components/material-components-android) <br>
[youlookwhat/ByRecyclerView](https://github.com/youlookwhat/ByRecyclerView) <br>
[square/retrofit](https://github.com/square/retrofit) <br>
[bumptech/glide](https://github.com/bumptech/glide) <br>
[LuckSiege/PictureSelector](https://github.com/LuckSiege/PictureSelector) <br>

#截图

<img src="https://raw.githubusercontent.com/muxila/BIKA/master/Screenshot/s1.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s2.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s3.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s4.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s5.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s6.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s7.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s8.webp" width="216" height="480"><img src="https://raw.githubusercontent.com/shizq123/BIKA/master/Screenshot/s9.webp" width="216" height="480">

#未完成

    开发app的目标 apk安装包要小 图片加载不卡顿 应用不闪退 功能要齐全

    修改个人信息
    漫画详情章节观看记录
    漫画详情章节图片观看记录
    漫画阅读器
    聊天室发送语音
    子评论页加进度条
    评论举报

    未来添加下载
    未来适配平板
    未来添加漫画下载
    添加转场动画

#待优化

    注册页面 需要优化 跳转到登录页要显示注册的账号密码
    主页会列表重复
    Toast,图片,UI
    应用加载时间
    漫画详情页面
    回退键
    设置页太乱
    历史记录需要优化
    更改密码 需要添加进度条
    列表滑动有卡顿 后续优化
    优化activity 转成fragment
    文本复制功能 漫画评论复制
    加个图片选择器apk增加了1M :(
    上传头像的锯齿较多不清晰
    搜索框

#服务器状态

    400 1019 cannot comment 无法发表评论
    400 1031 higher level is required 评论等级不够
    400 1002 avatar invalid base64 image 无效的base64图像
    400 1004 账号密码错误
    400 1008 email is already exist 邮箱已存在
    400 1009 name is already exist 名称已存在
    400 1014 漫画审核中
    401 1005 unauthorized token过期
    404 1007 not found 找不到数据
    413 request entity too large 图片上传太大
    500 :( -- 

#聊天室 webSocket

    0 连接成功 
    2 心跳包 服务器会回复 3
    40 连接成功
    41 关闭聊天
    42 消息
    1000 超时
