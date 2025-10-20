package com.alchemist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;

@Service
public class ServiceImpl implements MTMService{
	private String BOOK_TICKET_URL = "http://localhost:9090/ticket";       //ip inplace of localhost when deployed on AWS
	private String GET_TICKET_URL = "http://localhost:9090/ticket/{ticketNum}";

	@Override
	public Ticket bookTicket(Passenger passenger) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<Ticket> response = rt.postForEntity(BOOK_TICKET_URL, passenger, Ticket.class);       //to send post request
		Ticket ticket = response.getBody();
		return ticket;
	}

	@Override
	public Ticket getTicketByNum(Integer ticketNumber) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<Ticket> response = rt.getForEntity(GET_TICKET_URL, Ticket.class, ticketNumber);    // to send get request
		Ticket ticket = response.getBody();
		return ticket;
	}

}
