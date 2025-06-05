package com.gsdd.rabbitmq.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RpcConsumerClientTest {

  private static final String EMPTY = "";
  private static final String TEST = "test";
  private static final String MSG = "msg";

  @Mock private Channel c;
  @Mock private Envelope e;
  @Mock private BlockingQueue<String> response;
  private RpcConsumerClient client;

  @BeforeEach
  void setUp() {
    client = new RpcConsumerClient(c, TEST, response);
  }

  @Test
  void handleDeliveryGoodCorrIdTest() {
    AMQP.BasicProperties properties =
        new AMQP.BasicProperties.Builder().correlationId(TEST).build();
    byte[] body = MSG.getBytes();
    client.handleDelivery(EMPTY, e, properties, body);
    Mockito.verify(response).offer(new String(body, StandardCharsets.UTF_8));
  }

  @Test
  void handleDeliveryBadCorrIdTest() {
    AMQP.BasicProperties properties =
        new AMQP.BasicProperties.Builder().correlationId("test2").build();
    byte[] body = MSG.getBytes();
    client.handleDelivery(EMPTY, e, properties, body);
    Mockito.verify(response, Mockito.never()).offer(new String(body, StandardCharsets.UTF_8));
  }
}
