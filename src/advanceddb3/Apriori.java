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

/**
 * COMS E6111 - Project 3
 * Apriori.java
 * Purpose: Run Apriori algorithm to create itemsets and association rules and write to a file.
 *
 * @author Sriharsha Gundappa, Vidya Venkiteswaran 
 * @version 1.0 12/03/2016
 */
public class Apriori {
	public void runApriori(String fileName, float minSupport, float minConfidence) throws IOException{
		/**
		 * Runs Apriori algorithm to get frequent itemsets and association rules and write it to a file.
		 *
		 * @param  fileName     	name of the file from which items needs to be read
		 * @param  minSupport  		minimum support value by which itemset support should exceed
		 * @param  minConfidence  	minimum confidence value by which association rule confidence should exceed
		 * @throws IOException 		If an input or output exception occurred
		 * @return no args
		 */ 
		 // Read file and create individual itemset and collect of each line into itemset 
		 ReadFile result = ReadFile.readFile(fileName);
		 // To count occurrence of each itemset
		 Map<Set<String>, Integer> frequency = new HashMap<Set<String>, Integer>();
		 Map<Integer, Set<Set<String>>> fullItemSet = new HashMap<Integer, Set<Set<String>>>();
		 Set<Set<String>> CSet;
		 // Get itemset which has support greater than equal to minSupport
		 CSet = getItemsWithMinSup(result.getTransactions(), result.getItemSet(), minSupport, frequency);
		 Set<Set<String>> LSet = CSet;
		 fullItemSet.put(1, LSet);
		 Integer count = 2;
		 
		 Map<Set<String>, Float> itemSetCount = new HashMap<Set<String>, Float>();
		 List<ItemSet> freqItemSets = new ArrayList<ItemSet>();
		 
		 while(!LSet.isEmpty()){
			 // Call aprioriGen to create set of length count
			 LSet = aprioriGen(LSet, count);
			 // Get itemset which has support greater than equal to minSupport
			 CSet = getItemsWithMinSup(result.getTransactions(), LSet, minSupport, frequency);
			 LSet = CSet;
			 // Add it to a dictionary with key as length of set size and value as itemset
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
		 
         // Calculate support for discovered itemsets
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
		 
		 // Sort itemsets in decreasing order of support
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
		 
		 // Write frequent itemsets to a file with their support values
		 for(ItemSet item: freqItemSets) {
			 bw.write(item.itemSet1 + ", " + item.support*100 + "%\n");
		 }
		 
		 bw.write("\n\n");
		 bw.write("==High-confidence association rules (min_conf=" + minConfidence*100 + "%)\n");
		 List<ItemSet> highConfidence = new ArrayList<ItemSet>();
		 // Create association rules and ad it to the final rule only if it passes the minConfidence value
		 for(int i=2;fullItemSet.get(i) != null;i++) {
			 Set<Set<String>> items = fullItemSet.get(i);
			 for (Set<String> item : items){
				 
				 List<String> ordered = new ArrayList<String>(item);
				 // Break itsemsets into individual items and create rule
				 for(int j=0;j<ordered.size();j++) {
					 Set<String> currentItemSet = new HashSet<String>();
					 Set<String> combinedSet = new HashSet<String>();
					 currentItemSet.add(ordered.get(j));
					 combinedSet.add(ordered.get(j));
					 
					 Set<String> otherItemSet = new HashSet<String>();
					 
					 for(int k=0;k<ordered.size();k++) {
						 if(k != j) {
							 otherItemSet.add(ordered.get(k));
							 combinedSet.add(ordered.get(k));
						 }
					 }
					 
					 float overallSize = result.getTransactions().size();
					 float support = frequency.get(combinedSet)/overallSize;
					 
					 // Calculate confidence of the association rule
					 float secondConfidence = ((float)frequency.get(combinedSet))/((float)frequency.get(otherItemSet));
					 
					 if(secondConfidence >= minConfidence) {
						 highConfidence.add(new ItemSet(otherItemSet, currentItemSet, support, secondConfidence));
					 }
				 }
			}
		 }
		 
		 // Sort association rules according to decreasing confidence value
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
			 
		 	 // Write association rules to the same file with their support and confidence values
			 for(ItemSet item: highConfidence) {
				 System.out.println(item.itemSet1 + " => " + item.itemSet2 + " (Conf: " + item.confidence*100 + "%, Supp: " + item.support*100 + "%)");
				 bw.write(item.itemSet1 + " => " + item.itemSet2 + " (Conf: " + item.confidence*100 + "%, Supp: " + item.support*100 + "%)\n");
			 }
		 
		bw.close();
	}
	
	private Set<Set<String>> aprioriGen(Set<Set<String>> lSet, Integer setSize){
		/**
		 * Creates a super set of size setSize from lSet itself.
		 *
		 * @param  lSet     Sets of set of type String
		 * @param  setSize  Size of the set required
		 * @return unionSet Superset of lSet of size setSize.
		 */
		Set<Set<String>> unionSet = new HashSet<Set<String>>();
		for (Set<String> itemSet1: lSet){
			for (Set<String> itemSet2: lSet){
				Set<String> temp = new HashSet<String>();
				temp.addAll(itemSet1);
				temp.addAll(itemSet2);
				// Add to final set if temporary set size is equal to given setSize
				if(temp.size() == setSize){
					unionSet.add(temp);
				}
			}
		}
		return unionSet;
	}
	
	private Set<Set<String>> getItemsWithMinSup(List<Set<String>> transactions, Set<Set<String>> itemSet, float minSupport, Map<Set<String>, Integer> frequency){
		/**
		 * Returns itemsets which are equal(or more than) to given minimum support
		 *
		 * @param  transactions     Itemset collection from file
		 * @param  itemSet  		itemSet whose support needs to be calculated
		 * @param  minSupport  		Real value with which itemSet support should exceed
		 * @param  frequency  		Universal dictionary of itemsets
		 * @return finalItemSet 	Itemsets which satisfy minSupport rule
		 */
		Set<Set<String>> finalItemSet = new HashSet<Set<String>>();;
		Map<Set<String>, Integer> itemSetDict = new HashMap<Set<String>, Integer>();
		
		// Calculate support for each itemset
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
		
		// Check which itemset satisfies minSupport
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
