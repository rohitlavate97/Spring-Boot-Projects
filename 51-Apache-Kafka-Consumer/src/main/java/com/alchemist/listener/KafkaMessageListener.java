package com.alchemist.listener;

import com.alchemist.constants.KafkaConstants;
import com.alchemist.model.Customer;
import com.alchemist.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    @Autowired
    private CustomerService customerService;

    @KafkaListener(topics = KafkaConstants.TOPIC, groupId = KafkaConstants.GROUP_ID,
                   containerFactory = "kafkaListenerContainerFactory")
    public void consume(Customer customer) {
        System.out.println("✅ Received Customer: " + customer);
        customerService.setLastCustomer(customer);
    }
}
