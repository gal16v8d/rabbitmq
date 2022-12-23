package com.gsdd.rabbitmq.pointtopoint;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PtpRunner {
  
  public static void main(String[] args) throws IOException, TimeoutException {
    BrokerPtp ptp = BrokerPtp.getIntance();
    initConsumer(ptp);
    initProducer(ptp);
    try {
      // Delay for avoid to close the connection 
      // before consumer ends its process
      Thread.sleep(45000);
      ptp.closeConnections();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
  
  private static void initConsumer(BrokerPtp ptp) {
    try {
      ptp.receiveMessage();
    } catch (IOException e) {
      log.error("Error: {}", e.getMessage(), e);
    }
  }
  
  private static void initProducer(BrokerPtp ptp) {
    for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
      sendMessage(ptp, i);
    }
  }

  private static void sendMessage(BrokerPtp ptp, int i) {
    try {
      ptp.sendMessage("Rabbit BroadCast #:" + i);
    } catch (IOException e) {
      log.error("Error {}", e.getMessage(), e);
    }
  }

}
