package com.ms.techtest.report;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;

import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.repository.Repository;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;
import com.ms.techtest.util.Logger;

public class SalesByProductReport extends Report{

	//After every 10th message received your application should 
	//log a report detailing the number of sales of each 
	//product and their total value
	
	private static final String REPORT_HEADER = "%n%nSales Report%n============%n";
	
	public SalesByProductReport(Repository saleRepository) {
		repository = saleRepository;
	}
	
	public void report() {		
		Logger.log(REPORT_HEADER);
		Iterator<Product> products = repository.findAllProduct();
		while( products.hasNext() ) {
			Product product = products.next();
			reportproductTotalSale(product);
		}
	}

	private void reportproductTotalSale(Product product) {
		BigDecimal total;
		Iterator<Transaction> sales = repository.findAllTransactionsByProduct(product, TransactionType.SALE, OrderType.ASC);
		total = new BigDecimal(0L);
		while ( sales.hasNext()) {
			SaleTransaction sale = (SaleTransaction) sales.next();
			total = total.add(sale.getValue());		
		}
		System.out.printf("%-25s: Total Sale:%s%n", product.getName(), NumberFormat.getCurrencyInstance().format(total.divide(new BigDecimal(100L),2, BigDecimal.ROUND_HALF_UP)));
	}
	
}
