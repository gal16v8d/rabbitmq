package com.gsdd.rabbitmq.rpc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gsdd.rabbitmq.rpc.RPCConsumerClient;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

@ExtendWith(MockitoExtension.class)
class RPCConsumerClientTest {

  private static final String EMPTY = "";
  private static final String TEST = "test";
  private static final String MSG = "msg";

  @Mock
  private Channel c;
  @Mock
  private Envelope e;
  @Mock
  private BlockingQueue<String> response;
  private RPCConsumerClient client;

  @BeforeEach
  void setUp() {
    client = new RPCConsumerClient(c, TEST, response);
  }

  @Test
  void handleDeliveryGoodCorrIdTest() throws IOException {
    AMQP.BasicProperties properties =
        new AMQP.BasicProperties.Builder().correlationId(TEST).build();
    byte[] body = MSG.getBytes();
    client.handleDelivery(EMPTY, e, properties, body);
    Mockito.verify(response).offer(new String(body, StandardCharsets.UTF_8));
  }

  @Test
  void handleDeliveryBadCorrIdTest() throws IOException {
    AMQP.BasicProperties properties =
        new AMQP.BasicProperties.Builder().correlationId("test2").build();
    byte[] body = MSG.getBytes();
    client.handleDelivery(EMPTY, e, properties, body);
    Mockito.verify(response, Mockito.never()).offer(new String(body, StandardCharsets.UTF_8));
  }
}
