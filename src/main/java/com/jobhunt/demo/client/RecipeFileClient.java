package com.jobhunt.demo.client;

import com.jobhunt.demo.model.Post;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;

import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.retry.RetryExhaustedException;
import reactor.util.context.Context;
import reactor.util.retry.Retry;

@Component
public class RecipeFileClient {

    private final int readTimeoutMillis = 30000;
    private final int tcpConnectTimeoutMillis = 30000;
    private static final Logger logger = LoggerFactory.getLogger(RecipeFileClient.class);
    private final WebClient webClient;

    @Autowired
    RecipeFileClient() {
        this.webClient = init();
    }

    private WebClient init() {
        HttpClient httpClient = HttpClient.create().tcpConfiguration(tcpClient -> {
            tcpClient = tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, tcpConnectTimeoutMillis);
            tcpClient = tcpClient.doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(readTimeoutMillis, TimeUnit.MILLISECONDS)));
            return tcpClient;
        });

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<Post> doGet(String uri, MultiValueMap<String, String> headers) {
        return webClient.get().uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(Post.class)
                // this should always be less than the TCP timeout
                .timeout(Duration.ofSeconds(30))
                .retryWhen(getRetry(3));
    }

    private Retry getRetry(int maximumRetry) {
        return
                Retry.from((companion -> companion.handle((retrySignal, sink) -> {
                    long totalAttempedRetries = retrySignal.totalRetries();
                    if (!isRetryApplicableException(retrySignal.failure())) {
                        sink.error(retrySignal.failure());
                    } else if (totalAttempedRetries >= maximumRetry) {
                        logger.info("Reached maximum number of retry attempts");
                        // Maximum number of retries reached
                        throw new RetryExhaustedException("Reached maximum number of retry attempts");
                    } else {
                        logger.info("Retrying request with counter..." + totalAttempedRetries);
                        sink.next(Context.of("retriesLeft", totalAttempedRetries - 1, "lastError",
                                retrySignal.failure()));
                    }
                })));
    }

    private static boolean isRetryApplicableException(Throwable throwable) {
        logger.info("Throwable class is " + throwable.getClass());
        // Retry if error is read time out exception
        return throwable instanceof ReadTimeoutException || throwable instanceof SocketTimeoutException;
    }
}