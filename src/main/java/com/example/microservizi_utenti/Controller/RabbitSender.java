package com.example.microservizi_utenti.Controller;

import com.example.microservizi_utenti.Entities.RabbitSenderInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RabbitSender implements RabbitSenderInterface {
    @Autowired
    private FanoutExchange exchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchange.getName(), "", message);
        System.out.println("Mandato il messaggio " + message + " correttamente");
    }

}