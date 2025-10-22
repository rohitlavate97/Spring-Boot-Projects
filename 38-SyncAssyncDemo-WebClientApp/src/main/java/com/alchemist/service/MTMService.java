package com.alchemist.service;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;

public interface MTMService {
	public Ticket bookTicketSync(Passenger passenger);

	public Ticket getTicketByNumSync(Integer tikcetNumber);

	public void bookTickeASync(Passenger passenger);

	public void getTicketByNumASync(Integer tikcetNumber);
}
