package reactive.resilience.ms.webresilience.handler;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class HelloHandler {

    private final CircuitBreaker circuitBreaker;

    public HelloHandler(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public Mono<ServerResponse> sayHello(ServerRequest request) {
        boolean fallback = Boolean.parseBoolean(request.queryParam("fallback").orElse("false"));
        if (fallback) {
            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue("Fallback: Hello, World!");
        } else {
            return Mono.just("Hello, World!")
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                    .flatMap(message -> ServerResponse.ok()
                            .contentType(MediaType.TEXT_PLAIN)
                            .bodyValue(message));
        }
    }
}
