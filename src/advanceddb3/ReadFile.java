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
		List<Set<String>> transactions = new ArrayList<Set<String>>();
		Set<Set<String>> itemSet = new HashSet<Set<String>>();;
		
		String fileLine = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((fileLine = br.readLine()) != null) {
            	//System.out.println(line);
                Set<String> fileItems = new HashSet<String>(Arrays.asList(fileLine.split(",")));
                transactions.add(fileItems);
                for(String s : fileItems){
                	itemSet.add(Collections.singleton(s.trim()));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return new ReadFile(transactions, itemSet);
	}

}
