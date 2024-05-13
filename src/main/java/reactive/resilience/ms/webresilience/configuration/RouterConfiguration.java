package reactive.resilience.ms.webresilience.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive.resilience.ms.webresilience.handler.HelloHandler;

import java.time.Duration;

@Configuration
public class RouterConfiguration {
    @Bean
    @Order
    public RouterFunction<ServerResponse> defaultRoute() {
        return RouterFunctions.route()
                .GET("/**", request -> ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue("Endpoint not found")))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public RouterFunction<ServerResponse> helloRoute(HelloHandler helloHandler) {
        return RouterFunctions.route()
                .GET("/hello", request -> helloHandler.sayHello(request))
                .build();
    }

    @Bean
    public CircuitBreaker circuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        return registry.circuitBreaker("helloCircuitBreaker");
    }
}
