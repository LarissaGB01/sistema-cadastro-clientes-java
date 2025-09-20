package com.tcc.engenharia_software.infra.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.engenharia_software.infra.database.entity.Clientes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessageDeliveryMode;

@Component
public class ClientePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void enviarParaFila(Clientes cliente) {
        try {
            String json = objectMapper.writeValueAsString(cliente);

            MessageProperties props = new MessageProperties();
            props.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            Message message = new Message(json.getBytes(), props);
            rabbitTemplate.send(RabbitMQConfig.QUEUE_NAME, message);

            System.out.println("Mensagem publicada na fila: " + json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar cliente para JSON", e);
        }
    }
}
