package com.gsdd.rabbitmq.publishsubscribe;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublishSubscribeRunner {

  public static void main(String[] args) throws IOException, TimeoutException {
    BrokerPublishSubscribe broker = BrokerPublishSubscribe.getIntance();
    initSubscriber(broker);
    initPublisher(broker);
    try {
      // Delay for avoid to close the connection
      // before consumer ends its process
      Thread.sleep(45000);
      broker.closeConnections();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private static void initSubscriber(BrokerPublishSubscribe broker) {
    try {
      broker.receiveMessage();
    } catch (IOException e) {
      log.error("Error {}", e.getMessage(), e);
    }
  }

  private static void initPublisher(BrokerPublishSubscribe broker) {
    for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
      sendMessage(broker, i);
    }
  }

  private static void sendMessage(BrokerPublishSubscribe broker, int i) {
    try {
      broker.sendMessage("Rabbit BroadCast #:" + i);
    } catch (IOException e) {
      log.error("Error {}", e.getMessage(), e);
    }
  }
}
