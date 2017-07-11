package com.ms.techtest.domain;

import java.util.Objects;

import com.ms.techtest.types.OperationType;

public class AdjustmentTransaction extends Transaction {
	
	private OperationType operation;
	private double amount;
	
	public AdjustmentTransaction(String company, long identifier, String operation, double amount) {
		super(company, identifier);
		this.amount = amount;		
		if (operation.equals(OperationType.ADD.getOperationTypeCode())) {
			this.operation=OperationType.ADD;
		} 
		else if (operation.equals(OperationType.SUBTRACT.getOperationTypeCode())) {
			this.operation=OperationType.SUBTRACT;
		}
		else if (operation.equals(OperationType.MULTIPLY.getOperationTypeCode())) {
			this.operation=OperationType.MULTIPLY;
		}
	}
	
	public OperationType getOperation() {
		return operation;
	}
	
	public double getAmount() {
		return amount;
	}
	
	@Override
	public String toString() {
		return ":<Adjustment" + super.toString() + String.format(":%s:%s>", operation, amount);
	}
	
	@Override
    public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		else if (!(o instanceof AdjustmentTransaction)) {
            return false;
        }
		else {
        	AdjustmentTransaction s = (AdjustmentTransaction) o;
        	return identifier == s.identifier &&
             	   company.equals(s.company); 
        }          	
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, company, this);
    }
		
}
