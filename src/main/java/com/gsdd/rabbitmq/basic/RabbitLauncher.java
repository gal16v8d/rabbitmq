package com.gsdd.rabbitmq.basic;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import com.gsdd.rabbitmq.models.MessageRecord;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitLauncher {

  private static RabbitConsumer consumer;
  private static RabbitProducer producer;

  public static void main(String[] args) throws Exception {
    initConsumer();
    initProducer();
    finalizeRabbit();
  }

  private static void initConsumer() throws IOException, TimeoutException {
    consumer = new RabbitConsumer(RabbitConstants.COMMON_QUEUE);
    Thread consumerThread = new Thread(consumer);
    consumerThread.start();
  }

  private static void initProducer() throws IOException, TimeoutException {
    producer = new RabbitProducer(RabbitConstants.COMMON_QUEUE);

    for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
      producer.sendMessage(new MessageRecord(i));
      log.info("Message # {} sent.", i);
    }
  }

  private static void finalizeRabbit() {
    consumer.close();
    producer.close();
  }
}
