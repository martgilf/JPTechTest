package com.ms.techtest.domain;

import java.util.Objects;

public class Product {
	private String name;

	public Product(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return ":<Product:" + name + ">";
	}
	
	@Override
    public boolean equals(Object o) {

        if (o == this) { 
        	return true;
        }
        else if (!(o instanceof Product)) {
        	return false;
        }
        else {
        	Product p = (Product) o;
        	return name.equals(p.getName());
        } 
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
  
}
