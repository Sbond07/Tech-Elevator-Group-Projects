package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
// TODO Code review Question
// Get transfers with strings
//    @Override
//    public Transfer get(int transferId) throws TransferNotFoundException {
//        Transfer transfer = null;
//        String sql = "SELECT t.transfer_id, tu.username , (SELECT tu.username FROM tenmo_user tu " +
//                "JOIN account a ON a.user_id = tu.user_id " +
//                "JOIN transfer t ON t.account_to = a.account_id " +
//                "WHERE transfer_id = ?), tt.transfer_type_desc,  ts.transfer_status_desc, amount " +
//                "FROM transfer t " +
//                "RIGHT JOIN account a ON t.account_from = a.account_id " +
//                "JOIN tenmo_user tu ON tu.user_id = a.user_id " +
//                "JOIN transfer_status ts ON ts.transfer_status_id = t.transfer_status_id " +
//                "JOIN transfer_type tt ON tt.transfer_type_id = t.transfer_type_id " +
//                "WHERE transfer_id = ?;";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId, transferId);
//        if (results.next()) {
//            transfer = mapRowToTransferString(results);
//        } else {
//            throw new TransferNotFoundException();
//        }
//        return transfer;
//    }

    // Get transfers
    @Override
    public Transfer get(int transferId) throws TransferNotFoundException {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, " +
                "transfer_status_id, account_from, account_to, " +
                "amount FROM transfer WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

    // Get transfers by user ID
    @Override
    public List<Transfer> getByUserId(int userId) throws TransferNotFoundException {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount FROM transfer t " +
                "JOIN account a " +
                "ON a.account_id = t.account_from " +
                "WHERE a.user_id = ?; ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

        // Create a transfer
    @Transactional
    @Override
    public Transfer createTransfer(int accountFrom, int accountTo, BigDecimal amount) throws TransferNotFoundException {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountFrom);

        sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountTo);

        sql =   "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) " +
                "VALUES ((SELECT transfer_type_id FROM transfer_type " +
                        "WHERE transfer_type_desc = 'Send'), " +
                        "(SELECT transfer_status_id FROM transfer_status " +
                        "WHERE transfer_status_desc = 'Approved')," +
                        " ?, ?, ?) RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, accountFrom, accountTo, amount);

        return get(transferId);
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

//  TODO   Code review question
// map method for returning strings in search by transfer_id
//    private TransferString mapRowToTransferString(SqlRowSet rs) {
//        User user = new User();
//        TransferString transferString = new TransferString();
//
//        transferString.setTransferId(rs.getInt("transfer_id"));
//        user.setUsername(rs.getString("username"));
//        user.setUsername(rs.getString("username"));
//        transferString.setTransferTypeDesc(rs.getString("transfer_type_desc"));
//        transferString.setTransferStatusDesc(rs.getString("transfer_status_desc"));
//        transferString.setAmount(rs.getBigDecimal("amount"));
//        return transferString;
//    }

}
