package com.gsdd.rabbitmq.rpc;

import com.gsdd.rabbitmq.RabbitManager;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import com.rabbitmq.client.AMQP;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import lombok.Getter;

@Getter
public class RpcClient {

  private final RabbitManager manager;
  private final String replyQueueName;

  public RpcClient() throws IOException, TimeoutException {
    manager = new RabbitManager(RabbitConstants.RPC_SEND_QUEUE);
    replyQueueName =
        manager
            .getChannel()
            .queueDeclare(RabbitConstants.RPC_RESPONSE_QUEUE, false, false, true, null)
            .getQueue();
  }

  public String call(String message, String corrId) throws IOException, InterruptedException {

    AMQP.BasicProperties props =
        new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();

    final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

    manager
        .getChannel()
        .basicConsume(
            replyQueueName, true, new RpcConsumerClient(manager.getChannel(), corrId, response));

    manager
        .getChannel()
        .basicPublish(
            "", RabbitConstants.RPC_SEND_QUEUE, props, message.getBytes(StandardCharsets.UTF_8));

    return response.take();
  }
}
