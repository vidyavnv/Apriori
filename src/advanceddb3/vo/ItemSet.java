package advanceddb3.vo;

import java.util.Set;

/**
 * COMS E6111 - Project 3
 * ItemSet.java
 * Purpose: Create class for each itemset with support and confidence.
 *
 * @author Sriharsha Gundappa, Vidya Venkiteswaran 
 * @version 1.0 12/03/2016
 */
public class ItemSet {
	public Set<String> itemSet1;
	public float support;
	public Set<String> itemSet2;
	public float confidence;
	
	public ItemSet(Set<String> itemSet, float support) {
		this.itemSet1 = itemSet;
		this.support = support;
	}
	
	public ItemSet(Set<String> leftSet, Set<String> rightSet, float support, float confidence) {
		this.itemSet1 = leftSet;
		this.itemSet2 = rightSet;
		this.support = support;
		this.confidence = confidence;
	}
}
