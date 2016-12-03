package advanceddb3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import advanceddb3.vo.ItemSet;

public class Apriori {
	public void runApriori(String fileName, float minSupport, float minConfidence) throws IOException{
		 ReadFile result = ReadFile.readFile(fileName);
		 Map<Set<String>, Integer> frequency = new HashMap<Set<String>, Integer>();
		 Map<Integer, Set<Set<String>>> fullItemSet = new HashMap<Integer, Set<Set<String>>>();
		 Set<Set<String>> CSet;
		 CSet = getItemsWithMinSup(result.getTransactions(), result.getItemSet(), minSupport, frequency);
		 Set<Set<String>> LSet = CSet;
		 fullItemSet.put(1, LSet);
		 Integer count = 2;
		 
		 Map<Set<String>, Float> itemSetCount = new HashMap<Set<String>, Float>();
		 List<ItemSet> freqItemSets = new ArrayList<>();
		 
		 while(!LSet.isEmpty()){
			 LSet = aprioriGen(LSet, count);
			 CSet = getItemsWithMinSup(result.getTransactions(), LSet, minSupport, frequency);
			 LSet = CSet;
			 fullItemSet.put(count, LSet);
			 count += 1;
		 }
		 
		 File file = new File("output.txt");
		 
         // if file doesnt exists, then create it
         if (!file.exists()) {
                file.createNewFile();
         }

         FileWriter fw = new FileWriter(file.getAbsoluteFile());
         BufferedWriter bw = new BufferedWriter(fw);
		 
	//	 System.out.println("==Frequent itemsets (min_sup=" + minSupport*100 + "%)\n");
		 bw.write("==Frequent itemsets (min_sup=" + minSupport*100 + "%)\n");
		 for ( Set<Set<String>> items : fullItemSet.values() ) {
				for (Set<String> item : items){
					float size = result.getTransactions().size();
					float support = frequency.get(item)/size;
					itemSetCount.put(item, support);
				}
		 }
		 
		 for (Set<String> key : itemSetCount.keySet()) {
			 freqItemSets.add(new ItemSet(key,itemSetCount.get(key)));
		}
		 
		 Collections.sort(freqItemSets, new Comparator<ItemSet>() {

			@Override
			public int compare(ItemSet o1, ItemSet o2) {
				if(o1.support > o2.support) {
					return -1;
				} else if(o1.support < o2.support) {
					return 1;
				}
				return 0;
			}
			 
		 });
		 
		 for(ItemSet item: freqItemSets) {
		//	 System.out.println(item.itemSet1 + ", " + item.support*100 + "%");
			 bw.write(item.itemSet1 + ", " + item.support*100 + "%\n");
		 }
		 
	//	 System.out.println("\n\n");
	//	 System.out.println("==High-confidence association rules (min_conf=" + minConfidence*100 + "%)\n");
		 bw.write("\n\n");
		 bw.write("==High-confidence association rules (min_conf=" + minConfidence*100 + "%)\n");
		 List<ItemSet> highConfidence = new ArrayList<>();
		 // Create Rules
		 for(int i=2;fullItemSet.get(i) != null;i++) {
			 Set<Set<String>> items = fullItemSet.get(i);
			 for (Set<String> item : items){
				 
				 List<String> ordered = new ArrayList<>(item);
				 
				 for(int j=0;j<ordered.size();j++) {
					 Set<String> currentItemSet = new HashSet<>();
					 Set<String> combinedSet = new HashSet<>();
					 currentItemSet.add(ordered.get(j));
					 combinedSet.add(ordered.get(j));
					 
					 Set<String> otherItemSet = new HashSet<>();
					 
					 for(int k=0;k<ordered.size();k++) {
						 if(k != j) {
							 otherItemSet.add(ordered.get(k));
							 combinedSet.add(ordered.get(k));
						 }
					 }
					 
					 float overallSize = result.getTransactions().size();
					 float support = frequency.get(combinedSet)/overallSize;
					 
					 float secondConfidence = ((float)frequency.get(combinedSet))/((float)frequency.get(otherItemSet));
					 
					 if(secondConfidence >= minConfidence) {
						 highConfidence.add(new ItemSet(otherItemSet, currentItemSet, support, secondConfidence));
					 }
				 }
			}
		 }
		 
		 Collections.sort(highConfidence, new Comparator<ItemSet>() {

				@Override
				public int compare(ItemSet o1, ItemSet o2) {
					if(o1.confidence > o2.confidence) {
						return -1;
					} else if(o1.confidence < o2.confidence) {
						return 1;
					}
					return 0;
				}
				 
			 });
			 
			 for(ItemSet item: highConfidence) {
			//	 System.out.println(item.itemSet1 + " => " + item.itemSet2 + " (Conf: " + item.confidence*100 + "%, Supp: " + item.support*100 + "%)");
				 bw.write(item.itemSet1 + " => " + item.itemSet2 + " (Conf: " + item.confidence*100 + "%, Supp: " + item.support*100 + "%)\n");
			 }
		 
		bw.close();
	}
	
	private Set<Set<String>> aprioriGen(Set<Set<String>> lSet, Integer setSize){
		Set<Set<String>> unionSet = new HashSet<Set<String>>();
		for (Set<String> itemSet1: lSet){
			for (Set<String> itemSet2: lSet){
				Set<String> temp = new HashSet<String>();
				temp.addAll(itemSet1);
				temp.addAll(itemSet2);
				if(temp.size() == setSize){
					unionSet.add(temp);
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
			float totalSize = transactions.size();
			float support = itemSetDict.get(item)/totalSize;
			if(support >= minSupport){
				finalItemSet.add(item);
			}
		}
		return finalItemSet;
	}
	
	
}
