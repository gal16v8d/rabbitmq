package com.gsdd.rabbitmq.rpc;

import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitRpcClientLauncher {

  public static void main(String[] argv) {
    try {
      String response;
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
    RpcClient fibonacciRpc = null;
    try {
      fibonacciRpc = new RpcClient();
      return fibonacciRpc.call(msj, corrId);
    } finally {
      if (fibonacciRpc != null && fibonacciRpc.getManager() != null) {
        fibonacciRpc.getManager().close();
      }
    }
  }
}
