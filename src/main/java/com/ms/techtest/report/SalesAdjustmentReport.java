package com.ms.techtest.report;

import java.util.Iterator;

import com.ms.techtest.domain.AdjustmentTransaction;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.repository.Repository;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;
import com.ms.techtest.util.Logger;

public class SalesAdjustmentReport extends Report {
	
	//After 50 messages your application should log 
	//that it is pausing, stop accepting new messages 
	//and log a report of the adjustments that have 
	//been made to each sale type while the application was running. 
	
	private static final String REPORT_HEADER = "%nSales Adjustment Report%n=======================%n";
	private static final String NEWLINE_ADJUSTMENT_SEPARATOR = "%n      %s:%s, ";
	private static final String ADJUSTMENT_SEPARATOR = "%s:%s, ";
	private static final String PRODUCT_SEPARATOR = "%n%s:%n      ";
	private static final int NUMBER_OF_ADJUSTMENTS_PER_LINE = 3;
	//private int frequency = 50;
	
	public SalesAdjustmentReport(Repository salesRepository) {
		repository = salesRepository;
	}
	
	@Override
	public void report() {	
		Logger.log(REPORT_HEADER);
		Iterator<Product> products = repository.findAllProduct();
		while ( products.hasNext()) {
			Product product = products.next();
			Logger.log(PRODUCT_SEPARATOR, product.getName());
			reportProductAdjustments(product);
		}
	}

	private void reportProductAdjustments(Product product) {
		Iterator<Transaction> adjs = repository.findAllTransactionsByProduct(product, TransactionType.ADJUSTMENT, OrderType.ASC);
		for ( int i=0; adjs.hasNext(); i++) {
			AdjustmentTransaction adj = (AdjustmentTransaction) adjs.next();
			if ( i==0 | i % NUMBER_OF_ADJUSTMENTS_PER_LINE != 0 ) {
				Logger.log(ADJUSTMENT_SEPARATOR, adj.getOperation(), adj.getAmount());
			} else {
				Logger.log(NEWLINE_ADJUSTMENT_SEPARATOR, adj.getOperation(), adj.getAmount());
				i=0;
			}				
		}
	}	
}
