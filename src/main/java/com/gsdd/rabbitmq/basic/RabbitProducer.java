package com.gsdd.rabbitmq.basic;

import com.gsdd.rabbitmq.RabbitManager;
import com.gsdd.rabbitmq.constants.RabbitConstants;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.SerializationUtils;

public class RabbitProducer extends RabbitManager {

  public RabbitProducer(String endPointName) throws IOException, TimeoutException {
    super(endPointName);
  }

  public void sendMessage(Serializable object) throws IOException {
    getChannel()
        .basicPublish(
            RabbitConstants.EXCHANGE,
            getEndPointName(),
            null,
            SerializationUtils.serialize(object));
  }
}
