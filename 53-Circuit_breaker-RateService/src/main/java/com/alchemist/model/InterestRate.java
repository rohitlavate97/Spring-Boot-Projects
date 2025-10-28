package com.alchemist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestRate {
	private Integer id;
	private String type;
	private Double rateValue;
}
