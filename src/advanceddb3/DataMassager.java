package advanceddb3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import advanceddb3.vo.ConvertedSalary;

public class DataMassager {

	public static void main(String[] args) {
		BufferedReader br = null;
	//	ArrayList<ConvertedSalary> records = new ArrayList<>();

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
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(i);
				
				if(i != 0 && !sCurrentLine.contains("\"")) {
					String [] arr = sCurrentLine.split(",");
				/*	OriginalRecord record = new OriginalRecord(arr[0], arr[1], arr[2], arr[3],
							arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], arr[10], arr[11],
							arr[12], arr[13], arr[14], arr[15], arr[16]);*/
					
					String baseSalary = arr[10];
					String payBasis = arr[11];
					
					long salary = 0;
					
					if("per day".equalsIgnoreCase(payBasis)) {
						salary = Long.parseLong(baseSalary.substring(1, baseSalary.length()-3)) * 21*12;
					}else if("per hour".equalsIgnoreCase(payBasis)) {
						salary = Long.parseLong(baseSalary.substring(1, baseSalary.length()-3)) * 8*21*12;
					} else {
						salary = Long.parseLong(baseSalary.substring(1, baseSalary.length()-3));
					}
					
					ConvertedSalary sal = new ConvertedSalary(arr[2], arr[3], arr[4], 
							arr[5], arr[8], arr[9], salary);
					
			//		records.add(sal);
					
					String initPart = sal.agencyName + "," + sal.titleDesc + ",";
					
					if(sal.salary < 25000) {
			//			bw.write(initPart + "1,0,0,0,0,0");
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
