package com.alchemist.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMER_INFO")
public class Customer {
	@Id
	@Column(name = "CUSTOMER_ID")
	private Integer id;
	private String firstname;
	private String lastname;
	private String email;
	private String gender;
	private String contactNum;
	private String country;
	private String dob;
}
