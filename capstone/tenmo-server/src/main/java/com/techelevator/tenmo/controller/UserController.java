package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/user")
public class UserController {

    private JdbcUserDao userDao;

    public UserController(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    // List all users
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    // Get a user
    @RequestMapping(path = "/{username}", method = RequestMethod.GET)
    public User getByUserName(@PathVariable String username) {
        try {
            return userDao.findByUsername(username);
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
