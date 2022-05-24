package com.gsdd.rabbitmq;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

@Getter
@Setter
public class RabbitManager {

  private Channel channel;
  private Connection connection;
  private String endPointName;

  public RabbitManager(String endpointName) throws IOException, TimeoutException {
    this.endPointName = endpointName;
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(RabbitConstants.HOST);
    factory.setUsername(RabbitConstants.USER);
    factory.setPassword(RabbitConstants.PASS);
    factory.setPort(RabbitConstants.PORT);
    factory.setAutomaticRecoveryEnabled(true);
    factory.setNetworkRecoveryInterval(RabbitConstants.RECOVERY_CHECK);
    factory.setConnectionTimeout(RabbitConstants.TIMEOUT);
    connection = factory.newConnection();
    channel = connection.createChannel();
    // declaring a queue for this channel. If queue does not exist,
    // it will be created on the server.
    channel.queueDeclare(endpointName, false, false, true, null);
  }

  /** Close channel and connection. Not necessary as it happens implicitly any way. */
  public void close() {
    IOUtils.closeQuietly(this.connection);
  }
}
