package com.alchemist.rest;

import java.time.Duration;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public Flux<CustomerEvent> getCustomerStream() {
	    
	    // Creating two sample CustomerEvent objects with name and current timestamp
	    CustomerEvent event1 = new CustomerEvent("Rohit", new Date());
	    CustomerEvent event2 = new CustomerEvent("Pramod", new Date());
	    
	    // Storing them in an array
	    CustomerEvent[] eventArray = {event1, event2};
	    
	    // Creating a Flux (reactive stream) from the array of events
	    // Flux.fromArray() creates a publisher that emits each element in the array
	    Flux<CustomerEvent> dataFlux = Flux.fromArray(eventArray);
	    
	    // Creating another Flux that emits a sequential number every 5 seconds
	    // This acts as a timer that controls how often data is emitted
	    Flux<Long> internalFlux = Flux.interval(Duration.ofSeconds(5));
	    
	    // Combining (zipping) both Flux streams together
	    // Each emitted item from internalFlux (timer) will be paired with one item from dataFlux
	    // Tuple2 represents a pair of (timer value, event object)
	    Flux<Tuple2<Long, CustomerEvent>> zip = Flux.zip(internalFlux, dataFlux);
	    
	    // Extracting only the CustomerEvent (second element of the tuple)
	    // Tuple2::getT2 means “get the second value” from each tuple
	    Flux<CustomerEvent> fluxMap = zip.map(Tuple2::getT2);
	    
	    // Returning the stream of CustomerEvent objects as Server-Sent Events (SSE)
	    // This will send one event every 5 seconds to the client
	    return fluxMap;
	    //return new ResponseEntity<>(fluxMap, HttpStatus.OK)
	}

}
