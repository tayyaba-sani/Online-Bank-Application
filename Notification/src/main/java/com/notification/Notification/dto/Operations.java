package com.notification.Notification.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class Operations extends BaseEntity {

    private Long id;
    private String operationId;

    private double amount;

    private OperationType typeOperation;


    private Accounts accounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OperationType getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(OperationType typeOperation) {
        this.typeOperation = typeOperation;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }
}
