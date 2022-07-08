package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class TransferDTO {

    @NotEmpty
    private int accountFrom;
    @NotEmpty
    private int accountTo;
    @NotEmpty
    private BigDecimal amount;

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
