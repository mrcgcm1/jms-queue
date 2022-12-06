package com.marco.jmsqueue.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marco.jmsqueue.config.JmsConfig;
import com.marco.jmsqueue.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
    System.out.println("Manda un messaggio");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Ciao mondo")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
        System.out.println("Message Sent!");

    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Ciao mondo")
                .build();

        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                try {
                    Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "com.marco.jmsqueue.model.HelloWorldMessage");
                    System.out.println("Sending Message!");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException("Errore !");
                }
            }
        });
        System.out.println("Message Sent! :" + receivedMessage.getBody(String.class));

    }
}
