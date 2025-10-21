package com.alchemist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.alchemist.controller.MakeMyTripController;
import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;

@Service
public class ServiceImpl implements MTMService{

	private String BOOK_TICKET_URL = "http://localhost:9090/ticket";       //ip inplace of localhost when deployed on AWS
	private String GET_TICKET_URL = "http://localhost:9090/ticket/{ticketNumber}";


	@Override
	public Ticket bookTicket(Passenger passenger) {
		WebClient webclient = WebClient.create();
		Ticket ticket = webclient.post()
				     .uri(BOOK_TICKET_URL)
				     //.body(BodyInserters.fromObject(passenger))    //deprecated
				     .bodyValue(passenger)
				     .retrieve()
				     .bodyToMono(Ticket.class)
				     .block();
		System.out.println(ticket);
		return ticket;
	}

	@Override
	public Ticket getTicketByNum(Integer ticketNumber) {
		//Get the instance of webclient(Implementaion class object)
		WebClient webclient = WebClient.create(); 
		//Send GET Request and map response to ticket obj
		Ticket ticket = webclient.get()                         //represent get request
				        .uri(GET_TICKET_URL,ticketNumber)      //If we want to send query param-->set it parameter here
				        .retrieve()                            //retrieve the response
				        .bodyToMono(Ticket.class)              //response map to Ticket class object and return object
				        .block();                             //wait till get response from IRCTC app and store into variable
		System.out.println(ticket);
		return ticket;
	}

}
