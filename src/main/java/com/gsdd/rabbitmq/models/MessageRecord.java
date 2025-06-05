package com.gsdd.rabbitmq.models;

import java.io.Serializable;

public record MessageRecord(int messageNumber) implements Serializable {}
