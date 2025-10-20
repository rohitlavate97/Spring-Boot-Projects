package com.alchemist.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;
import com.alchemist.service.BookingService;

@RestController
public class BookingRestController {
	@Autowired
	private BookingService service;
	
	@PostMapping(
			value="/ticket",
			consumes="application/json",
			produces="application/json"
			)
	public  ResponseEntity<Ticket> bookTicket(@RequestBody Passenger passenger){
		Ticket bookedTicket = service.bookTicket(passenger);
		return new ResponseEntity<>(bookedTicket,HttpStatus.CREATED);
	}
	
	@GetMapping(
			value="/ticket/{ticketNum}",
			produces="application/json"
			)
	public Ticket getTicketByNum(@PathVariable Integer ticketNum) {
		Ticket ticket = service.getTicket(ticketNum);
		return ticket;
	}
}
