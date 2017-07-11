package com.ms.techtest.report;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SalesByProductReportTest {

	@BeforeClass
	public static void initCalculator() {
		//adjustment = new ProductAdjustment( "Apple", OperationType.ADD, 30L);
	}

	@Before
	public void beforeEachTest() {
		//System.out.println("This is executed before each Test");
	}

	@After
	public void afterEachTest() {
		//System.out.println("This is exceuted after each Test");
	}

	@Ignore
	@Test
	public void testgetProduct() {
		//assertEquals("Apple", result);
	}
}