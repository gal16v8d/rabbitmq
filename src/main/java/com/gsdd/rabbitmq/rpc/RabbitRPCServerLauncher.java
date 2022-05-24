package com.gsdd.rabbitmq.rpc;

import com.gsdd.rabbitmq.RabbitManager;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitRPCServerLauncher {

  public static void main(String[] argv) {
    RabbitManager rm = null;
    try {
      rm = new RabbitManager(RabbitConstants.RPC_SEND_QUEUE);
      rm.getChannel().basicQos(1);

      log.info(" [x] Awaiting RPC requests");
      RPCConsumerServer consumer = new RPCConsumerServer(rm.getChannel());
      rm.getChannel().basicConsume(RabbitConstants.RPC_SEND_QUEUE, consumer);
      // Wait and be prepared to consume the message from RPC client.
      waitMessages(consumer);
    } catch (IOException | TimeoutException e) {
      log.error(e.getMessage(), e);
    } finally {
      if (rm != null && rm.getConnection() != null) {
        rm.close();
      }
    }
  }

  private static void waitMessages(RPCConsumerServer consumer) {
    while (true) {
      synchronized (consumer) {
        try {
          consumer.wait();
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
          Thread.currentThread().interrupt();
        }
      }
    }
  }
}
