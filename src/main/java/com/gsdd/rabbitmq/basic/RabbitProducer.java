package com.gsdd.rabbitmq.basic;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang.SerializationUtils;
import com.gsdd.rabbitmq.RabbitManager;
import com.gsdd.rabbitmq.constants.RabbitConstants;

public class RabbitProducer extends RabbitManager {

  public RabbitProducer(String endPointName) throws IOException, TimeoutException {
    super(endPointName);
  }

  public void sendMessage(Serializable object) throws IOException {
    channel.basicPublish(RabbitConstants.EXCHANGE, endPointName, null,
        SerializationUtils.serialize(object));
  }
}
