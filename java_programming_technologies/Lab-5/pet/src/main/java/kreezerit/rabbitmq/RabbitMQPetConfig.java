package kreezerit.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQPetConfig {

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
    public Queue petGetAllQueue() {
        return new Queue("rpc.pet.getAll", false);
    }

    @Bean
    public Queue petGetByIdQueue() {
        return new Queue("rpc.pet.getById", false);
    }

    @Bean
    public Queue petGetIdByOwnerIdQueue() {
        return new Queue("rpc.pet.ownerId.byPetId", false);
    }

    @Bean
    public Queue petCreateQueue() {
        return new Queue("rpc.pet.create", false);
    }

    @Bean
    public Queue petUpdateQueue() {
        return new Queue("rpc.pet.update", false);
    }

    @Bean
    public Queue petDeleteQueue() {
        return new Queue("rpc.pet.delete", false);
    }

    @Bean
    public Queue petDeleteAllQueue() {
        return new Queue("rpc.pet.deleteAll", false);
    }

    @Bean
    public Queue petSearchByNameQueue() {
        return new Queue("rpc.pet.search.by.name", false);
    }

    @Bean
    public Queue petSearchQueue() {
        return new Queue("rpc.pet.search", false);
    }

    @Bean
    public Queue petFriendsOfQueue() {
        return new Queue("rpc.pet.friends.of", false);
    }

    @Bean
    public Queue petGetAllFriends() {
        return new Queue("rpc.pet.friends.all", false);
    }
}
