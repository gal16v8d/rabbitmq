package com.gsdd.rabbitmq.publishsubscribe;

import com.gsdd.rabbitmq.AbstractBrokerConfig;
import com.gsdd.rabbitmq.DestinationType;
import com.gsdd.rabbitmq.MessageProcessor;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class BrokerPublishSubscribe extends AbstractBrokerConfig {

  private static BrokerPublishSubscribe instance;

  private BrokerPublishSubscribe(String host) throws IOException, TimeoutException {
    super(host, DestinationType.EXCHANGE);
  }

  @Override
  public void defineDestination() throws IOException {
    getChannel().exchangeDeclare(DestinationType.EXCHANGE.getValue(), "fanout");
  }

  @Override
  public void sendMessage(String message) throws IOException {
    getChannel().basicPublish(DestinationType.EXCHANGE.getValue(), "", null, message.getBytes());
  }

  @Override
  public void receiveMessage() throws IOException {
    String queueName = getChannel().queueDeclare().getQueue();
    getChannel().queueBind(queueName, getDestinationType().getValue(), "");

    String queueName2 = getChannel().queueDeclare().getQueue();
    getChannel().queueBind(queueName2, getDestinationType().getValue(), "");

    MessageProcessor processor = new MessageProcessor(getChannel());
    getChannel().basicConsume(queueName, false, processor);
  }

  public static synchronized BrokerPublishSubscribe getInstance()
      throws IOException, TimeoutException {
    if (instance == null) {
      instance = new BrokerPublishSubscribe(RabbitConstants.HOST);
    }
    return instance;
  }
}
