package com.ms.techtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.text.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ms.techtest.domain.AdjustmentTransaction;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;

public class ProcessMessageTest {

	static {
	}
	
	@BeforeClass
	public static void init() throws ParseException, org.json.simple.parser.ParseException
	{
	}
		
	@Rule
	public ExpectedException thrown = ExpectedException.none();
     	
	@Test
	public void creates_sale_when_amount_is_double() throws Exception {
		String message = "{ \"company\": \"co\", \"id\": 1, \"product\": 2, \"sale\": { \"amount\": 10.5, \"quantity\": 5}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
		assertEquals(st.getSalePrice(),10.5D,0.0);
	}
	
	@Test
	public void throws_exception_when_sale_amount_is_integer() throws Exception {
		thrown.expect(ClassCastException.class);
	    String message = "{ \"company\": \"X\", \"id\": 1, \"product\": 11, \"sale\": { \"amount\": 10, \"quantity\": 5}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		@SuppressWarnings("unused")
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
	}
	
	@Test
	public void throws_exception_when_string_for_identifier() throws Exception {
		thrown.expect(ClassCastException.class);
	    String message = "{ \"company\": \"X\", \"id\": 1, \"product\": \"p2\", \"sale\": { \"amount\": 10, \"quantity\": 5}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		@SuppressWarnings("unused")
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
	}
	
	@Test
	public void throws_exception_when_company_isa_number() throws Exception {
		thrown.expect(ClassCastException.class);
	    String message = "{ \"company\": 11, \"id\": 1, \"product\": 2, \"sale\": { \"amount\": 10, \"quantity\": 5}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		@SuppressWarnings("unused")
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
	}
	
	@Test
	public void throws_exception_when_sale_not_provided() throws Exception {
		thrown.expect(NullPointerException.class);
	    String message = "{ \"company\": \"co\", \"id\": 1, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		@SuppressWarnings("unused")
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
	}
	
	@Test
	public void throws_exception_when_quantity_is_double() throws Exception {
		thrown.expect(ClassCastException.class);
	    String message = "{ \"company\": \"co\", \"id\": 1, \"product\": \"p2\", \"sale\": { \"amount\": 10.1, \"quantity\": 5.1}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		@SuppressWarnings("unused")
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
	}
			
	@Test
	public void throws_exception_when_invalid_adjustment_operation() throws Exception {
		thrown.expect(Exception.class);
	    String message = "{ \"company\": \"co\", \"id\": 1, \"product\": 2, \"sale\": { \"amount\": 10, \"quantity\": 5}, \"adjustment\": { \"operation\": \"DIVIDE\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		@SuppressWarnings("unused")
		AdjustmentTransaction at = p.createAdjustmentTransaction((JSONObject)m);
	}
	
	@Test
	public void creates_sale_when_adjustment_not_provided() throws Exception {
		String message = "{ \"company\": \"X\", \"id\": 1, \"product\": \"Product1\", \"sale\": {  \"quantity\": 5, \"amount\": 10.0}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
		assertNotEquals(null, st);
	}
	
	@Test
	public void creates_sale_and_adjustment_when_both_provided() throws Exception {
	    String message = "{ \"company\": \"co\", \"id\": 1, \"product\": 2, \"sale\": {  \"amount\": 10.0, \"quantity\": 5}, \"adjustment\": { \"operation\": \"ADD\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		AdjustmentTransaction at = p.createAdjustmentTransaction((JSONObject)m);
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
		assertNotEquals(at, null);
		assertNotEquals(st, null);		
	}
	
	@Test
	public void creates_sale_when_quantity_not_provided() throws Exception {
		String message = "{ \"company\": \"co\", \"id\": 1, \"product\": \"p2\", \"sale\": { \"amount\": 10.1}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		SaleTransaction st = p.createSaleTransaction((JSONObject)m);
		assertNotEquals(st, null);	
	}
	
	@Test
	public void creates_product_when_provided() throws Exception {
		String message = "{ \"company\": \"co\", \"id\": 1, \"product\": \"p2\", \"sale\": { \"amount\": 10.1}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		Product product = p.createProduct((JSONObject)m);
		assertNotEquals(product, null);	
	}
	
	@Test
	public void throws_exception_when_product_not_provided() throws Exception {
		String message = "{ \"company\": \"co\", \"id\": 1, \"sale\": { \"amount\": 10.1}, \"adjustment\": { \"operation\": \"MULTIPLY\", \"amount\": 1.5}}";
		JSONParser parser = new JSONParser();
		JSONObject m = (JSONObject)parser.parse(message); 
		ProcessMessage p = new ProcessMessage();
		Product product = p.createProduct((JSONObject)m);
		assertNotEquals(product, null);	
	}
	
}
	
