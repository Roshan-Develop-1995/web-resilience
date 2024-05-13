package reactive.resilience.ms.webresilience.router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class RouterHandlerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHelloHandler() {
        webTestClient.get().uri("/hello")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Hello, World!");
    }

    @Test
    public void testHelloHandlerWithFallback() {
        webTestClient.get().uri("/hello?fallback=true")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Fallback: Hello, World!");
    }

    @Test
    public void testHelloHandler_withWrongEndpoint() {
        webTestClient.get().uri("/hello-incorrect")
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
