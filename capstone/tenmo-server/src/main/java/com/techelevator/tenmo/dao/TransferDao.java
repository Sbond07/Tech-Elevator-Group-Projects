package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNotFoundException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> list();

    Transfer get(int transferId) throws AccountNotFoundException, TransferNotFoundException;

    // Get transfers by userid
    Transfer getByUserId(int userId) throws TransferNotFoundException;

    Transfer createTransfer(int accountFrom, int accountTo, BigDecimal amount) throws TransferNotFoundException;

}

