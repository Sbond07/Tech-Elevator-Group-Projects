package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcAccountDao() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // List accounts - modelled on catcards jdbcCatCardDao 10:15pm 7/6/22
    @Override
    public List<Account> list() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    // Get account
    @Override
    public Account get(int accountId) throws AccountNotFoundException {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        if(result.next()) {
            account = mapRowToAccount(result);
        } else {
            throw new AccountNotFoundException();
        }
        return account;
    }

    // Get account balance
    @Override
    public BigDecimal getBalance(int accountId) {
        int balance = 0;
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId, BigDecimal.class);
        return result.getBigDecimal(balance);
    }

    // Update account
    @Override
    public boolean updateAccount(int accountId, Account account) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        return jdbcTemplate.update(sql, account.getBalance(), account.getAccountId()) == 1;
    }

    // Delete account
    @Override
    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM account WHERE account_id = ?;";
        jdbcTemplate.update(sql, accountId);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }

}
