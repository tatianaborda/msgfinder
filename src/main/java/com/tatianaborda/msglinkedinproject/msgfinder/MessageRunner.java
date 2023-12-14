package com.tatianaborda.msglinkedinproject.msgfinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MessageRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    private ConfigurableApplicationContext context;

    public MessageRunner(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception{
        try(Scanner s= new Scanner(System.in)){
            System.out.print("ID of meeting: ");
            long idMeeting = Long.parseLong(s.next());
            System.out.print("ID of person: ");
            long idPerson = Long.parseLong(s.next());
            MessageData messageData = new MessageData();
            messageData.setIdMeeting(idMeeting);
            messageData.setIdAttendee(idPerson);
            rabbitTemplate.convertAndSend("services",
                    "meeting.message.person",
                    objectMapper.writeValueAsString(messageData));
            context.close();
        }
    }
}
