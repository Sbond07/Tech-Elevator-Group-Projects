package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
public class AccountController {

    private JdbcAccountDao accountDao;
    private JdbcUserDao userDao;

    public AccountController(JdbcAccountDao accountDao, JdbcUserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    // List all accounts
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return accountDao.list();
    }

    // Get an account
    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account get(@PathVariable int accountId) {
        try {
            return accountDao.get(accountId);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get an account by user ID
    @RequestMapping(path = "search/{userId}", method = RequestMethod.GET)
    public Account getByUserId(@PathVariable int userId) {
        try {
            return accountDao.getByUserId(userId);
        } catch (AccountNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get account balance
    @RequestMapping(path = "/balance/{accountId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId) {
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
