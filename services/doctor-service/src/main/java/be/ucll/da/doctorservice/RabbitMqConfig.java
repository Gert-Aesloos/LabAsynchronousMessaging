package be.ucll.da.doctorservice;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Declarables createDoctorCreatedSchema(){
        return new Declarables(
                new FanoutExchange("x.doctor-created"),
                new Queue("q.doctor-appointment-service" ),
                QueueBuilder.durable("q.doctor-notification-service")
                        .withArgument("x-dead-letter-exchange","x.notification-failure")
                        .withArgument("x-dead-letter-routing-key","fall-back")
                        .build(),
                new Binding("q.doctor-appointment-service",
                        Binding.DestinationType.QUEUE,
                        "x.doctor-created",
                        "doctor-appointment-service",
                        null),
                new Binding(
                        "q.doctor-notification-service",
                        Binding.DestinationType.QUEUE,
                        "x.doctor-created",
                        "doctor-notification-service",
                        null)
        );
    }

    @Bean
    public Declarables createDeadLetterSchema(){
        return new Declarables(
                new DirectExchange("x.notification-failure"),
                new Queue("q.fall-back-notification"),
                new Binding("q.fall-back-notification", Binding.DestinationType.QUEUE,"x.notification-failure", "fall-back", null)
        );
    }
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(
            Jackson2JsonMessageConverter converter,
            CachingConnectionFactory cachingConnectionFactory) {
        var template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

//    @Bean
//    public RetryOperationsInterceptor retryInterceptor(){
//        return RetryInterceptorBuilder.stateless().maxAttempts(3)
//                .backOffOptions(2000, 2.0, 100000)
//                .recoverer(new RejectAndDontRequeueRecoverer())
//                .build();
//    }
}
