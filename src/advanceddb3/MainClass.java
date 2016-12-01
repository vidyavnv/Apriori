package advanceddb3;

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
            System.out.println(fileName);
            System.out.println(minSupport);
            System.out.println(minConfidence);
             // Minimum Support should be between 0 and 1. Else exit
             if(minSupport > 1.0 || minSupport < 0) {
            	 System.out.println("Invalide value for Minimum Support");
            	 System.exit(0);
             }
             
          // Minimum Confidence should be between 0 and 1. Else exit
             if(minConfidence > 1.0 || minConfidence < 0) {
            	 System.out.println("Invalide value for Minimum Confidence");
            	 System.exit(0);
             }
             Apriori apriori = new Apriori();
             String outfile = apriori.runApriori(fileName, minSupport, minConfidence);
             
            
                    
                    
      } else {
             System.out.println("Invalid number of arguments");
             System.exit(1);
      }
   }
 
}