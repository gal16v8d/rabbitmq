package com.gsdd.rabbitmq.pointtopoint;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulatorConsumer {

  public static void main(String[] args) {
    try {
      BrokerPTP.getIntance().receiveMessage();
    } catch (IOException | TimeoutException e) {
      log.error("Error: {}", e.getMessage(), e);
    }
  }

}
