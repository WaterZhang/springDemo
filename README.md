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

### Log4j2 <a name="log4j2"></a>
> check log4j2.xml

### [OpenFeign](https://github.com/OpenFeign/feign) & [Hystrix](https://github.com/Netflix/Hystrix) <a name="openfeign"></a>
> add Transaction and Trace log
> Hystrix support Circuit breaker pattern
> Check Hystrix configuration [here](https://github.com/Netflix/Hystrix/wiki/Configuration).

### [PMD](https://pmd.github.io/) & [spotbugs](https://github.com/spotbugs/spotbugs) <a name="pmd"></a>
spotbugs is the spiritual successor of [FindBugs](https://github.com/findbugsproject/findbugs)

## just do it <a name="justdoit"></a>

### Code analysis <a name="codecheck"></a>
> ./gradlew check

### build <a name="cleanbuild"></a>
> ./gradlew clean build

### running <a name="running"></a>
> ./gradlew clean bootRun

### Test <a name="testing"></a>
> https://localhost:8443

