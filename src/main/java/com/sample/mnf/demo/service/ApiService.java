package com.sample.mnf.demo.service;

import com.sample.mnf.demo.client.BusinessClient;
import com.sample.mnf.demo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final BusinessClient businessClient;

    @Autowired
    ApiService(BusinessClient businessClient) {
        this.businessClient = businessClient;
    }

    public Mono<Post> getApis() {
        return businessClient.getApi();
    }

}
