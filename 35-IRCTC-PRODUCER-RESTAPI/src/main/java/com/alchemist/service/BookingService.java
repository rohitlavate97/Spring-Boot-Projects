package com.alchemist.service;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;

public interface BookingService {
	
	public Ticket bookTicket(Passenger passenger);
	
	public Ticket getTicket(Integer ticketNumber);

}
