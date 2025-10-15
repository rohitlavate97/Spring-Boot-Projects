package com.alchemist.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class AccountPK implements Serializable {
	
	private String accNum;
	
	private String accType;

}
