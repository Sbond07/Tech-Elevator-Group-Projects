package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/transfer")
public class TransferController {

    private JdbcTransferDao transferDao;
    private JdbcAccountDao accountDao;

    public TransferController(JdbcTransferDao transferDao, JdbcAccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    //returning a list of transfers
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getAllTransfer() {

        return transferDao.list();
    }

    //get a transfer
    @RequestMapping(path = "/{transferId}", method = RequestMethod.GET)
    public Transfer get(@PathVariable int transferId) {
        try {
            return transferDao.get(transferId);
        } catch (TransferNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get by userId
    @RequestMapping(path = "/search/{userId}", method = RequestMethod.GET)
    public List<Transfer> getByUserId(@PathVariable int userId) {
       /* TODO If we want to be secure: add as parameter above : , Principal principal
        if (principal.getName().equals()
         need to call the user.getName inside the equals above */
        try {
            return transferDao.getByUserId(userId);
        } catch (TransferNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Create transfer
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody TransferDTO newTransfer) {
        Transfer transfer = null;

        try {
            Account fromAccount = accountDao.get(newTransfer.getAccountFrom());
            if (fromAccount.getBalance().compareTo(newTransfer.getAmount()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds.");
            }
            if (newTransfer.getAccountFrom() == newTransfer.getAccountTo()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Don't waste your time and mine!!!");
            }
            if (newTransfer.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0 ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Why do you want to transfer 0 or less? Why bother?");
            }

            transfer = transferDao.createTransfer(newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
        } catch (TransferNotFoundException | AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed. Team 2 - you can solve this!");
        }
        return transfer;
    }

}
