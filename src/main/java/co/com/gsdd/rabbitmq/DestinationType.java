package co.com.gsdd.rabbitmq;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.MODULE)
public enum DestinationType {
    QUEUE("SAMPLE.QUEUE"), EXCHANGE("EXCHANGE.SAMPLE"), RPC_CLIENT("RPC.CLIENT.QUEUE"), RPC_SERVER("RPC.SERVER.QUEUE");

    private final String value;

}
