package co.com.gsdd.rabbitmq.constants;

import co.com.gsdd.docker.config.util.DockerEnvLoader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RabbitConstants {

    public static final String COMMON_QUEUE = "common";
    public static final String RPC_SEND_QUEUE = "sendRPC";
    public static final String RPC_RESPONSE_QUEUE = "responseRPC";

    public static final String NOT_IMPLEMENTED = "Not implemented yet";
    public static final String MSJ_KEY = "newMsg";

    public static final String EXCHANGE = "";

    public static final String HOST = DockerEnvLoader.getDockerServiceIp();
    public static final String USER = "guest";
    public static final String PASS = USER;
    public static final int PORT = 5672;
    public static final int TIMEOUT = 90000;
    public static final int WAIT_TIME = 200;
    public static final int RECOVERY_CHECK = 60000;

    public static final int PACKAGES_TO_SEND = 200;

}
