package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;



@RestController
@RequestMapping("/account")
public class AccountController {
    //3:40pm - Jonathan - building an API endpoint to check the balance of an account.
    private JdbcAccountDao accountDao;
    private JdbcUserDao userDao;
    private JdbcTransferDao transferDao;

    public AccountController(JdbcAccountDao accountDao, JdbcUserDao userDao, JdbcTransferDao transferDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    // List accounts - modelled on the catcards app
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return accountDao.list();
    }

    // Get account
    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account get(@PathVariable int accountId) {
        try {
            return accountDao.get(accountId);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get account balance
    @RequestMapping(path = "/balance/{accountId}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable int accountId) {
        return accountDao.getBalance(accountId);
    }

    // Update account
    @RequestMapping(path = "/update/{accountId}", method = RequestMethod.PUT)
    public boolean updateAccount(@PathVariable @Valid int accountId, @RequestBody Account account) {
        return accountDao.updateAccount(accountId, account);
    }

    // Delete account
    @RequestMapping(path = "/delete/{accountId}", method = RequestMethod.POST)
    public void deleteAccount(@PathVariable int accountId) {
        accountDao.deleteAccount(accountId);
    }

}
