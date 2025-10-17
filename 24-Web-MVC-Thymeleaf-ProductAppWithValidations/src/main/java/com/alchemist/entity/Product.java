package com.alchemist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer pid;
	
	@Size(min = 3, max = 15, message = "Name must have between {min} and {max} characters")
	@NotBlank(message="Name is mandatory")
	private String name;
	
	@NotNull(message="Price should be not null")
	private Double price;
	
	@NotNull(message="Quantity should be entered")
	private Integer quantity;

}
