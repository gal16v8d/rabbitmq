package co.com.gsdd.rabbitmq.basic;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import co.com.gsdd.rabbitmq.constants.RabbitConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitLauncher {

    private static RabbitConsumer consumer;
    private static RabbitProducer producer;

    public static void main(String[] args) throws Exception {
        initConsumer();
        initProducer();
        finalizeRabbit();
    }

    private static void initConsumer() throws IOException, TimeoutException {
        consumer = new RabbitConsumer(RabbitConstants.COMMON_QUEUE);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

    private static void initProducer() throws IOException, TimeoutException {
        producer = new RabbitProducer(RabbitConstants.COMMON_QUEUE);

        for (int i = 0; i < RabbitConstants.PACKAGES_TO_SEND; i++) {
            HashMap<String, Integer> message = new HashMap<>();
            message.put(RabbitConstants.MSJ_KEY, i);
            producer.sendMessage(message);
            log.info("Mensaje # {} enviado.", i);
        }
    }

    private static void finalizeRabbit() {
        consumer.close();
        producer.close();
    }
}
