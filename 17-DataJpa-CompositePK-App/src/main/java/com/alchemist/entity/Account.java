package com.alchemist.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "account_tbl")
@Data
public class Account {
	
	private String holderName;
	
	private String branch;
	
	@EmbeddedId
	private AccountPK accountPK;

}
