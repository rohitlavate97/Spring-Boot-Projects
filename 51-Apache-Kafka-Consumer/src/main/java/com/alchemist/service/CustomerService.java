package com.alchemist.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alchemist.constants.KafkaConstants;
import com.alchemist.model.Customer;

@Service("CustomerService")
public class CustomerService {
	private Customer lastReceivedCustomer;

	
	@KafkaListener(topics = KafkaConstants.TOPIC, groupId = KafkaConstants.GROUP_ID)
	public void listener(Customer c) {
		System.out.println("****Message received form kafka-topic::"+c);
		//return c;
		this.lastReceivedCustomer = c;
	}
	
    public Customer getLastReceivedCustomer() {
        return lastReceivedCustomer;
    }
}
