package amol.apriori;

public class Apriori {

	public static void main(String args[]) throws Exception{
		Items its = new Items();
		its.read(args[1]);
		
		Transactions trans = new Transactions();
		trans.read(args[0]);
		
		int supportPercentage = Integer.parseInt(args[2]);
		int minSupport = (supportPercentage * trans.getNoOfTransactions())/100;
		
		int minConfidence = Integer.parseInt(args[3]);
		
		new AprioriAlgorithm(trans, its, minSupport, minConfidence);
		
	}
}
