package kreezerit.Mowner.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQOwnerConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setReplyTimeout(5000);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue onwerCreateForUserQueue() {
        return new Queue("rpc.owner.create.for.user", false);
    }

    @Bean
    public Queue onwerGetAllQueue() {
        return new Queue("rpc.owner.getAll", false);
    }

    @Bean
    public Queue onwerGetByIdQueue() {
        return new Queue("rpc.owner.getById", false);
    }

    @Bean
    public Queue onwerGetIdByUsernameQueue() {
        return new Queue("rpc.owner.id.by.username", false);
    }

    @Bean
    public Queue onwerCreateQueue() {
        return new Queue("rpc.owner.create", false);
    }

    @Bean
    public Queue onwerUpdateQueue() {
        return new Queue("rpc.owner.update", false);
    }

    @Bean
    public Queue onwerDeleteQueue() {
        return new Queue("rpc.owner.delete", false);
    }

    @Bean
    public Queue onwerDeleteAllQueue() {
        return new Queue("rpc.owner.deleteAll", false);
    }

    @Bean
    public Queue onwerSearchByNameQueue() {
        return new Queue("rpc.owner.search.by.name", false);
    }
}
