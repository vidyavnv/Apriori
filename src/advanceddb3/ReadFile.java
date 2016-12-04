package advanceddb3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * COMS E6111 - Project 3
 * ReadFile.java
 * Purpose: Reads a file and converts each line into a set and adds individual items
 *			to create 1-length itemset.
 *
 * @author Sriharsha Gundappa, Vidya Venkiteswaran 
 * @version 1.0 12/03/2016
 */
public class ReadFile {
	private List<Set<String>> transactions;
    private Set<Set<String>> itemSet;
    
    public ReadFile(List<Set<String>> transactions, Set<Set<String>> itemSet) {
        this.transactions = transactions;
        this.itemSet = itemSet;
    }

    public List<Set<String>> getTransactions() {
        return transactions;
    }

    public Set<Set<String>> getItemSet() {
        return itemSet;
    }
    
    public static ReadFile readFile(String fileName){
    	/**
		 * Runs Apriori algorithm to get frequent itemsets and association rules and write it to a file.
		 *
		 * @param  fileName  						name of the file from which items needs to be read
		 * @return ReadFile(transactions, itemsets) transactions and 1-itemsets from csv file
		 */ 
    	// Read input file and collect items into an itemset
		List<Set<String>> transactions = new ArrayList<Set<String>>();
		Set<Set<String>> itemSet = new HashSet<Set<String>>();;
		
		String fileLine = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((fileLine = br.readLine()) != null) {
            	// Split file lines with , delimiter
            	List<String> tempList = Arrays.asList(fileLine.split(","));
            	// Create a set to store each individual itemset as a set
            	Set<String> fileItems = new HashSet<String>();
            	for(String elem:tempList) {
            		itemSet.add(Collections.singleton(elem.trim()));
            		fileItems.add(elem.trim());
            	}
                transactions.add(fileItems);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return new ReadFile(transactions, itemSet);
	}

}
