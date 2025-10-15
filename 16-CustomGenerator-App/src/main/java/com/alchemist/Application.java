package com.alchemist;

import com.alchemist.entity.OrderDetailsEntity;
import com.alchemist.repository.OrderDetailsRepository;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    private final OrderDetailsRepository orderDetailsRepository;

    Application(OrderDetailsRepository orderDetailsRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
    }

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		OrderDetailsRepository repo = context.getBean(OrderDetailsRepository.class);
		OrderDetailsEntity entity = new OrderDetailsEntity();
		entity.setOrderBy("Prime");
		entity.setOrderPlaceDate(new Date().toString());
		OrderDetailsEntity order = repo.save(entity);
		System.out.println(order.getOrderId());
		context.close();
		
	}

}
