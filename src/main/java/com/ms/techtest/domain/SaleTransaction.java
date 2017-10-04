package com.ms.techtest.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class SaleTransaction extends Transaction {
	
	private static final long DEFAULT_QUANTITY = 1;	
	private double salePrice;
	private BigDecimal value;	
	private long quantity;
	
	public SaleTransaction(String company, long identifier, double price){
		this(company, identifier, price, DEFAULT_QUANTITY) ;
	}
	
	public SaleTransaction(String company, long identifier, double price, Long quantity){
		super(company, identifier);
		this.salePrice = price;
		this.quantity = quantity;	
		BigDecimal priceBD = BigDecimal.valueOf(price);
		BigDecimal quantityBD = new BigDecimal(quantity);
		this.value = priceBD.multiply(quantityBD);		
	}
		
	public double getSalePrice() {
		return salePrice;
	}
	
	public long getQuantity() {
		return quantity;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ":<Sale" + super.toString() + String.format(":CurrentValue=%s:Quantity=%s:InitialPrice=%s>", value.doubleValue(), quantity, salePrice);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		else if (!(o instanceof SaleTransaction)) {
            return false;
        }
		else { 
			SaleTransaction s = (SaleTransaction) o;
			return identifier == s.identifier &&
									company.equals(s.company); 
		}
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, company, this.getClass().getName());
    }
}
