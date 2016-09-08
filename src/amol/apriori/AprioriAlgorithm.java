package amol.apriori;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.MinMaxPriorityQueue;

public class AprioriAlgorithm {
	Transactions trans;
	Items items;
	int minSupport;
	int minConf;
	HashMap<Integer, ArrayList<ItemSet>> freqItemSets;
	
	public AprioriAlgorithm(Transactions trans, Items items, int minSupport, int minConfidence) throws Exception{
		this.trans = trans;
		this.items = items;
		this.minSupport = minSupport;
		this.minConf = minConfidence;
		freqItemSets = genUsingF1();
		freqItemSets = genUsingSelf();
		countMaximalFreqItemSets();
		countClosedFreqItemSets();
		countConfBasedPrunedRules();
		genTopTenRules();
		genTopTenRulesUsingLift();
	}
	
	public HashMap<Integer, ArrayList<ItemSet>> genUsingF1() throws Exception{
		ArrayList<Integer> f1 = trans.genFreq1(minSupport);
		ArrayList<ItemSet> currentItemSetList = new ArrayList<>();
		ArrayList<ItemSet> candidateItemSetList = new ArrayList<>();
		HashMap<Integer, ArrayList<ItemSet>> allFreqItemSets = new HashMap<>();
		
		
		// create an ArrayList of ItemSets as the current list, using F1
		for(Integer freqItem : f1){
			ItemSet itemSet = new ItemSet(items.getWidth());
			itemSet.addItem(freqItem);
			currentItemSetList.add(itemSet);
		}
		
		//System.out.println("1-itemsets: " + currentItemSetList.size());
		int k = 1;
		int noOfCandidateSetsGen = currentItemSetList.size();
		int noOfFreqSetsGen = 0;
		allFreqItemSets.put(k, currentItemSetList);
		
		
		while(currentItemSetList.size() != 0){
			for(ItemSet currentItemSet : currentItemSetList){
				int highest = currentItemSet.getHighestItem();
				
				for(int index = highest + 1; index < items.getWidth(); index++){
					if(f1.contains(index)){
						ItemSet candidateItemSet = currentItemSet.getClone();
						candidateItemSet.addItem(index);
						candidateItemSetList.add(candidateItemSet);		
						//candidateItemSet.printItemSetContents(items);
					}
				}
			}
			
			noOfCandidateSetsGen += candidateItemSetList.size();
			
			ArrayList<ItemSet> freqItemSetList = new ArrayList<>();
			for(ItemSet aCandidateItemSet : candidateItemSetList){
				if(trans.satisfiesMinSupport(aCandidateItemSet, minSupport)){
					//aCandidateItemSet.print();
					freqItemSetList.add(aCandidateItemSet);
				}
			}
				
			if(freqItemSetList.size() != 0){
				allFreqItemSets.put(++k, freqItemSetList);
				
				System.out.print("k" + k);
				System.out.print(" c" + candidateItemSetList.size());
				System.out.println(" f" + freqItemSetList.size());
				//for(ItemSet aItemSet : freqItemSetList){
					//aItemSet.print();
				//}
				currentItemSetList = freqItemSetList;
				candidateItemSetList = new ArrayList<>();
				freqItemSetList = new ArrayList<>();
			}
			else{
				break;
			}
		}
		
		for(Map.Entry<Integer, ArrayList<ItemSet>> entry : allFreqItemSets.entrySet()){
			noOfFreqSetsGen += entry.getValue().size();
		}	
		
		System.out.print("F(k-1)*F(1)");
		System.out.println(" Candidates: " + noOfCandidateSetsGen + " Frequent " + noOfFreqSetsGen);
		System.out.println("===================================================");
		return allFreqItemSets;
		
	}

