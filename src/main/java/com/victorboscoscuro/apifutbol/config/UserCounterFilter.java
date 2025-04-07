package com.victorboscoscuro.apifutbol.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class UserCounterFilter implements WebFilter {

    private final MeterRegistry meterRegistry;

    public UserCounterFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("USER-ID");

        if (userId != null && !userId.isEmpty()) {
            Counter.builder("api_user_access")
                    .tag("userId", userId)
                    .register(meterRegistry)
                    .increment();
        }

        return chain.filter(exchange);
    }
}