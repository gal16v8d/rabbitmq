# rabbitmq

Basic sample of RabbitMQ usage.

For the sample you will need:

* Java 17
* Maven
* Docker

For get started with the docker image execute:

- docker pull rabbitmq
- docker run -d -p 15672:15672 -p 5672:5672 rabbitmq:3-management

You can check the page docker-ip:15672 and login with the default user

# Running the samples

## Basic
Run RabbitLauncher.java

## Point to Point
Run PtpRunner.java

## Publish Subscribe
Run PublishSubscribeRunner.java

## RPC
Run RabbitRPCServerLauncher.java then run RabbitRPCClientLauncher.java