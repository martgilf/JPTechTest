package com.ms.techtest.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductTest {
	
	static Product p1;
	static Product p2;
	static Product p3;
	static Product p4;
	
	@BeforeClass
	public static void init() {
		p1 = new Product("Product1");
		p2 = new Product("Product2");
		p3 = new Product("Product1");
		p4 = p1;
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
	public void returns_the_correct_product_name() {
		assertEquals("Product1", p1.getName());
	}
	
	@Test
	public void products_with_the_same_name_are_equal() {
		assertEquals(true, p1.equals(p3));
	}
	
	@Test
	public void products_with_different_names_are_not_equal() {
		assertNotEquals(true, p1.equals(p2));
	}
	
	@Test
	public void equal_product_references_are_equal() {
		assertEquals(true, p1.equals(p4));
	}
	
	@Test
	public void equal_product_references_return_same_hashcode() {
		assertEquals(p1.hashCode(),p4.hashCode());
	}
	
	@Test
	public void different_products_return_different_hashcode() {
		assertNotEquals(p1.hashCode(),p2.hashCode());
	}
	
	@Test
	public void returns_correct_string_representation() {
		assertEquals(":<Product:Product1>",p1.toString());
	}
}