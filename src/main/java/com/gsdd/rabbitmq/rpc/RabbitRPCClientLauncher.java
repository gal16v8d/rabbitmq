package com.gsdd.rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitRPCClientLauncher {

  public static void main(String[] argv) {
    try {
      String response = null;
      for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
        log.info(" [x] Requesting fib({})", i);
        response = sendMessages(String.valueOf(i), String.valueOf(i));
        log.info(" [.] Got '{}'", response);
      }
    } catch (IOException | TimeoutException e) {
      log.error(e.getMessage(), e);
    } catch (InterruptedException i) {
      log.error(i.getMessage(), i);
      Thread.currentThread().interrupt();
    }
  }

  private static String sendMessages(String corrId, String msj)
      throws IOException, InterruptedException, TimeoutException {
    RPCClient fibonacciRpc = null;
    try {
      fibonacciRpc = new RPCClient();
      return fibonacciRpc.call(msj, corrId);
    } finally {
      if (fibonacciRpc != null && fibonacciRpc.getManager() != null) {
        fibonacciRpc.getManager().close();
      }
    }
  }
}
