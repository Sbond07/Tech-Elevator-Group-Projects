package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;

@RestController
public class AccountController {
    // TODO build this AccountController using API endpoints (see CatCardController in CatCard Exercise)

    /*
    //3:40pm - Jonathan - building an API endpoint to check the balance of an account.
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int id) throws AccountNotFoundException {
        return accountDao.getAccount(id);
    }
   */
}
