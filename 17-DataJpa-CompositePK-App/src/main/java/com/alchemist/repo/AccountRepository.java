package com.alchemist.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alchemist.entity.Account;
import com.alchemist.entity.AccountPK;

public interface AccountRepository extends JpaRepository<Account, AccountPK> {

}
