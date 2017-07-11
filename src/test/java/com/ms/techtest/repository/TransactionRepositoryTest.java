package com.ms.techtest.repository;

import static org.junit.Assert.*;

import java.util.Iterator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.ms.techtest.domain.AdjustmentTransaction;
import com.ms.techtest.domain.Product;
import com.ms.techtest.domain.SaleTransaction;
import com.ms.techtest.domain.Transaction;
import com.ms.techtest.types.OrderType;
import com.ms.techtest.types.TransactionType;

public class TransactionRepositoryTest {

	static Product p1, p2, p3;
	static SaleTransaction st1, st2;
	static AdjustmentTransaction at1, at2;
	static Repository repo;
	
	@BeforeClass
	public static void init() {
		repo = TransactionRepository.getInstance();	
		p1 = new Product("Product1");
		p2 = new Product("Product2");
		p3 = new Product("Product1");
		st1 = new SaleTransaction("Company1", 998L, 1.1D);
		st2 = new SaleTransaction("Company1", 999L, 1.2D, 99L);
		at1 = new AdjustmentTransaction("Company1", 998L, "ADD",2.1D);
		at2 = new AdjustmentTransaction("Company1", 999L, "SUBTRACT",2.0D);
	}

	@Before
	public void beforeEachTest() {		
	}

	@After
	public void afterEachTest() {
		repo.deleteAll();
	}
	
	@Test
   	public void can_save_sale_transaction() throws Exception {     
       	repo.save(p1, st1);
       	assertEquals(repo.getCountOfAllTransactions(),1L);
   	}
	
	@Test
   	public void can_save_adjustment_transaction() throws Exception {     
       	repo.save(p1, at1);
       	assertEquals(repo.getCountOfAllTransactions(),1L);
   	}

	
	@Test
	public void transaction_count_correct_when_transactions_saved() {
		try {
			repo.save(p1, st1);
			repo.save(p1, at1);
			assertEquals(repo.getCountOfAllTransactions(),2L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Rule
	public ExpectedException thrown = ExpectedException.none();
    
    @Test
	public void exception_thrown_when_duplicate_transaction_submitted() throws Exception {
    	thrown.expect(Exception.class);
        thrown.expectMessage("Duplicate transaction");       
    	repo.save(p1, st1);
		repo.save(p1, at1);
		repo.save(p1, at1);
	}
    
    @Test
   	public void can_findAllProducts() throws Exception {     
       	repo.save(p1, st1);
   		repo.save(p1, st2);
   		repo.save(p1, at1);
   		repo.save(p2, at2);
   		Iterator<Product> it = repo.findAllProduct();
   		for (int i=0;it.hasNext();i++) {
   			Product p = (Product) it.next();
   			if (i==0) {
   				assertEquals(p,p1);
   			} 
   			else if (i==1){
   				assertEquals(p,p2);
   			}
   		}
   	}
    
    @Test
   	public void can_finds_all_adjustment_transactions_by_product() throws Exception {     
       	repo.save(p1, st1);
   		repo.save(p1, st2);
   		repo.save(p1, at1);
   		repo.save(p1, at2);
   		Iterator<Transaction> it = repo.findAllTransactionsByProduct(p1, TransactionType.ADJUSTMENT, OrderType.ASC);
   		for (int i=0;it.hasNext();i++) {
   			AdjustmentTransaction trans = (AdjustmentTransaction) it.next();
   			if (i==0) {
   				assertEquals(trans,at1);
   			} 
   			else if (i==1){
   				assertEquals(trans,at2);
   			}
   		}
   	}
    
    @Test
   	public void can_finds_all_sale_transactions_by_product() throws Exception {     
       	repo.save(p1, st1);
   		repo.save(p1, st2);
   		repo.save(p1, at1);
   		repo.save(p1, at2);
   		Iterator<Transaction> it = repo.findAllTransactionsByProduct(p1, TransactionType.SALE, OrderType.ASC);
   		for (int i=0;it.hasNext();i++) {
   			SaleTransaction trans = (SaleTransaction) it.next();
   			if (i==0) {
   				assertEquals(trans,st1);
   			} 
   			else if (i==1){
   				assertEquals(trans,st2);
   			}
   		}
   	}
    
}