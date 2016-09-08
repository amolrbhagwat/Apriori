package amol.apriori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReadTransactionsTest {

	@Test
	public void testFileNotFound() throws Exception {
		assertNull(Utils.getLinesFromFile("aFile"));
	}

	@Test
	public void testFileBlank() throws Exception{
		assertEquals(Utils.getLinesFromFile("/home/amol/Documents/emptyfile").size(),0);
	}
	
	@Test
	public void testFileAllOkay() throws Exception{
		assertEquals(Utils.getLinesFromFile("/home/amol/Documents/Data Mining/A4/Data/market").size(), 9);
	}
		
	
	
}
