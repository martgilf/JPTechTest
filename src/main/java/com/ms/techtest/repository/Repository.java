package com.ms.techtest.repository;

import java.util.Iterator;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;

public interface Repository {

	void save(Product product, Transaction transaction) throws Exception;

	Transaction find(Product product, Transaction transaction);

	Iterator<Product> findAllProduct();

	Iterator<Transaction> findAllTransactionsByProduct(Product product, TransactionType transactionType, OrderType orderType);

	void deleteAll();

	long getCountOfAllTransactions();

}