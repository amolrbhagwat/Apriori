package amol.apriori;

public class AssociationRule {
	ItemSet antecedent;
	ItemSet consequent;
	int confidence;
	Transactions t;
	
	public AssociationRule(ItemSet antecedent, ItemSet consequent, Transactions t) throws Exception{
		this.antecedent = antecedent;
		this.consequent = consequent;
		//System.out.print("A ");this.antecedent.print();
		//System.out.print("B ");this.consequent.print();
		this.t = t;

		this.confidence = 
				(100 * t.getItemSetSupportCount(antecedent.mergeWith(consequent)))/
				t.getItemSetSupportCount(antecedent);
	}

	public int getConfidence() throws Exception{
		return this.confidence;
	}
	
	public double getLift() throws Exception{
		return t.getItemSetSupport(antecedent.mergeWith(consequent)) / 
				(t.getItemSetSupport(antecedent) * t.getItemSetSupport(consequent));
	}
}
