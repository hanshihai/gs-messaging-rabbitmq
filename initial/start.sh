#!/bin/bash

java -Dsun.misc.URLClassPath.disableJarChecking=true -Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Djava.io.tmpdir=./ -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -Djava.net.preferIPv6Addresses=false -jar ./messaging-rabbitmq-0.0.3-SNAPSHOT.jar --spring.profiles.active=local
