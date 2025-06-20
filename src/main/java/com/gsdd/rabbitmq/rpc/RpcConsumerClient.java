package com.gsdd.rabbitmq.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcConsumerClient extends DefaultConsumer {

  private final String corrId;
  private final BlockingQueue<String> response;

  public RpcConsumerClient(Channel channel, String corrId, BlockingQueue<String> response) {
    super(channel);
    this.corrId = corrId;
    this.response = response;
  }

  @Override
  public void handleDelivery(
      String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
    if (properties.getCorrelationId().equals(corrId)) {
      response.offer(new String(body, StandardCharsets.UTF_8));
    } else {
      log.error("Bad correlation id");
    }
  }
}
