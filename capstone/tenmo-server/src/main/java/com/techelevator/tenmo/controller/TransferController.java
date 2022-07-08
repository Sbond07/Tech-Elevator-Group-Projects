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



    //Create transfer
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/createTransfer", method = RequestMethod.POST)
    public void createTransfer(@Valid @RequestBody TransferDTO newTransfer) {
//        if (!transferDao.createTransfer(newTransfer.  )) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed.");
//        }
    }
//
//    public Transfer createTransfer(int accountFrom, int accountTo, BigDecimal amount) {
//        Transfer transfer = new Transfer();
//
//    }


}
