package com.alchemist.rest;

import java.time.Duration;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.binding.CustomerEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class ReactiveRestController {
	@GetMapping("/single")
	public Mono<String> sigleResponse(){
		String msg ="Hello World";
		return Mono.just("Hello world");
		//return Mono.justOrEmpty(msg);   //To avoid NullPointerException-->if object available it pass or return empty value
	}
	
	@GetMapping("/multiple")
	public Flux<Integer> multipleResponses(){
		return Flux.range(1, 10);
	}
	
	@GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<Integer> streamMultiResponses() {
        // Emits numbers 1–10, one every 10 seconds
        return Flux.range(1, 10)
                   .delayElements(Duration.ofSeconds(10));
    }
	
	@GetMapping(value = "/event", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<CustomerEvent> getEvent() {
		CustomerEvent event = new CustomerEvent("John",new Date());
		return Mono.justOrEmpty(event);
	}
	
	@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CustomerEvent> getCustomerStream(){
		CustomerEvent event = new CustomerEvent("Rohit", new Date());
		CustomerEvent[] eventArray = {event};
		//Creating data flux
		Flux<CustomerEvent> dataFlux = Flux.fromArray(eventArray);
		//setting response internal
		Flux<Long> internalFlux = Flux.interval(Duration.ofSeconds(5));
		Flux<Tuple2<Long,CustomerEvent>> zip = Flux.zip(internalFlux, dataFlux);
		//getting Flux value from the zip
		Flux<CustomerEvent> fluxMap = zip.map(Tuple2::getT2);
		return fluxMap;
	}

}
