package com.gsdd.rabbitmq.pointtopoint;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulatorProducer {

  public static void main(String[] args) throws IOException, TimeoutException {
    BrokerPTP ptp = BrokerPTP.getIntance();
    for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
      sendMessage(ptp, i);
    }
    ptp.closeConnections();
  }

  private static void sendMessage(BrokerPTP ptp, int i) {
    try {
      ptp.sendMessage("Rabbit BroadCast #:" + i);
    } catch (IOException e) {
      log.error("Error {}", e.getMessage(), e);
    }
  }
}
