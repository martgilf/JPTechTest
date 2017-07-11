package com.ms.techtest.domain;

import java.util.Objects;

public abstract class Transaction {

	protected long identifier;
	protected String company;
	
	public Transaction(String company, long identifier) {
		this.company = company;
		this.identifier = identifier;
	}
		
	public Long getIdentifier() {
		return identifier;
	}
	
	public String getCompany() {
		return company;
	}
	
	@Override
	public String toString() {
		return String.format(":<Transaction:%s:%s>", company, identifier);
	}
	
	@Override
    public boolean equals(Object o) {
        if ( o == this ) {
        	return true;
        }
        else if ( !(o instanceof Transaction)) {
        	return false;
        }
        else {
        	Transaction t = (Transaction) o;
        	return identifier == t.identifier &&
                   Objects.equals(company, t.company) &&
                   o != this;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, company, this);
    }
}
