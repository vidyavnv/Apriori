package advanceddb3;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Apriori {
	public String runApriori(String fileName, float minSupport, float minConfidence) throws IOException{
		 ReadFile result = ReadFile.readFile(fileName);
		 Map<Set<String>, Integer> frequency = new HashMap<Set<String>, Integer>();
		 Map<Integer, Set<Set<String>>> fullItemSet = new HashMap<Integer, Set<Set<String>>>();
		 Set<Set<String>> CSet;
		 CSet = getItemsWithMinSup(result.getTransactions(), result.getItemSet(), minSupport, frequency);
		 Set<Set<String>> LSet = CSet;
		 Integer count = 0;
		 
		 Map<Set<String>, Float> itemSetCount = new HashMap<Set<String>, Float>();
		 
		 while(!LSet.isEmpty()){
			 fullItemSet.put(count, LSet);
			 LSet = aprioriGen(LSet, count+1);
			 CSet = getItemsWithMinSup(result.getTransactions(), LSet, minSupport, frequency);
			 LSet = CSet;
			 count += 1;
		 }
		 
		 for ( Set<Set<String>> items : fullItemSet.values() ) {
				for (Set<String> item : items){
					float support;
					support = frequency.get(item)/result.getTransactions().size();
					itemSetCount.put(item, support);
					System.out.print(item);
					System.out.print(support);
				}
		 }
		 
		 // Create Rules
		 
		 return null;
	}
	
	private Set<Set<String>> aprioriGen(Set<Set<String>> lSet, Integer setSize){
		Set<Set<String>> unionSet = new HashSet<Set<String>>();
		for (Set<String> itemSet1: lSet){
			for (Set<String> itemSet2: lSet){
				Set<String> temp = new HashSet<String>();
				temp = itemSet1;
				Set<String> tempSet = new HashSet<String>();
				tempSet.addAll(itemSet2);
				if(tempSet.size() == setSize){
					unionSet.add(tempSet);
				}
			}
		}
		return unionSet;
	}
	private Set<Set<String>> getItemsWithMinSup(List<Set<String>> transactions, Set<Set<String>> itemSet, float minSupport, Map<Set<String>, Integer> frequency){
		Set<Set<String>> finalItemSet = new HashSet<Set<String>>();;
		Map<Set<String>, Integer> itemSetDict = new HashMap<Set<String>, Integer>();
		
		for (Set<String> item : itemSet) {
			for (Set<String> transaction : transactions){
				if(transaction.containsAll(item)){
					Integer value = frequency.get(item);
					if (value == null)
						value = 0;
					value += 1;
					frequency.put(item, value);
					itemSetDict.put(item, value);
				}

			}
		}
		
		for ( Set<String> item : itemSetDict.keySet() ) {
			float support;
			support = itemSetDict.get(item)/transactions.size();
			if(support >= minSupport){
				finalItemSet.add(item);
			}
		}
		return finalItemSet;
	}
	
	
}
