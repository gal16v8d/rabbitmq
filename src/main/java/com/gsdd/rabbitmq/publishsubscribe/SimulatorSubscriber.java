package com.gsdd.rabbitmq.publishsubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulatorSubscriber {

  public static void main(String[] args) throws IOException, TimeoutException {
    try {
      BrokerPublishSubscribe.getIntance().receiveMessage();
    } catch (IOException | TimeoutException e) {
      log.error("Error {}", e.getMessage(), e);
    }
  }

}
