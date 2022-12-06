package com.marco.jmsqueue.listener;

import com.marco.jmsqueue.config.JmsConfig;
import com.marco.jmsqueue.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloMessage, @Headers MessageHeaders headers, Message message) {

        System.out.println("Ricevuto il messaggio");

        System.out.println(helloMessage);

    }

    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage helloMessage, @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage messagePayload = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Ciao mondo")
                .build();

        jmsTemplate.convertAndSend((Destination) message.getJMSReplyTo(),
                messagePayload);

    }

}
