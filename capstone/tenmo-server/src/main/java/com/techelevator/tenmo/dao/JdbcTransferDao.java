package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // List transfers
    @Override
    public List<Transfer> list() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    // Get transfers
    @Override
    public Transfer get(int transferId) throws TransferNotFoundException {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfer WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

        // Create a transfer
    @Override
    public Transfer createTransfer(int accountFrom, int accountTo, BigDecimal amount) {
        Transfer transfer = new Transfer();
        String sql = "BEGIN TRANSACTION;" +
                "UPDATE account SET balance = balance - ? WHERE account_id = ?;" +
                "UPDATE account SET balance = balance + ? WHERE account_id = ?;" +
                "INSERT INTO transfer_type (transfer_type_desc) " +
                "VALUES ('Send');" +
                "INSERT INTO transfer_status (transfer_status_desc) " +
                "VALUES ('Approved');" +

                "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) " +
                "VALUES ((SELECT transfer_type_id FROM transfer_type " +
                        "WHERE transfer_type_desc = 'Send' ORDER BY transfer_type_id DESC LIMIT 1)," +
                        "(SELECT transfer_status_id FROM transfer_status " +
                        "WHERE transfer_status_desc = 'Approved' ORDER BY transfer_status_id DESC LIMIT 1)," +
                        " ?, ?, ?) RETURNING transfer_id;" +
                "COMMIT;" +
                "SELECT * FROM transfer ORDER BY transfer_id DESC LIMIT 1;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, amount, accountFrom, amount, accountTo,
                                                    accountFrom, accountTo, amount);
        if(result.next()) {
            transfer = mapRowToTransfer(result);
        }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }


}
