package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountDao {

    List<Account> listAccounts ();

    Account getAccount (int accountId) throws AccountNotFoundException;

    boolean updateAccount (int accountId, Account account);

    boolean deleteAccount (int accountId);

}
