package com.ms.techtest.domain;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SaleTransactionTest {
	
	static SaleTransaction st1;
		
	@BeforeClass
	public static void init() {
		st1 = new SaleTransaction("Company1", 999L, 1.2D, 99L);
	}

	@Before
	public void beforeEachTest() {
		//System.out.println("This is executed before each Test");
	}

	@After
	public void afterEachTest() {
		//System.out.println("This is exceuted after each Test");
	}

	@Test
	public void returns_correct_object_access_values() {
		assertEquals("Company1", st1.getCompany());
		assertEquals(999L, st1.getIdentifier().longValue());
		assertEquals(1.2D, st1.getSalePrice(),0.00);
		assertEquals(99L, st1.getQuantity());
		assertEquals(118.80D, st1.getValue().doubleValue(),0.00);
	}
	
	@Test
	public void conversion_toString() {
		System.out.println(st1);
		assertEquals(":<Sale:<Transaction:Company1:999>:CurrentValue=118.8:Quantity=99:InitialPrice=1.2>", st1.toString());		
	}
}