package advanceddb3.vo;

/**
 * COMS E6111 - Project 3
 * ConvertedSalary.java
 * Purpose: Create class for data read from input csv file.
 *
 * @author Sriharsha Gundappa, Vidya Venkiteswaran 
 * @version 1.0 12/03/2016
 */
public class ConvertedSalary {
	public String agencyName;
	public String lastName;
	public String firstName;
	public String midInit;
	public String titleDesc;
	public String leaveStatus;
	public long salary;
	
	public ConvertedSalary(String agencyName,String lastName, String firstName, String midInit,
	String titleDesc, String leaveStatus, long salary) {
		this.agencyName = agencyName;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midInit = midInit;
		this.titleDesc = titleDesc;
		this.leaveStatus = leaveStatus;
		this.salary = salary;
	}
}
