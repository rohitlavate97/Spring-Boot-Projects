package com.alchemist.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ORDER_DTLS")
public class OrderDetailsEntity {
	
	@Id
	@GenericGenerator(name = "order_id_gen", strategy = "com.alchemist.generator.OrderIdGenerator")
	@GeneratedValue(generator = "order_id_gen")
	@Column(name = "ORDER_ID")
	private String orderId;
	
	@Column(name = "ORDER_BY")
	private String orderBy;
	
	@Column(name = "ORDER_PLACE_DATE")
	private String orderPlaceDate;

}
