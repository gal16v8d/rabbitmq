package co.com.gsdd.rabbitmq.rpc;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import co.com.gsdd.rabbitmq.constants.RabbitConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RPCConsumerServer extends DefaultConsumer {

    protected Channel channel;

    public RPCConsumerServer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                .correlationId(properties.getCorrelationId()).build();

        String response = "";

        try {
            String message = new String(body, RabbitConstants.UTF_8);
            int n = Integer.parseInt(message);

            log.info(" [.] fib({})", message);
            response += fib(n);
        } catch (RuntimeException e) {
            log.error(" [.] {}", e);
        } finally {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes(RabbitConstants.UTF_8));
            channel.basicAck(envelope.getDeliveryTag(), false);
            // RabbitMq consumer worker thread notifies the RPC
            // server owner thread
            synchronized (this) {
                this.notify();
            }
        }
    }

    private static long fib(int n) {
        if (n <= 1) {
            return n;
        }
        int fib = 1;
        int prevFib = 1;

        for (int i = 2; i < n; i++) {
            int temp = fib;
            fib += prevFib;
            prevFib = temp;
        }
        return fib;
    }
}
