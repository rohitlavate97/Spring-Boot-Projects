package com.alchemist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alchemist.request.Passenger;
import com.alchemist.response.Ticket;
import com.alchemist.service.MTMService;


@Controller
public class MakeMyTripController {
	@Autowired
	private MTMService service;
	
	@GetMapping("/")
	public String loadForm(Model model) {
		model.addAttribute("passenger",new Passenger());
		return "index";
	}
	
	@PostMapping("book-ticket")
	public String bookTicket(@ModelAttribute("passenger")Passenger passenger, Model model) {
		Ticket bookedTicket = service.bookTicket(passenger);
		model.addAttribute("msg","Ticket is booked with ID:"+bookedTicket.getTicketNumber());
		return "index";
	}
	
	@GetMapping("/get-ticket")
	public String getTicket(@RequestParam("ticketNum") Integer ticketNum, Model model) {
		Ticket ticketByNum = service.getTicketByNum(ticketNum);
		model.addAttribute("ticket",ticketByNum);
		return "ticket-form";
	}
	
}
