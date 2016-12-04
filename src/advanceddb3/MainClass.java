package advanceddb3;

/**
 * COMS E6111 - Project 3
 * MainClass.java
 * Purpose: Main entry to accept input file, minimum confidence and minimum 
 * 			support and pass it to Apriori algorithm.
 *
 * @author Sriharsha Gundappa, Vidya Venkiteswaran 
 * @version 1.0 12/03/2016
 */
public class MainClass {
          
	public static String fileName;
	public static float minSupport;
	public static float minConfidence;
   
 
   /*
   * Main Entry class for the program
   * Accepts arguments in below order
   * 1 - File Name
   * 2 - Minimum Support
   * 3 - Minimum Confidence
   */
	public static void main(String[] args) throws Exception {
		// If all arguments are not passed, exit the program gracefully
		if(args != null && args.length >= 3) {
			// Assign arguments to corresponding variables
			fileName = args[0];
			minSupport = Float.parseFloat(args[1]);
			minConfidence = Float.parseFloat(args[2]);
			// Minimum Support should be between 0 and 1. Else exit
			if(minSupport > 1.0 || minSupport < 0) {
				System.out.println("Invalid value for Minimum Support");
            	System.exit(0);
            	}
             
            // Minimum Confidence should be between 0 and 1. Else exit
            if(minConfidence > 1.0 || minConfidence < 0) {
            	System.out.println("Invalid value for Minimum Confidence");
            	System.exit(0);
             	}
            Apriori apriori = new Apriori();
            // Run Apriori and output to a file
            apriori.runApriori(fileName, minSupport, minConfidence);
                    
      } else {
             System.out.println("Invalid number of arguments");
             System.exit(1);
      }
   }
 
}