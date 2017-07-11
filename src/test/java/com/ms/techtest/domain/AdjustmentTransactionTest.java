package com.ms.techtest.domain;

import static org.junit.Assert.assertEquals;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ms.techtest.types.OperationType;

public class AdjustmentTransactionTest {
	
	static AdjustmentTransaction at1;
	
	@BeforeClass
	public static void init() {
		at1 = new AdjustmentTransaction("Company1", 999L, "ADD",2.1D);	
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
		assertEquals("Company1", at1.getCompany());
		assertEquals(999L, at1.getIdentifier().longValue());
		assertEquals(OperationType.ADD, at1.getOperation());
		assertEquals(2.1D, at1.getAmount(),0.00);
	}
	
	@Test
	public void returns_correct_conversion_toString() {
		System.out.println(at1);
		assertEquals(":<Adjustment:<Transaction:Company1:999>:ADD:2.1>", at1.toString());		
	}
}