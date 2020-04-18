package co.com.gsdd.rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import co.com.gsdd.rabbitmq.RabbitManager;
import co.com.gsdd.rabbitmq.constants.RabbitConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitRPCServerLauncher {

    public static void main(String[] argv) {
        RabbitManager rm = null;
        try {
            rm = new RabbitManager(RabbitConstants.RPC_SEND_QUEUE);
            rm.getChannel().basicQos(1);

            log.info(" [x] Awaiting RPC requests");
            RPCConsumerServer consumidor = new RPCConsumerServer(rm.getChannel());
            rm.getChannel().basicConsume(RabbitConstants.RPC_SEND_QUEUE, consumidor);
            // Wait and be prepared to consume the message from RPC client.
            waitMessages(consumidor);
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (rm != null && rm.getConnection() != null) {
                rm.close();
            }
        }
    }

    private static void waitMessages(RPCConsumerServer consumidor) {
        while (true) {
            synchronized (consumidor) {
                try {
                    consumidor.wait();
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
