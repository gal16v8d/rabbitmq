package com.gsdd.rabbitmq.publishsubscribe;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulatorPublisher {

  public static void main(String[] args) throws IOException, TimeoutException {
    BrokerPublishSubscribe publishSubscribe = BrokerPublishSubscribe.getIntance();
    for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
      sendMessage(publishSubscribe, i);
    }
    publishSubscribe.closeConnections();
  }

  private static void sendMessage(BrokerPublishSubscribe publishSubscribe, int i) {
    try {
      publishSubscribe.sendMessage("Rabbit BroadCast #:" + i);
    } catch (IOException e) {
      log.error("Error {}", e.getMessage(), e);
    }
  }
}
