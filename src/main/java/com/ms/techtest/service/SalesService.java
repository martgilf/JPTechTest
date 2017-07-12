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
	private static final int PAUSE      = 1000;
	private int saleCount 	= 1;
	
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
		BigDecimal adjAmountBD = BigDecimal.valueOf(adjustment.getAmount());
		BigDecimal quantityBD = new BigDecimal(sale.getQuantity());
		BigDecimal value = sale.getValue();
		value = calculateNewValue(adjustment, adjAmountBD, quantityBD, value);
		sale.setValue(value);
	}

	private BigDecimal calculateNewValue(AdjustmentTransaction adjustment, BigDecimal adjAmountBD,
			BigDecimal quantityBD, BigDecimal value) {
		BigDecimal calculatedValue = BigDecimal.valueOf(value.doubleValue());
		switch (adjustment.getOperation()) {
			case ADD: {
				calculatedValue = value.add(adjAmountBD.multiply(quantityBD));
				break; 
			}
			case SUBTRACT: {
				calculatedValue = value.subtract(adjAmountBD.multiply(quantityBD));
				break; 
			}
			case MULTIPLY: {
				calculatedValue = value.multiply(adjAmountBD);
				break;
			}
		}
		return calculatedValue;
	}
	
	private void report() {
		if (saleCount % numSalesForReport == 0 ) {
			salesByProductReport.report();
		}
		if (saleCount % numAdjustmentsForReport == 0 ) {
			Logger.log(APP_PAUSING_MESSAGE);
			salesAdjustmentReport.report();
			try {
				Thread.sleep(PAUSE);
			} 
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			saleCount=0;
		}		
		saleCount++;
	}
	
}
