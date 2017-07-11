package com.ms.techtest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.ms.techtest.domain.AdjustmentTransaction;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;
import com.ms.techtest.service.SalesService;
import com.ms.techtest.types.OperationType;
import com.ms.techtest.util.Logger;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONArray;

public class ProcessMessage {

	private static final int ADJUSTMENT_REPORT_COUNT = 50;
	private static final int SALE_REPORT_COUNT = 10;
	private static final long DEFAULT_QUANTITY = 1L;
	private static final String ADJUSTMENT	= "adjustment";
    private static final String PRODUCT		= "product";
	private static final String AMOUNT		= "amount";
	private static final String QUANTITY	= "quantity";
    private static final String IDENTIFIER	= "id";
    private static final String OPERATION	= "operation"; 
    private static final String COMPANY		= "company"; 
    private static final String SALE		= "sale"; 
	
	public static List<String> messages = new ArrayList<String> () ;
	
	public static void main(String[] args) throws Exception  {		 		
		JSONArray messages = readMessagesFromFile(args); 	    
		processMessages(messages);   
    }

	private static void processMessages(JSONArray messages) {
		ProcessMessage processor = new ProcessMessage() ; 	
		for (Object message : messages)
        {
			try {
				processor.process((JSONObject)message);	  
			}
			catch (Exception e) {
				//skip message and process next
				Logger.log("Skipping message:%s:%s%n", e.getMessage(),((JSONObject)message).toJSONString());
				continue;
			}
        }
	}

	private static JSONArray readMessagesFromFile(String[] args) throws Exception {
		JSONArray messages = null;
		try {
			messages = parseFile(args[0]);
		} 
        catch (Exception e) {
        	Logger.log("Problem processing file or contents:%s", e.getMessage()); 
            throw e;
        }
		return messages;
	}
	
	public static JSONArray parseFile(String fileName) throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
    	return (JSONArray)parser.parse(new FileReader(fileName));         
	}
	

	public void process( JSONObject sale) throws Exception {
		Product product;
		SaleTransaction saleTransaction;
		AdjustmentTransaction adjustmentTransaction;
		SalesService salesService = new SalesService(SALE_REPORT_COUNT, ADJUSTMENT_REPORT_COUNT);      

		product = createProduct(sale);
		saleTransaction = createSaleTransaction(sale);
		adjustmentTransaction = createAdjustmentTransaction(sale);
		salesService.processSale(product, saleTransaction, adjustmentTransaction);		
	}
	
	public SaleTransaction createSaleTransaction(JSONObject jmessage) throws Exception {
		JSONObject jsale = null;
		try{	
			jsale = (JSONObject) jmessage.get(SALE);
			long saleQuantity = DEFAULT_QUANTITY;
			String company = (String) jmessage.get(COMPANY); 
	        long identifier = (Long) jmessage.get(IDENTIFIER); 		
	        double saleAmount = (Double) jsale.get(AMOUNT);
	        if ((Long) jsale.get(QUANTITY) != null) {
	        	saleQuantity = (Long) jsale.get(QUANTITY);
	        }
	        return new SaleTransaction(company, identifier, saleAmount, saleQuantity);
		}
	    catch (Exception  e) {
        	Logger.log("createSaleTransaction:Problem parsing message:%s%n", e.getMessage()); 
        	Logger.log("createSaleTransaction:Message:%s%n", jsale.toJSONString());
        	throw e;
	    }
	}
	
	public AdjustmentTransaction createAdjustmentTransaction(JSONObject jmessage) throws Exception {		
		JSONObject jadjustment = null;
		try {
			jadjustment = (JSONObject) jmessage.get(ADJUSTMENT);
			if (jadjustment == null) {
				return null;
			}
		    String company = (String) jmessage.get(COMPANY);
		    long identifier = (Long) jmessage.get(IDENTIFIER); 
		    String adjustmentOperation = (String) jadjustment.get(OPERATION);
		    if ( ! ( adjustmentOperation.equals(OperationType.ADD.getOperationTypeCode())
		    		 || adjustmentOperation.equals(OperationType.SUBTRACT.getOperationTypeCode())
		    		 || adjustmentOperation.equals(OperationType.MULTIPLY.getOperationTypeCode()) 
		            ) 
		    	) {
		    	throw new Exception("Invalid adjustment operation");
		    }
	        double adjustmentAmount = (Double) jadjustment.get(AMOUNT);
	        return new AdjustmentTransaction(company, identifier, adjustmentOperation, adjustmentAmount) ;
		}
	    catch (Exception e ) {
	    	Logger.log("createAdjustmentTransaction:Problem parsing message:%s%n", e.getMessage()); 
	    	Logger.log("createAdjustmentTransaction:Message:%s%n", jadjustment);
	    	throw e;
	    } 
 	}
	
	public Product createProduct(JSONObject jmessage) throws Exception {
		try {
			String product = (String) jmessage.get(PRODUCT);
			return new Product(product);
		}
		catch (Exception e) {
	    	Logger.log("createProduct:Problem parsing message:%s%n", e.getMessage()); 
	    	throw e;
	    }
	}
}
	
