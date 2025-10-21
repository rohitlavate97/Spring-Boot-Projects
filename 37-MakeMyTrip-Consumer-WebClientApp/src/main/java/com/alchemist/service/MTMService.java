package com.alchemist.service;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;

public interface MTMService {
	public Ticket bookTicket(Passenger passenger);
	
	public Ticket getTicketByNum(Integer tikcetNumber);
}
