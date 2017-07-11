package com.ms.techtest.service;

import com.ms.techtest.domain.AdjustmentTransaction;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.report.Report;
import com.ms.techtest.report.SalesAdjustmentReport;
import com.ms.techtest.report.SalesByProductReport;
import com.ms.techtest.repository.TransactionRepository;
import com.ms.techtest.repository.Repository;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;
import com.ms.techtest.util.Logger;

import java.math.BigDecimal;
import java.util.Iterator;

public class SalesService  {

	private static final String FAILED_TO_STORE_TRANSACTION = "Failed to store transaction%n";
	private static final String APP_PAUSING_MESSAGE = "%nApplication is pausing........%n";
	public static int SALE_COUNT 	= 1;
	private static int PAUSE        = 1000;
	
	private int numAdjustmentsForReport;
	private int numSalesForReport;
	
	private Repository repository = TransactionRepository.getInstance();
	private SalesByProductReport salesByProductReport = new SalesByProductReport(repository);
	private Report salesAdjustmentReport = new SalesAdjustmentReport(repository);
	
	public SalesService(int numSalesForReport, int numAdjustmentsForReport){
		this.numAdjustmentsForReport = numAdjustmentsForReport;
		this.numSalesForReport = numSalesForReport;
	}
	
	public void processSale(Product product, SaleTransaction saleTransaction,
							AdjustmentTransaction adjustmentTransaction) throws Exception {
		try {
			//saves and process adjustment first
			//then saves the sale so that the adjustment
			//is not applied to the current sale
			if (adjustmentTransaction!=null) {
				repository.save(product, adjustmentTransaction);
			}			
		} catch (Exception e) {
			Logger.log(FAILED_TO_STORE_TRANSACTION);
			throw e;
		}
		applyAdjustmentToProduct(product, adjustmentTransaction);
		repository.save(product, saleTransaction);
		report();
	}
	
	public void applyAdjustmentToProduct(Product product, AdjustmentTransaction adjustmentTransaction){ 
		if (adjustmentTransaction==null) {
			return;
		}		
		Iterator<Transaction> sales = repository.findAllTransactionsByProduct(product, TransactionType.SALE, OrderType.ASC);
		while ( sales.hasNext() ){
			SaleTransaction sale = (SaleTransaction) sales.next();
			applyAdjustmentToProductSale(sale,adjustmentTransaction);
		}
	}
	
	public void applyAdjustmentToProductSale(SaleTransaction sale, AdjustmentTransaction adjustment){
		BigDecimal adjAmountBD = new BigDecimal(adjustment.getAmount());
		BigDecimal quantityBD = new BigDecimal(sale.getQuantity());
		BigDecimal value = sale.getValue();
		switch (adjustment.getOperation()) {
			case ADD: {
				value = value.add(adjAmountBD.multiply(quantityBD));
				//Logger.log("Value:%f%n",(adjAmountBD.multiply(quantityBD)).doubleValue());
				break; 
			}
			case SUBTRACT: {
				value = value.subtract(adjAmountBD.multiply(quantityBD));
				//Logger.log("Value:%f%n",value);
				break; 
			}
			case MULTIPLY: {
				value = value.multiply(adjAmountBD);
				//Logger.log("Value:%f%n",value);
				break;
			}
		}
		sale.setValue(value);
	}
	
	private void report() {
		if (SALE_COUNT % numSalesForReport == 0 ) {
			salesByProductReport.report();
		}
		if (SALE_COUNT % numAdjustmentsForReport == 0 ) {
			Logger.log(APP_PAUSING_MESSAGE);
			salesAdjustmentReport.report();
			try {
				Thread.sleep(PAUSE);
			} 
			catch (InterruptedException e) {
			}
			SALE_COUNT=0;
		}		
		SALE_COUNT++;
	}
	
}
