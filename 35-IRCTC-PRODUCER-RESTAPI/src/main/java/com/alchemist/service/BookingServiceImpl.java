package com.alchemist.service;

import java.util.HashMap;
import java.util.Map;
import com.alchemist.rest.BookingRestController;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;

@Service
public class BookingServiceImpl implements BookingService{

	private Map<Integer ,Ticket> ticketMap = new HashMap<>();
	private Integer ticketNum = 1;

	@Override
	public Ticket bookTicket(Passenger passenger) {
		Ticket t = new Ticket();
		BeanUtils.copyProperties(passenger, t);
		t.setTicketCost(1000.00);
		t.setStatus("CONFIRMED");
		t.setTicketNumber(ticketNum);
		ticketMap.put(ticketNum, t);
		ticketNum++;
		return t;
	}

	@Override
	public Ticket getTicket(Integer ticketNumber) {
		if(ticketMap.containsKey(ticketNumber)) {
			Ticket ticket = ticketMap.get(ticketNumber);
			System.out.println(ticket);
			return ticket;
		}
		return null;
	}
	

}