	public HashMap<Integer, ArrayList<ItemSet>> genUsingSelf() throws Exception{
		ArrayList<Integer> f1 = trans.genFreq1(minSupport);
		ArrayList<ItemSet> currentItemSetList = new ArrayList<>();
		ArrayList<ItemSet> candidateItemSetList = new ArrayList<>();
		HashMap<Integer, ArrayList<ItemSet>> allFreqItemSets = new HashMap<>();
		
		
		// create an ArrayList of ItemSets as the current list, using F1
		for(Integer freqItem : f1){
			ItemSet itemSet = new ItemSet(items.getWidth());
			itemSet.addItem(freqItem);
			currentItemSetList.add(itemSet);
		}
		
		//for(ItemSet anItemSet : currentItemSetList){
			//anItemSet.printItemSetContents(items);
		//}
		
		//System.out.println("1-itemsets: " + currentItemSetList.size());
		int k = 1;
		int noOfCandidateSetsGen = currentItemSetList.size();
		int noOfFreqSetsGen = 0;
		allFreqItemSets.put(k, currentItemSetList);
		
		while(currentItemSetList.size() != 0){
			for(int i = 0; i < currentItemSetList.size() - 1; i++){
				 
				ItemSet currentItemSet = currentItemSetList.get(i);
				for(int j = i+1; j < currentItemSetList.size(); j++){
					ItemSet nextItemSet = currentItemSetList.get(j);
					if(currentItemSet.isPrefixCommon(nextItemSet)){
						ItemSet candidateItemSet = currentItemSet.getClone();
						candidateItemSet = candidateItemSet.mergeWith(nextItemSet);
						
						//candidateItemSet.printItemSetContents(items);
						candidateItemSetList.add(candidateItemSet);
						
					}
				}
				
			}
			//System.out.println(total);
			
			noOfCandidateSetsGen += candidateItemSetList.size();
			
			//System.out.println("Frequent");
			ArrayList<ItemSet> freqItemSetList = new ArrayList<>();
			for(ItemSet aCandidateItemSet : candidateItemSetList){
				if(trans.satisfiesMinSupport(aCandidateItemSet, minSupport)){
					//aCandidateItemSet.print();
					//aCandidateItemSet.printItemSetContents(items);
					freqItemSetList.add(aCandidateItemSet);
				}
			}
				
			if(freqItemSetList.size() != 0){
				allFreqItemSets.put(++k, freqItemSetList);
				
				System.out.print("k" + k);
				System.out.print(" c" + candidateItemSetList.size());
				System.out.println(" f" + freqItemSetList.size());
				
				//for(ItemSet aItemSet : freqItemSetList){
					//aItemSet.print();
				//}
				
				currentItemSetList = freqItemSetList;
				candidateItemSetList = new ArrayList<>();
				freqItemSetList = new ArrayList<>();
			}
			else{
				break;
			}
		}
		
		for(Map.Entry<Integer, ArrayList<ItemSet>> entry : allFreqItemSets.entrySet()){
			noOfFreqSetsGen += entry.getValue().size();
		}	
		
		System.out.print("F(k-1)*F(k-1)");
		System.out.println(" Candidates: " + noOfCandidateSetsGen + " Frequent " + noOfFreqSetsGen);
		System.out.println("===================================================");
		
		return allFreqItemSets;
	}

	public void countMaximalFreqItemSets() throws Exception{
		int noOfMaximalFreqItemSets = 0;
		for(int i = 1; i < freqItemSets.size(); i++){
			for(ItemSet aItemSet : freqItemSets.get(i)){
				boolean isMaximal = true;
				for(ItemSet higherItemSet : freqItemSets.get(i+1)){
					if(higherItemSet.contains(aItemSet)){
						isMaximal = false;
						break;
					}
				}
				if(isMaximal){
					noOfMaximalFreqItemSets++;
				}
			}
		}
		System.out.println("Number of maximal frequent itemsets: " + noOfMaximalFreqItemSets);
	}
	
	public void countClosedFreqItemSets() throws Exception{
		int noOfClosedFreqItemSets = 0;
		for(int i = 1; i < freqItemSets.size(); i++){
			for(ItemSet aItemSet : freqItemSets.get(i)){
				boolean isClosed = true;
				for(ItemSet higherItemSet : freqItemSets.get(i+1)){
					if(trans.getItemSetSupportCount(aItemSet) == trans.getItemSetSupportCount(higherItemSet)){
						isClosed = false;
						break;
					}
				}
				if(isClosed){
					noOfClosedFreqItemSets++;
				}
			}
		}
		System.out.println("Number of closed frequent itemsets: " + noOfClosedFreqItemSets);
	}
	
	public void countConfBasedPrunedRules() throws Exception{
		BigInteger count = new BigInteger("0");
		BigInteger total = new BigInteger("0");
		
		for(int i = 1; i < freqItemSets.size(); i++){
			for(ItemSet aItemSet : freqItemSets.get(i)){
				count = count.add(countSubSets(aItemSet));
				BigInteger currTotal = new BigDecimal(Math.pow(2, aItemSet.getCardiniality())).toBigInteger();
				//currTotal = currTotal.subtract(new BigInteger("2"));
				total = total.add(currTotal);
			}
		}
		System.out.println("Confident rules/All possible: "
				+ count.toString() + " " + total.toString());
	}
	
	public BigInteger countSubSets(ItemSet iSet) throws Exception{
		BitSet belowConfidence = new BitSet(iSet.getLength());
		
		BigInteger count = new BigDecimal(Math.pow(2, iSet.getCardiniality())).toBigInteger();
		
		for(int i = 0; i < iSet.getLength() - 1; i++){
			if(!iSet.isItemPresent(i) || belowConfidence.get(i)) continue;
			for(int j = i; j < iSet.getLength(); j++){
				if(!iSet.isItemPresent(j) || belowConfidence.get(j)) continue;
				
				ItemSet consequentItemSet = new ItemSet(iSet.getLength());
				consequentItemSet.addItem(i);
				consequentItemSet.addItem(j);
				ItemSet antecedantItemSet = iSet.subtract(consequentItemSet);
				
				if(!meetsConfCriteria(iSet, antecedantItemSet)){
					belowConfidence.set(j);
					if(count.mod(new BigInteger("2")).equals(0)){
						BigInteger divisor = new BigDecimal(Math.pow(2, i)).toBigInteger();
						count = count.divide(divisor);
						count = count.add(BigInteger.ONE);
					}
					else{
						BigInteger divisor = new BigDecimal(Math.pow(2, i)).toBigInteger();
						count = count.divide(divisor);
					}
				}
			}
		}
		
		count.subtract(new BigInteger("2"));
		return count;
	}

