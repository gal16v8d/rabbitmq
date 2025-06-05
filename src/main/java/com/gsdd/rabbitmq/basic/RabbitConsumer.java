package com.gsdd.rabbitmq.basic;

import com.gsdd.rabbitmq.RabbitManager;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import com.gsdd.rabbitmq.models.MessageRecord;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

@Slf4j
public class RabbitConsumer extends RabbitManager implements Runnable, Consumer {

  public RabbitConsumer(String endPointName) throws IOException, TimeoutException {
    super(endPointName);
  }

  @Override
  public void run() {
    try {
      // start consuming messages. Auto acknowledge messages.
      getChannel().basicConsume(getEndPointName(), true, this);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  /** Called when consumer is registered. */
  @Override
  public void handleConsumeOk(String consumerTag) {
    log.info("Consumer: {} registered", consumerTag);
  }

  /** Called when new message is available. */
  @Override
  public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body) {
    MessageRecord message = SerializationUtils.deserialize(body);
    log.info("Message # {} received.", message.messageNumber());
  }

  @Override
  public void handleCancel(String consumerTag) {
    throw new UnsupportedOperationException(RabbitConstants.NOT_IMPLEMENTED);
  }

  @Override
  public void handleCancelOk(String consumerTag) {
    throw new UnsupportedOperationException(RabbitConstants.NOT_IMPLEMENTED);
  }

  @Override
  public void handleRecoverOk(String consumerTag) {
    throw new UnsupportedOperationException(RabbitConstants.NOT_IMPLEMENTED);
  }

  @Override
  public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
    log.info("RabbitMQ for consumerTag {} is shutdown now", consumerTag);
  }
}
