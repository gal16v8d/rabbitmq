package com.gsdd.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.io.IOUtils;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;

@Getter
public abstract class AbstractBrokerConfig {

  private ConnectionFactory factory = new ConnectionFactory();
  private Connection connection;
  private Channel channel;
  private DestinationType destinationType;

  protected AbstractBrokerConfig(String host, DestinationType destinationType)
      throws IOException, TimeoutException {
    createConnections(host, destinationType);
  }

  public abstract void defineDestination() throws IOException;

  public abstract void sendMessage(String message) throws IOException;

  public abstract void receiveMessage() throws IOException;

  public final void createConnections(String host, DestinationType destinationType)
      throws IOException, TimeoutException {
    factory.setHost(host);
    factory.setUsername(RabbitConstants.USER);
    factory.setPassword(RabbitConstants.PASS);
    factory.setPort(RabbitConstants.PORT);
    factory.setAutomaticRecoveryEnabled(true);
    factory.setNetworkRecoveryInterval(RabbitConstants.RECOVERY_CHECK);
    factory.setConnectionTimeout(RabbitConstants.TIMEOUT);
    connection = factory.newConnection();
    channel = connection.createChannel();
    this.destinationType = destinationType;
    defineDestination();
  }

  public void closeConnections() throws IOException, TimeoutException {
    if (channel != null) {
      channel.close();
    }
    IOUtils.closeQuietly(connection);
  }

}
