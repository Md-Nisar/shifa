package com.mna.shifa.config;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class HttpTraceConfiguration {


    @Bean
    @Primary
    public HttpExchangeRepository httpExchangeRepository() {
        return new HttpExchangeRepository() {
            private final List<HttpExchange> exchanges = new ArrayList<>();

            @Override
            public void add(HttpExchange exchange) {
                if (exchanges.size() >= 100) {
                    exchanges.remove(0); // Remove the oldest exchange if limit is reached
                }
                exchanges.add(exchange);
                log.info("HTTP Request:: Method: {}, URI: {}, Time {}ms", exchange.getRequest().getMethod(), exchange.getRequest().getUri(), exchange.getTimeTaken());

            }

            @Override
            public List<HttpExchange> findAll() {
                return new ArrayList<>(exchanges);
            }
        };
    }


}

