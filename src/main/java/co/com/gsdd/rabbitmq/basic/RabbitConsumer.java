package co.com.gsdd.rabbitmq.basic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import co.com.gsdd.rabbitmq.RabbitManager;
import co.com.gsdd.rabbitmq.constants.RabbitConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitConsumer extends RabbitManager implements Runnable, Consumer {

    public RabbitConsumer(String endPointName) throws IOException, TimeoutException {
        super(endPointName);
    }

    @Override
    public void run() {
        try {
            // start consuming messages. Auto acknowledge messages.
            channel.basicConsume(endPointName, true, this);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Called when consumer is registered.
     */
    @Override
    public void handleConsumeOk(String consumerTag) {
        log.info("Consumer: {} registered", consumerTag);
    }

    /**
     * Called when new message is available.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
            throws IOException {
        Map<String, Integer> map = (HashMap<String, Integer>) SerializationUtils.deserialize(body);
        log.info("Mensaje # {} recibido.", map.get(RabbitConstants.MSJ_KEY));
    }

    @Override
    public void handleCancel(String consumerTag) {
        throw new UnsupportedOperationException(RabbitConstants.NOT_IMPLEMENTED);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        throw new UnsupportedOperationException(RabbitConstants.NOT_IMPLEMENTED);
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        throw new UnsupportedOperationException(RabbitConstants.NOT_IMPLEMENTED);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
        log.info("RabbitMQ for consumerTag {} is shutdown now", consumerTag);
    }
}