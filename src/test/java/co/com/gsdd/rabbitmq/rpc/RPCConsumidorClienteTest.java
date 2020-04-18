package co.com.gsdd.rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

@ExtendWith(MockitoExtension.class)
public class RPCConsumidorClienteTest {

    private static final String VACIO = "";
    private static final String TEST = "test";
    private static final String MSJ = "msj";
    private static final String UTF_8 = "UTF-8";

    @Mock
    private Channel c;
    @Mock
    private Envelope e;
    @Mock
    private BlockingQueue<String> response;
    private RPCConsumerClient cliente;

    @BeforeEach
    public void setUp() {
        cliente = new RPCConsumerClient(c, TEST, response);
    }

    @Test
    public void handleDeliveryGoodCorrIdTest() throws IOException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().correlationId(TEST).build();
        byte[] body = MSJ.getBytes();
        cliente.handleDelivery(VACIO, e, properties, body);
        Mockito.verify(response).offer(new String(body, UTF_8));
    }

    @Test
    public void handleDeliveryBadCorrIdTest() throws IOException {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().correlationId("test2").build();
        byte[] body = MSJ.getBytes();
        cliente.handleDelivery(VACIO, e, properties, body);
        Mockito.verify(response, Mockito.never()).offer(new String(body, UTF_8));
    }
}
