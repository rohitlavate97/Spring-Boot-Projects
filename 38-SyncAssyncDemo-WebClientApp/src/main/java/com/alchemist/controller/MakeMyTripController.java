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
		Ticket bookedTicket = service.bookTicketSync(passenger);
		model.addAttribute("msg","Ticket is booked with ID:"+bookedTicket.getTicketNumber());
		return "index";
	}
	
	/*
	 * @GetMapping("/get-ticket") public String
	 * getTicket(@RequestParam("tikcetNumber") Integer ticketNumber, Model model) {
	 * Ticket ticketByNum = service.getTicketByNum(ticketNumber);
	 * model.addAttribute("ticket",ticketByNum); return "ticket-form"; }
	 */
	
	@GetMapping("/get-ticket")  
	public String getTicketDetails(
	        @RequestParam(value = "ticketNumber", required = false) Integer ticketNumber, 
	        Model model) {
	    
	    System.out.println("=== GET TICKET DEBUG ===");
	    System.out.println("Received ticketNum parameter: " + ticketNumber);
	    
	    if (ticketNumber != null) {
	        System.out.println("Calling service.getTicketByNum(" + ticketNumber + ")");
	        Ticket ticket = service.getTicketByNumSync(ticketNumber);
	        System.out.println("Service returned ticket: " + ticket);
	        model.addAttribute("ticket", ticket);
	        
	        if (ticket == null) {
	            System.out.println("⚠️ No ticket found for number: " + ticketNumber);
	        } else {
	            System.out.println("✅ Ticket found: " + ticket.getTicketNumber());
	        }
	    } else {
	        System.out.println("ℹ️ No ticketNum parameter received");
	    }
	    
	    System.out.println("Returning view: ticket-form");
	    return "ticket-form";
	}
	
}
