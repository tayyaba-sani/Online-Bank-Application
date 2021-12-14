package com.accounts.accounts.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Accounts extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String branchAddress;

    private double actualBalance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accounts")
    private List<Operation> operations =  new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public double getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(double actualBalance) {
        this.actualBalance = actualBalance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @JsonManagedReference
    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
    public void doOperation(Operation operation) {
        getOperations().add(operation);
        operation.setAccount(this);
    }
    public Accounts removeOperation(Operation operation) {
        getOperations().remove(operation);
        operation.setAccount(null);
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}
