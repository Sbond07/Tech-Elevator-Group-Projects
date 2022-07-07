package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
//import java.util.logging.Logger;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
//    private Logger log = (Logger) LoggerFactory.getLogger(getClass());

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public List<Transfer> list() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }


    @Override
    public Transfer get(int transferId) throws TransferNotFoundException {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(int accountFrom, int accountTo, BigDecimal amount) {
        Transfer transfer = new Transfer();
        String sql = "BEGIN TRANSACTION;" +
                "UPDATE account SET balance = balance - ? WHERE account_id = ?;" +
                "UPDATE account SET balance = balance + ? WHERE account_id = ?;" +
                "SELECT t.transfer_id FROM transfer t JOIN account a ON " +
                "t.account_from = a.account_id WHERE t.account_from = ?; " +
                "COMMIT;";

        return null;
    }

    @Override
    public boolean updateTransfer(int transferId, Transfer transfer) {
        return false;
    }

    @Override
    public void deleteTransfer(int transferId) {

    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setUserId(rs.getInt("user_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }


}
