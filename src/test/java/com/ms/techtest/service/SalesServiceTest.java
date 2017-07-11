package com.ms.techtest.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ms.techtest.ProcessMessage;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.repository.TransactionRepository;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;

public class SalesServiceTest  {
	
	private static TransactionRepository repo = (TransactionRepository) TransactionRepository.getInstance();
	
	@BeforeClass
	public static void init() throws Exception
	{		
	}

	@Before
	public void beforeEachTest() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("testFile2.jsn").getFile());		
		JSONParser parser = new JSONParser();
		JSONArray messages = (JSONArray)parser.parse(new FileReader(file.getAbsolutePath()));         
    	ProcessMessage pm = new ProcessMessage();
    	for (Object message : messages)
        {
        	//System.out.println("\nSale:"+((JSONObject)message).toJSONString());
        	pm.process((JSONObject)message);
        }
	}

	@After
	public void afterEachTest() {
		repo.deleteAll();
	}

	@Test
	public void transaction_count_correct_after_processing() throws ParseException {
		assertEquals(17L, repo.getCountOfAllTransactions());
	}
	
	@Test
	public void product_total_correct_after_addition_adjustment() throws ParseException {
		assertEquals(107.50D, getProductTotal(new Product("Pear"))/100,0.00D);
	}
	
	@Test
	public void product_total_correct_after_subtract_adjustment() throws ParseException {
		assertEquals(95.00D,getProductTotal(new Product("Orange"))/100,0.00D);
	}
	
	@Test
	public void product_total_correct_after_multiply_adjustment() throws ParseException {
		assertEquals(150.00D,getProductTotal(new Product("Apple"))/100,0.00D);
	}
		
	@Test
	public void product_total_correct_after_mutiple_adjustments() throws Exception {
		assertEquals(150.00D,getProductTotal(new Product("Banana"))/100,0.00D);
	}	
	
	static double getProductTotal(Product product){
		Iterator<Transaction> it = repo.findAllTransactionsByProduct(product, TransactionType.SALE, OrderType.ASC);
		double total = 0D;
		while ( it.hasNext()) {
			SaleTransaction s = (SaleTransaction) it.next();
			total+=s.getValue().doubleValue();
		}
		System.out.println("Total:"+total);
		return total;
	}
}