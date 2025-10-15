package com.alchemist.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class AccountPK {
	
	private String accNum;
	
	private String accType;

}
