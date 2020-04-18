package co.com.gsdd.rabbitmq.publishsubscribe;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import co.com.gsdd.rabbitmq.AbstractBrokerConfig;
import co.com.gsdd.rabbitmq.DestinationType;
import co.com.gsdd.rabbitmq.MessageProcessor;
import co.com.gsdd.rabbitmq.constants.RabbitConstants;

public final class BrokerPublishSubscribe extends AbstractBrokerConfig {

    private static BrokerPublishSubscribe instance;

    private BrokerPublishSubscribe(String host) throws IOException, TimeoutException {
        super(host, DestinationType.EXCHANGE);
    }

    @Override
    public void defineDestination() throws IOException {
        getChannel().exchangeDeclare(DestinationType.EXCHANGE.getValue(), "fanout");
    }

    @Override
    public void sendMessage(String message) throws IOException {
        getChannel().basicPublish(DestinationType.EXCHANGE.getValue(), "", null, message.getBytes());
    }

    @Override
    public void receiveMessage() throws IOException {
        String queueName = getChannel().queueDeclare().getQueue();
        getChannel().queueBind(queueName, getDestinationType().getValue(), "");

        String queueName2 = getChannel().queueDeclare().getQueue();
        getChannel().queueBind(queueName2, getDestinationType().getValue(), "");

        MessageProcessor procesador = new MessageProcessor(getChannel());
        getChannel().basicConsume(queueName, false, procesador);
    }

    public static synchronized BrokerPublishSubscribe getIntance() throws IOException, TimeoutException {
        if (instance == null) {
            instance = new BrokerPublishSubscribe(RabbitConstants.HOST);
        }
        return instance;
    }
}
