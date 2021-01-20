package microservices.book.gamification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitMQConfiguration implements RabbitListenerConfigurer {

    @Bean
    public TopicExchange multiplicationExchange(@Value("${multiplication.exchange}") final String exchangeName)
    {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue gamificationMultiplicationQueue(@Value("${multiplication.queue}") final String queueName)
    {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding binding(
            final Queue queue
            , final TopicExchange exchange
            , @Value("${multiplication.anything.routing-key") final String routingKey)
    {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConvertor()
    {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory()
    {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConvertor());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar)
    {
        rabbitListenerEndpointRegistrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
