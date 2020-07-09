# rabbitmq

Basic sample of RabbitMQ usage.

For the sample you will need:

* Java 8
* Maven
* Docker

For get started with the docker image execute:

docker pull rabbitmq
docker run -d -p 15672:15672 -p 5672:5672 rabbitmq:3-management
You can check the page docker-ip:15672 and login with the default user

# Running the samples

## Point to Point
Run SimulatorConsumer.java then run SimulatorProducer.java

## Publish Subscribe
Run SimulatorSubscriber.java then run SimulatorPublisher.java

## RPC
Run RabbitRPCServerLauncher.java then run RabbitRPCClientLauncher.java