	public boolean meetsConfCriteria(ItemSet iSet, ItemSet consequent) throws Exception{
		double frac = (double) trans.getItemSetSupportCount(iSet)/trans.getItemSetSupportCount(iSet.subtract(consequent));
		int confidence = (int) (100 * frac);
		return (confidence >= minConf);
	}

	public void genTopTenRules() throws Exception{
		MinMaxPriorityQueue<AssociationRule> queue = MinMaxPriorityQueue.orderedBy(new Comparator<AssociationRule>() {
			public int compare(AssociationRule rule1, AssociationRule rule2){
				try {
					int result = new Integer(rule1.getConfidence()).compareTo(rule2.getConfidence()); 
					return result;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			}
		}).create();
		
		for(int k = 2; k < freqItemSets.size(); k++){
			for(ItemSet iSet : freqItemSets.get(k)){
				
				BitSet belowConfidence = new BitSet(iSet.getLength());
				
				for(int i = 0; i < iSet.getLength() - 1; i++){
					if(!iSet.isItemPresent(i) || belowConfidence.get(i)) continue;
					for(int j = i; j < iSet.getLength(); j++){
						if(!iSet.isItemPresent(j) || belowConfidence.get(j)) continue;
						
						ItemSet antecedentItemSet = iSet.getClone();
						
						//System.out.print("- ");antecedentItemSet.print();
						ItemSet consequentItemSet = new ItemSet(iSet.getLength());
						consequentItemSet.addItem(i);
						consequentItemSet.addItem(j);
						antecedentItemSet.removeItem(i);
						antecedentItemSet.removeItem(j);
						
						if(antecedentItemSet.getCardiniality() != 0){
							//System.out.print("A ");antecedentItemSet.print();
							//System.out.print("B ");consequentItemSet.print();
							
							queue.add(new AssociationRule(antecedentItemSet, consequentItemSet, trans));
						}
						
						
					}
				}
			}
		}
		
		int count = 0;
		while(count != 10 && !queue.isEmpty()){
			AssociationRule aRule = queue.removeLast();
			aRule.antecedent.printItemSetContents(items);
			System.out.print("--> ");
			aRule.consequent.printItemSetContents(items);
			System.out.println("\nConfidence: " + aRule.getConfidence());
			count++;
		}
		System.out.println("===================================================");
		
	}

	public void genTopTenRulesUsingLift() throws Exception{
		MinMaxPriorityQueue<AssociationRule> queue = MinMaxPriorityQueue.orderedBy(new Comparator<AssociationRule>() {
			public int compare(AssociationRule rule1, AssociationRule rule2){
				try {
					int result = new Double(rule1.getLift()).compareTo(rule2.getLift()); 
					return result;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			}
		}).create();
		
		for(int k = 2; k < freqItemSets.size(); k++){
			for(ItemSet iSet : freqItemSets.get(k)){
				
				BitSet belowConfidence = new BitSet(iSet.getLength());
				
				for(int i = 0; i < iSet.getLength() - 1; i++){
					if(!iSet.isItemPresent(i) || belowConfidence.get(i)) continue;
					for(int j = i; j < iSet.getLength(); j++){
						if(!iSet.isItemPresent(j) || belowConfidence.get(j)) continue;
						
						ItemSet antecedentItemSet = iSet.getClone();
						
						ItemSet consequentItemSet = new ItemSet(iSet.getLength());
						consequentItemSet.addItem(i);
						consequentItemSet.addItem(j);
						antecedentItemSet.removeItem(i);
						antecedentItemSet.removeItem(j);
						
						if(antecedentItemSet.getCardiniality() != 0){
							//System.out.print("A ");antecedentItemSet.print();
							//System.out.print("B ");consequentItemSet.print();
							
							queue.add(new AssociationRule(antecedentItemSet, consequentItemSet, trans));
						}
						
						
					}
				}
			}
		}
		
		int count = 0;
		while(count != 10 && !queue.isEmpty()){
			AssociationRule aRule = queue.removeLast();
			aRule.antecedent.printItemSetContents(items);
			System.out.print("--> ");
			aRule.consequent.printItemSetContents(items);
			System.out.println("\nLift: " + aRule.getLift());
			count++;
		}
		
		System.out.println("===================================================");
		
	}
	
	
}
