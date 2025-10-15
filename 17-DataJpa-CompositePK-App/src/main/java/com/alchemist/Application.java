package com.alchemist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alchemist.entity.Account;
import com.alchemist.entity.AccountPK;
import com.alchemist.repo.AccountRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		AccountRepository repo = context.getBean(AccountRepository.class);
		AccountPK pk = new AccountPK();
		pk.setAccNum("123456");
		pk.setAccType("SAVINGS");
		
		Account account = new Account();
		account.setHolderName("Prajkta");
		account.setBranch("Pune");
		account.setAccountPK(pk);
		repo.save(account);
		System.out.println("Account saved");
	}

}
