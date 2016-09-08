package amol.apriori;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;


public class Items {
	ArrayList<String> items;
	
	public int read(String itemsFilename) throws Exception{
		ArrayList<String> lines = Utils.getLinesFromFile(itemsFilename);
		items = new ArrayList<String>();
		
		int noOfItemsAdded = 0;
		for(int i = 0; i < lines.size(); i++){
			if(add(lines.get(i))){
				noOfItemsAdded++;
			}
		}
		return noOfItemsAdded;
	}
	
	public boolean add(String itemName) throws Exception{
		return items.add(itemName);
	}
	
	public int getWidth(){
		return items.size();
	}

	public String getItemAt(int index){
		return items.get(index);
	}
	
	/* Tests */
	
	@Test
	public void testReadItems() throws Exception{
		assertEquals(read("/home/amol/Documents/Data Mining/A4/Data/marketitems"), 5);
	}
	
	@Test
	public void testAddItem() throws Exception{
		items = new ArrayList<String>();
		assertTrue(add(""));
	}
	
}
