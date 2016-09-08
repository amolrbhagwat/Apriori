package amol.apriori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class Transactions {
	HashMap<Integer, ItemSet> db;
	
	public int read(String transactionsFilename) throws Exception{
		ArrayList<String> lines = Utils.getLinesFromFile(transactionsFilename);
		db = new HashMap<Integer, ItemSet>();
		
		int noOfLinesAdded = 0;
		for(int i = 0; i < lines.size(); i++){
			if(add(i, lines.get(i))){
				noOfLinesAdded++;
			}
		}
		return noOfLinesAdded;
	}
	
	public boolean add(int i, String s){
		return db.put(i, new ItemSet(s)) == null ? true : false;
	}

	public int getItemSetSupportCount(ItemSet iSet) throws Exception{
		int containingTransactionCount = 0;
		for(Map.Entry<Integer, ItemSet> entry : db.entrySet()){
			if(entry.getValue().contains(iSet)){
				containingTransactionCount++;
			}
		}
		return containingTransactionCount;
	}
	
	public boolean satisfiesMinSupport(ItemSet iSet, int minSupport) throws Exception{
		return getItemSetSupportCount(iSet) >= minSupport;
	}
	
	public ArrayList<Integer> genFreq1(int minSupport){
		ArrayList<Integer> freq1 = new ArrayList<>();
		int items[];
		
		HashMap<Integer, Integer> itemSupport = new HashMap<>();
		for(Map.Entry<Integer, ItemSet> entry : db.entrySet()){
			items = entry.getValue().getItems();
			for(int i = 0; i < items.length; i++){
				if(items[i]== 1){
					if(itemSupport.containsKey(i)){
						itemSupport.put(i, itemSupport.get(i) + 1);
					}
					else{
						itemSupport.put(i, 1);
					}
				}
			}
		}
		
		int noOfItems = db.get(0).getLength();
		
		for(int i = 0; i < noOfItems; i++){
			if(itemSupport.containsKey(i) && itemSupport.get(i) >= minSupport){
				freq1.add(i);
			}
		}
		
		return freq1;
	}
	
	public int getNoOfTransactions(){
		return db.size();
	}
	
	public double getItemSetSupport(ItemSet iSet) throws Exception{
		double count = getItemSetSupportCount(iSet);
		double noOfRecords = db.size();
		return count/noOfRecords;
	}
	
	//////////////////////////////////////////////////////////////
	
	@Test
	public void testEquals() throws Exception{
		ItemSet i1 = new ItemSet("0 0 1 1 0");
		assertTrue(i1.equals(new ItemSet("0 0 1 1 0")));
	}
	
	@Test
	public void testClone() throws Exception{
		ItemSet i1 = new ItemSet("0 0 1 1 0");
		assertTrue(new ItemSet("0 0 1 1 0").equals(i1.getClone()));
	}
	
	/*
	@Test
	public void testGenFreq1() throws Exception{
		read("/home/amol/Documents/Data Mining/A4/Data/market");
		ArrayList<ItemSet> f1 = genFreq1(7);
		for(ItemSet iSet : f1){
			iSet.print();
		}
		
	}*/
	
	@Test
	public void testGetItemSetSupport() throws Exception{
		read("/home/amol/Documents/Data Mining/A4/Data/market");
		assertEquals(getItemSetSupportCount(new ItemSet("0 0 0 0 1")), 2);
	}
	
	@Test
	public void testReadFunction() throws Exception{
		assertEquals(read("/home/amol/Documents/Data Mining/A4/Data/market"), 9);
	}
	
	@Test
	public void testAdd() throws Exception{
		db = new HashMap<Integer, ItemSet>();
		assertTrue(add(0, ""));
	}
	
	@Test
	public void testIsPresetAt() throws Exception{
		read("/home/amol/Documents/Data Mining/A4/Data/market");
		assertTrue(db.get(0).isItemPresent(0));
	}
	
	@Test
	public void testAddItem() throws Exception{
		ItemSet iSet = new ItemSet("1 0 0 0 1 0");
		assertFalse(iSet.isItemPresent(2));
		iSet.addItem(2);
		assertTrue(iSet.isItemPresent(2));
	}
	
	@Test
	public void testRemoveItem() throws Exception{
		ItemSet iSet = new ItemSet("1 0 0 0 1 0");
		iSet.removeItem(0);
		assertFalse(iSet.isItemPresent(0));
	}
	
	@Test
	public void testCardiniality() throws Exception{
		ItemSet iSet = new ItemSet("1 0 0 0 1 0");
		assertEquals(iSet.getCardiniality(), 2);
		iSet.addItem(1);
		assertEquals(iSet.getCardiniality(), 3);
		iSet.removeItem(0);
		assertEquals(iSet.getCardiniality(), 2);
	}
	
	@Test
	public void testGetHighestItem() throws Exception{
		ItemSet iSet = new ItemSet("1 0 0 0 1 0");
		assertEquals(iSet.getHighestItem(), 4);
	}
	
	@Test
	public void testIsPrefixCommon() throws Exception{
		// valid
		ItemSet iset1 = new ItemSet("1 1 0 0 1");
		ItemSet iset2 = new ItemSet("1 1 0 1 0");
		assertTrue(iset1.isPrefixCommon(iset2));
		
		// invalid
		ItemSet iset3 = new ItemSet("1 1 1 0 1");
		ItemSet iset4 = new ItemSet("1 1 0 1 0");
		assertFalse(iset3.isPrefixCommon(iset4));
		
	}
	
	@Test
	public void testMergeWith() throws Exception{
		ItemSet iset1 = new ItemSet("1 0 0 0 0");
		ItemSet iset2 = new ItemSet("0 1 0 0 0");
		
		ItemSet iset3 = iset1.mergeWith(iset2);
		assertTrue(iset3.contains(new ItemSet("1 1 0 0 0")));
		
	}
	
}
