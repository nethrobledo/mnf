package com.sample.mnf.demo.client;

import com.sample.mnf.demo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Service
public class BusinessClient {

    @Value("${sample.baseUrl}")
    private String baseUrl;

    private static final String SAMPLE_URI = "/posts/1";
    private final ApiWebClient apiWebClient;

    @Autowired
    BusinessClient(ApiWebClient apiWebClient) {
        this.apiWebClient = apiWebClient;
    }

    public Mono<Post> getApi() {
        return apiWebClient.doGet(baseUrl + SAMPLE_URI, generateHeader());
    }

    private MultiValueMap<String, String> generateHeader() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        return headers;
    }

}
