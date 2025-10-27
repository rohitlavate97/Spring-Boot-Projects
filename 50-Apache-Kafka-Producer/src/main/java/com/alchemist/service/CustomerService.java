package com.alchemist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.alchemist.model.Customer;
import com.alchemist.util.KafkaConstants;

@Service("CustomerService")
public class CustomerService {
	@Autowired
	private KafkaTemplate<String, Customer> kafkaTemplate;
	
	public String add(List<Customer> customers) {
		if(!customers.isEmpty()) {
			for(Customer c : customers) {
				kafkaTemplate.send(KafkaConstants.TOPIC, c);
				System.out.println("*****Message is published to kafka topic*****");
			}
		}
		return "Customer record is added to the Kafka-Queue successfully";
	}
}
