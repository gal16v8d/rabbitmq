package com.gsdd.rabbitmq;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProcessor extends DefaultConsumer {

  private Channel channel;

  public MessageProcessor(Channel channel) {
    super(channel);
    this.channel = channel;
  }

  @Override
  public void handleDelivery(
      String consumerTag, Envelope envelop, BasicProperties basicProp, byte[] body)
      throws IOException {
    String message = "";
    try {
      message = new String(body, StandardCharsets.UTF_8);
      log.info("[x] Message received '{}'", message);
      Thread.sleep(RabbitConstants.WAIT_TIME);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      log.info("[x] Done ");
      channel.basicAck(envelop.getDeliveryTag(), false);
    }
  }
}
