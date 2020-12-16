# 目录
1. [介绍](#introduction)
2. [JDK版本](#jdk)
3. [Gradlew版本](#gradlew)
4. [HTTPS支持](#https)

# SpringDemo <a name="introduction"></a>
build SpringBoot as Web APP or Micro Service Template.

## JDK <a name="jdk"></a>
> adoptopenjdk 11

## upgrade gradlew <a name="gradlew"></a>
> ./gradlew wrapper --gradle-version 6.4 --distribution-type all

## HTTPS support <a name="https"></a>
> keytool -genkeypair -alias zzm -keyalg RSA -keysize 2048 -keystore zzm.jks 

## Log4j2
> check log4j2.xml

## Integrate [OpenFeign](https://github.com/OpenFeign/feign) & [Hystrix](https://github.com/Netflix/Hystrix)
> add Transaction and Trace log
> Hystrix support Circuit breaker pattern
> Check Hystrix configuration [here](https://github.com/Netflix/Hystrix/wiki/Configuration).

## Support [PMD](https://pmd.github.io/) & [spotbugs](https://github.com/spotbugs/spotbugs) 
spotbugs is the spiritual successor of [FindBugs](https://github.com/findbugsproject/findbugs)

## Code check
> ./gradlew check

## build
> ./gradlew clean build

## running
> ./gradlew clean bootRun

## Test
> https://localhost:8443

