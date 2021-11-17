package com.accounts.accounts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "operations")
public class Operation extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String operationId;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType typeOperation;

    @ManyToOne
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

    @JsonBackReference
    public Accounts getAccount() {
        return accounts;
    }

    public void setAccount(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Operation))
            return false;
        return
                id != null &&
                        id.equals(((Operation) obj).getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
