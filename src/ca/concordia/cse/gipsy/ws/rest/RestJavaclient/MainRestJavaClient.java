package ca.concordia.cse.gipsy.ws.rest.RestJavaclient;

import java.util.Scanner;

/**
 *
 * @author Jo
 */
public class MainRestJavaClient {
    public static void main(String[] args) {
        RestJavaClient client = new RestJavaClient("");
       
        Scanner scan = new Scanner(System.in);
        
        boolean finished = false;
        
        while (!finished) {
            System.out.println("Choose your option (exit to leave): ");
            System.out.println("GENERATE: to generate the files");
            System.out.println("WSDL: to get the wsdl file");
            System.out.println("OWL: to get the owl file");
            System.out.println("BPEL: to get the bpel file");
            System.out.println("WSLA: to get the wsla file");
            
            String answer = scan.nextLine();
            
            switch (answer) {
                case "GENERATE":
                    client.generateFiles(getGenConfiguration(scan));
                    break;
                case "WSDL":
                    client.getWSDL();
                    break;
                case "OWL":
                    client.getOWL();
                    break;
                case "WSLA":
                    client.getWSLA();
                    break;
                case "BPEL":
                    client.getBPEL();
                    break;
                case "EXIT":
                    finished = true;
                    break;
                default:
                    System.out.println("Wrong choice. Please try again.");
                    break;
            }
        }
        
        scan.close();
    }
    
    private static GeneratorConfiguration getGenConfiguration(Scanner scan) {
        System.out.println("Value for parameter 1:");
        String param1 = scan.nextLine();
        System.out.println("Value for parameter 2:");
        String param2 = scan.nextLine();
        
        return new GeneratorConfiguration(param1, param2);
    }
}
