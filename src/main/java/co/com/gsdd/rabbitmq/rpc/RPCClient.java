package co.com.gsdd.rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;

import co.com.gsdd.rabbitmq.RabbitManager;
import co.com.gsdd.rabbitmq.constants.RabbitConstants;
import lombok.Getter;

@Getter
public class RPCClient {

    private RabbitManager manager;
    private String replyQueueName;

    public RPCClient() throws IOException, TimeoutException {
        manager = new RabbitManager(RabbitConstants.RPC_SEND_QUEUE);
        replyQueueName = manager.getChannel().queueDeclare(RabbitConstants.RPC_RESPONSE_QUEUE, false, false, true, null)
                .getQueue();
    }

    public String call(String message, String corrId) throws IOException, InterruptedException {

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
                .build();

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        manager.getChannel().basicConsume(replyQueueName, true,
                new RPCConsumerClient(manager.getChannel(), corrId, response));

        manager.getChannel().basicPublish("", RabbitConstants.RPC_SEND_QUEUE, props,
                message.getBytes(RabbitConstants.UTF_8));

        return response.take();
    }

}
