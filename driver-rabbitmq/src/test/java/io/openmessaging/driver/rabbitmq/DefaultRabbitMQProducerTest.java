package io.openmessaging.driver.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ExportException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class DefaultRabbitMQProducerTest {
    static String host = "tcloud";
    static int port = 5672;
    static String user = "guest";
    static String password = "guest";
    static DefaultRabbitMQProducer producer = new DefaultRabbitMQProducer(host, port, user, password);

    @Test
    public void init() {
        assertNotNull(producer.getConnection());
    }

    @Test
    public void sendMessage() {
        try {
            producer.sendMessage("openchaos_client_test", "hello rabbitmq".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getConnection() {
        assertNotNull(producer.getConnection());
    }
}