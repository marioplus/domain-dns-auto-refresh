定时更新本机公网ip到aliyun dns解析

# 编译

工程基于`springboot 3.0`需要使用 `java17`才能运行，需要在`gradle.properties`中指定`JAVA_HOME`，或者删除该文件并在环境变量中配置正确的`JAVA_HOME`

```
gradle bootJar
```
编译好的`jar`包在`build/libs/`

# 运行

## 启动脚本

```bat
@echo off
start D:\Enviroment\Java\17\jdk-17.0.3.1\bin\javaw -jar update-domain-dns-0.0.1-SNAPSHOT.jar
exit
```

## 应用状态

依赖`actuator`,访问`http://localhost:18080/actuator`

## 关闭应用

同样依赖`actuator`

```bat
curl -X POST localhost:18080/actuator/shutdown
```

## 开机自启

脚本直接丢到启动文件夹就好`C:\Users\nokia\AppData\Roaming\Microsoft\Windows\Start Menu\Programs\Startup`启动脚本中的jar需要使用绝对路径

# 配置

需要在jar包同目录新建`application.yml`文件

```yml
# web端口
server:
  port: 18080
net.marioplus.domain-dns-auto-refresh:
  # 定时cron表达式，默认1分钟一次
  cron: 0 * * * * ?
  accounts:
      # 阿里云账号配置 https://next.api.aliyun.com/api/Alidns/2015-01-09/UpdateDomainRecord?params={}&tab=DEMO&lang=JAVA
    - access-key-id: xxx
      access-key-secret: xxxxx
      endpoint: alidns.cn-shenzhen.aliyuncs.com
      # 解析记录
      records:
          # 域名
        - domain-name: xxxx.net
          # 主机记录
          RR: xx.xx
          # 记录类型，只支持A/AAAA。默认:A
          type: A
          # 缓存时间。默认600,十分钟
          TTL: 600
        - domain-name: xxxx.net
          RR: xx.xx
          type: AAAA
```
