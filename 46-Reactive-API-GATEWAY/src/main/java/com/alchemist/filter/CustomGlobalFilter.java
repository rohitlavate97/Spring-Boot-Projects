package com.alchemist.filter;

import java.util.Set;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(">> Filter Execution Started::: ");
        ServerHttpRequest request =exchange.getRequest();
        RequestPath path = request.getPath();
        System.out.println(">> Pre Filter: "+path);
        System.out.println("--------------------------------------------");
        HttpHeaders headers = request.getHeaders();
        Set<String> keySet = headers.keySet();
        for(String key: keySet) {
        	System.out.print(key+" ");
        	System.out.println(headers.getValuesAsList(key));
        	
        }
        // Call the next filter in the chain
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // Post Filter logic
            System.out.println("<< Post Filter: " + exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Precedence, lower = higher priority
    }
}
