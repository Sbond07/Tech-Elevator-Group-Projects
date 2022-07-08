package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> list();

    Account get(int accountId) throws AccountNotFoundException;

    BigDecimal getBalance(int accountId);

    boolean updateAccount (int accountId, Account account);

    void deleteAccount (int accountId);

}
