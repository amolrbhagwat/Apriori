package amol.apriori;

public class ItemSet {
	//String s;
	private int items[];
	private int cardiniality;
	private int highestItem = -1;
	private int support;
	
	
	
	public ItemSet getClone() throws CloneNotSupportedException{
		ItemSet iSet = new ItemSet(items.length);
		for(int i = 0; i < items.length; i++){
			if(isItemPresent(i)){
				iSet.addItem(i);
			}
		}
		return iSet;
	}
	
	public ItemSet(String string){
		String splits[] = string.split(" ");
		items = new int[splits.length];
		for(int i = 0; i < splits.length; i++){
			items[i] = (splits[i].equals("1")) ? 1 : 0;
			if(items[i] == 1){
				cardiniality++;
				if(i > highestItem){
					highestItem = i;
				}
			}
		}
		
		//this.s = string;
	}
	
	public ItemSet(int width){
		items = new int[width];
	}
	
	public int[] getItems(){
		return this.items;
	}
	
	public boolean isItemPresent(int index){
		return (items[index] == 1);
	}

	public int getLength(){
		return items.length;
	}
	
	public boolean contains(ItemSet iSet){
		for(int i = 0; i < iSet.getLength(); i++){
			if(iSet.isItemPresent(i) && !isItemPresent(i)){
				return false;
			}
		}
		return true;
	}
	
	public void addItem(int index){
		if(!isItemPresent(index)){
			items[index] = 1;
			cardiniality++;
			if(index > highestItem){
				highestItem = index;
			}
		}
	}
	
	public void removeItem(int index){
		if(isItemPresent(index)){
			items[index] = 0;
			cardiniality--;
			setHighestItem();
		}
	}
	
	public int getCardiniality(){
		return this.cardiniality;
	}
	
	public void setHighestItem(){
		int index = -1;
		for(int i = 0; i < items.length; i++){
			if(items[i] == 1){
				index = i;
			}
		}
		highestItem = index;
	}
	
	public int getHighestItem(){
		return highestItem;
	}

	public boolean isPrefixCommon(ItemSet iSet){
		if(this.cardiniality == iSet.getCardiniality()){
			int moreItems = cardiniality - 1;
			for(int i = 0; (i < items.length) && moreItems > 0 ; i++){
				if(isItemPresent(i) && !iSet.isItemPresent(i)){
					return false;
				}
				else if(isItemPresent(i) && iSet.isItemPresent(i)){
					moreItems--;
				}
			}
			return true;
		}
		else{
			return false;
		}
	}

	public void setSupport(int support){
		this.support = support;
	}
	
	public int getSupport(){
		return this.support;
	}

	public ItemSet mergeWith(ItemSet iSet) throws Exception{
		ItemSet merged = getClone();
		if(iSet != null){
			for(int i = 0; i < items.length; i++){
				if(iSet.isItemPresent(i)){
					merged.addItem(i);
				}
			}
		}
		return merged;
	}
	
	public boolean equals(ItemSet iSet) throws Exception{
		for(int i = 0; i < items.length; i++){
			if(isItemPresent(i) && !iSet.isItemPresent(i)){
				return false;
			}
		}
		return true;
	}

	public void print(){
		for(int i = 0; i < items.length; i++){
			System.out.print(items[i] + " " );
		}
		System.out.println();
	}
	
	public void printItemSetContents(Items itemNames){
		System.out.print("{ ");
		for(int i = 0; i < items.length; i++){
			if(items[i] == 1){
				System.out.print(itemNames.getItemAt(i) + " ");
			}
		}
		System.out.print("}");
		//System.out.println();
	}

	public ItemSet subtract(ItemSet iSet) throws Exception{
		ItemSet subtracted = getClone();
		for(int i = 0; i < getLength(); i++){
			if(isItemPresent(i)){
				subtracted.removeItem(i);
			}
		}
		return subtracted;
	}

}
