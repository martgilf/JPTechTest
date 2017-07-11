package com.ms.techtest.repository;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.ms.techtest.domain.AdjustmentTransaction;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;
import com.ms.techtest.util.Logger;

public class TransactionRepository implements Repository {

	private static final String DUPLICATE_TRANSACTION = "Duplicate transaction";
	private static final String SAVE_FAILED = "Save:Failed";
	private Map<Product,List<Transaction>> store = new LinkedHashMap<>();
	private static TransactionRepository instance;
	 
    public static synchronized Repository getInstance(){
        if(instance == null){
            instance = new TransactionRepository();
        }
        return instance;
    }
    
	@Override
	public void save(Product product, Transaction transaction) throws Exception{
		if ( store == null || ! store.containsKey(product)) {
			List<Transaction> list = new LinkedList<>();
			list.add(transaction);
			store.put(product, list);
		}
		else if ( store.get(product).contains(transaction)) {
			Logger.log("%n%n:%s:%s:%n%n", SAVE_FAILED, product, transaction );
			throw new Exception(DUPLICATE_TRANSACTION);
		}
		else {
			List<Transaction> list = store.get(product);
			list.add(transaction);
		}
	}
	
	@Override
	public Transaction find(Product product, Transaction transaction){
		int idx = store.get(product).lastIndexOf(transaction);
		return store.get(product).get(idx);	
	}
	
	@Override
	public Iterator<Product> findAllProduct() {
		return store.keySet().iterator();	
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<Transaction> findAllTransactionsByProduct(Product product, TransactionType transactionType, OrderType orderType){
		List<Transaction> result =  new LinkedList<>();
		List<Transaction> trans = store.get(product);		
		
		for ( Transaction transaction : trans) {
			if ( transactionType == TransactionType.SALE && transaction instanceof SaleTransaction) {
				result.add((SaleTransaction) transaction);
			}
			else if (transactionType == TransactionType.ADJUSTMENT && transaction instanceof AdjustmentTransaction){
				result.add((AdjustmentTransaction) transaction);
			}	
		}
		
		if (orderType==OrderType.ASC) {
			return (Iterator<Transaction>) result.iterator();
		}
		else {
			return (Iterator<Transaction>) ((Deque<Transaction>)result).descendingIterator();
		}
	}

	@Override
	public void deleteAll() {
		((LinkedHashMap<Product, List<Transaction>>) store).clear();	
	}

	@Override
	public long getCountOfAllTransactions() {
		long count = 0L;
		for ( Product p: store.keySet()) {
			count+=store.get(p).size();
		}
		return count;
	}

}
