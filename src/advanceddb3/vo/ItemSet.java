package advanceddb3.vo;

import java.util.Set;

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
