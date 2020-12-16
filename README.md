# 目录
1. [介绍](#introduction)
2. [JDK版本](#jdk)
3. [Gradlew版本](#gradlew)
4. [HTTPS支持](#https)
5. [引用框架](#frameworks)
    1. [Log](#log4j2)
    2. [OpenFeign & Hystrix](#openfeign)
    3. [PMD & Spotbugs](#pmd)
6. [光说不练假把式](#justdoit)
    1. [代码分析](#codecheck)
    2. [build](#cleanbuild)
    3. [running](#running)
    4. [测试](#testing)

# SpringDemo <a name="introduction"></a>
基于SpringBoot构架Web APP以及微服务模板。
> 本人使用Macbook Pro,习惯在命令行编译运行项目，提出来的命令都是在此基础上。
> 如果是Windows用户或者习惯IDE的同学要做相应的调整。

## JDK <a name="jdk"></a>
本地环境只需要安装JDK环境即可。
> adoptopenjdk 11

## upgrade gradlew <a name="gradlew"></a>
本地不需要安装Gradle，如果需要，可以升级gradlew版本，当前使用的版本是6.4。
> ./gradlew wrapper --gradle-version 6.4 --distribution-type all

## HTTPS support <a name="https"></a>
使用JDK自带的keytool创建keystore证书，支持HTTPS。真实环境需要第三方认证的证书。
> keytool -genkeypair -alias zzm -keyalg RSA -keysize 2048 -keystore zzm.jks 

## import third-part framework and build your future <a name="frameworks"></a>
这里列举了重要的第三方框架，并不是所有的技术，详情要参考具体的代码。

### Log4j2 <a name="log4j2"></a>
日志记录采用Log4j2，可以自己定制。我这里结合了Hystrix，创建了Transaction Log和Trace Log。
* check src/main/resources/log4j2.xml
* Transaction Log用来记录APP与downstream service之间的HTTP Call.
* Trace Log用来记录具体的HTTP request 和 response
* 自己还可以根据不同的运行环境(比如dev, test and prod)，定制Log Level.

### [OpenFeign](https://github.com/OpenFeign/feign) & [Hystrix](https://github.com/Netflix/Hystrix) <a name="openfeign"></a>
* add Transaction and Trace log
* Hystrix support Circuit breaker pattern
* Check Hystrix configuration [here](https://github.com/Netflix/Hystrix/wiki/Configuration).

### [PMD](https://pmd.github.io/) & [spotbugs](https://github.com/spotbugs/spotbugs) <a name="pmd"></a>
静态代码分析工具，引用比较流行的PMD和Spotbugs。其中Spotbugs是[FindBugs](https://github.com/findbugsproject/findbugs)的替代接班者。

## just do it <a name="justdoit"></a>
**个人习惯在命令行中编译运行项目，在Windows下使用，要注意语法的区别。**

### Code analysis <a name="codecheck"></a>
检查代码有没有格式问题以及潜在的bug
> ./gradlew check

### build <a name="cleanbuild"></a>
命令行执行构建命令
> ./gradlew clean build

### running <a name="running"></a>
命令行运行
> ./gradlew clean bootRun

### Test <a name="testing"></a>
本地访问
> https://localhost:8443

