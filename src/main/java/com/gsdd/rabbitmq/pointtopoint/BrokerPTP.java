package com.gsdd.rabbitmq.pointtopoint;

import com.gsdd.rabbitmq.AbstractBrokerConfig;
import com.gsdd.rabbitmq.DestinationType;
import com.gsdd.rabbitmq.MessageProcessor;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class BrokerPTP extends AbstractBrokerConfig {

  private static BrokerPTP instance;

  private BrokerPTP(String host) throws IOException, TimeoutException {
    super(host, DestinationType.QUEUE);
  }

  @Override
  public void defineDestination() throws IOException {
    getChannel().queueDeclare(DestinationType.QUEUE.getValue(), false, false, false, null);
  }

  @Override
  public void sendMessage(String message) throws IOException {
    getChannel().basicPublish("", DestinationType.QUEUE.getValue(), null, message.getBytes());
  }

  @Override
  public void receiveMessage() throws IOException {
    getChannel().basicQos(1);
    MessageProcessor procesador = new MessageProcessor(getChannel());
    getChannel().basicConsume(getDestinationType().getValue(), false, procesador);
  }

  public static synchronized BrokerPTP getIntance() throws IOException, TimeoutException {
    if (instance == null) {
      instance = new BrokerPTP(RabbitConstants.HOST);
    }
    return instance;
  }
}
