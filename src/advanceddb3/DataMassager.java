package advanceddb3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import advanceddb3.vo.ConvertedSalary;

/**
 * COMS E6111 - Project 3
 * DataMassager.java
 * Purpose: Creates a new dataset of the form: agency name, title description, salary range.
 *			Numeric salary is categorised into <25k,<25kand>50k,<50kand>75k,<75kand>100k,
 *			<100kand>125k,>125k.
 * @author Sriharsha Gundappa, Vidya Venkiteswaran 
 * @version 1.0 12/03/2016
 */
public class DataMassager {

	public static void main(String[] args) {
		BufferedReader br = null;
		
		try {

			String sCurrentLine;

			//br = new BufferedReader(new FileReader("/Users/worksriharsha/Google Drive/Masters/W6111 Advanced Database Systems/project3/test_records.csv"));
			br = new BufferedReader(new FileReader("/Users/worksriharsha/Google Drive/Masters/W6111 Advanced Database Systems/project3/Full_year_2015_records.csv"));
			
			//File file = new File("/Users/worksriharsha/Google Drive/Masters/W6111 Advanced Database Systems/project3/test_output.csv");
			File file = new File("/Users/worksriharsha/Google Drive/Masters/W6111 Advanced Database Systems/project3/Full_year_2015_output.csv");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			int i = 0;
			// Read each line in csv file
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(i);
				
				if(i != 0 && !sCurrentLine.contains("\"")) {
					// Split each using ',' delimiter
					String [] arr = sCurrentLine.split(",");
					
					// Pick following columns Base Salary, Pay Basis and Salary
					String baseSalary = arr[10];
					String payBasis = arr[11];
					
					long salary = 0;
					
					// Convert per day/per month salary to per annum salary
					if("per day".equalsIgnoreCase(payBasis)) {
						salary = Long.parseLong(baseSalary.substring(1, baseSalary.length()-3)) * 21*12;
					}else if("per hour".equalsIgnoreCase(payBasis)) {
						salary = Long.parseLong(baseSalary.substring(1, baseSalary.length()-3)) * 8*21*12;
					} else {
						salary = Long.parseLong(baseSalary.substring(1, baseSalary.length()-3));
					}
					
					ConvertedSalary sal = new ConvertedSalary(arr[2], arr[3], arr[4], 
							arr[5], arr[8], arr[9], salary);
										
					String initPart = sal.agencyName + "," + sal.titleDesc + ",";
					
					// Create range category for each salary and write it to file to create a csv
					if(sal.salary < 25000) {
						bw.write(initPart + "lessthan25k");
					}else if(sal.salary >= 25000 && sal.salary < 50000) {
						bw.write(initPart + "between25kand50k");
					}else if(sal.salary >= 50000 && sal.salary < 75000) {
						bw.write(initPart + "between50kand75k");
					}else if(sal.salary >= 75000 && sal.salary < 100000) {
						bw.write(initPart + "between75kand100k");
					}else if(sal.salary >= 100000 && sal.salary < 125000) {
						bw.write(initPart + "between100kand125k");
					}else if(sal.salary >= 125000) {
						bw.write(initPart + "morethan125k");
					}
					
					bw.write("\n");
				}
				i++;
			}
			
			bw.close();
			
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
