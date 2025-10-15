package com.alchemist;

import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alchemist.entity.Account;
import com.alchemist.entity.AccountPK;
import com.alchemist.repo.AccountRepository;

@SpringBootApplication
public class Application {

    private final AccountRepository accountRepository;

    Application(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		AccountRepository repo = context.getBean(AccountRepository.class);
		/*
		 * AccountPK pk = new AccountPK(); pk.setAccNum("123787");
		 * pk.setAccType("SAVINGS");
		 * 
		 * Account account = new Account(); account.setHolderName("Pranali");
		 * account.setBranch("Ichalkaranji"); account.setAccountPK(pk);
		 * repo.save(account); System.out.println("Account saved");
		 */
		
		//Retrieving the record
		AccountPK pk = new AccountPK();
		pk.setAccNum("123787");
		pk.setAccType("SAVINGS");
		/*
		 * repo.findById(pk).ifPresent(acc -> {
		 * System.out.println("Account Holder Name: "+acc.getHolderName());
		 * System.out.println("Account Branch: "+acc.getBranch());
		 * System.out.println("Account Number: "+acc.getAccountPK().getAccNum());
		 * System.out.println("Account Type: "+acc.getAccountPK().getAccType()); });
		 */
		Optional<Account> byId = repo.findById(pk);
		if(byId.isPresent()) {
			System.out.println("Account Holder Name: "+byId.get());
		}
	}

}
