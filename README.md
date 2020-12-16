# springDemo
build spring boot APP, I will start to integrate other frameworks in this project.

## JDK
> adoptopenjdk 11

## upgrade gradlew 
> ./gradlew wrapper --gradle-version 6.4 --distribution-type all

## HTTPS support
> keytool -genkeypair -alias zzm -keyalg RSA -keysize 2048 -keystore zzm.jks 

## Log4j2
> check log4j2.xml

## Integrate [OpenFeign](https://github.com/OpenFeign/feign)
> add Transaction and Trace log

## [Hystrix](https://github.com/Netflix/Hystrix) support Circuit breaker pattern
> Check hystrix configuration [here](https://github.com/Netflix/Hystrix/wiki/Configuration).

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

