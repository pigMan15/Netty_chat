# Netty 即时聊天系统



## 技术栈 

+ 前端 :  mui (Hbuilder构建)

+ 后端：SpringBoot + Mybatis + Netty + Aliyun OSS

+ 数据库：Mysql

  

## 学习点

+ 采用雪花算法生成分布式ID

+ Netty Reactor 主从多线程模型， 一组线程池接收请求， 一组线程池处理 IO ，型适用于高并发场景 

+ Netty 心跳处理及读写超时设置，定期检测某个通道是否空闲，如果空闲超过一定的时间，就可以将对应客户端的通道资源关闭。

+ 文件上传采用 阿里云OSS，便捷高效

  

## 实现业务

+ 好友业务： 好友查找（扫一扫），申请添加和发起会话

+ 聊天业务：文本消息发送，已读/未读消息状态标记

+ 用户个人业务： 注册登陆，个人信息修改

  



## 有待扩展

+ 朋友圈业务实现
+ 群聊业务



## 展示

![拼接01](C:\Users\pigMan\Desktop\netty\result\拼接01.jpg)

![拼接02](C:\Users\pigMan\Desktop\netty\result\拼接02.jpg)



## 体验版下载

