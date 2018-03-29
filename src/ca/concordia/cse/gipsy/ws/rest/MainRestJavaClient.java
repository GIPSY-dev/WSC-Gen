package ca.concordia.cse.gipsy.ws.rest;

import java.util.Scanner;

/**
 *
 * @author Jo
 */
public class MainRestJavaClient {
    public static void main(String[] args) {
        RestJavaClient client = new RestJavaClient("http://localhost:8080/WSC-Gen/resources/restGenerator");
       
        Scanner scan = new Scanner(System.in);
        
        boolean finished = false;
        
        while (!finished) {
            System.out.println("Choose your option (exit to leave): ");
            System.out.println("GENERATE: to generate the files");
            System.out.println("WSDL: to get the wsdl file");
            System.out.println("OWL: to get the owl file");
            System.out.println("BPEL: to get the bpel file");
            System.out.println("WSLA: to get the wsla file");
            System.out.println("");
            
            String answer = scan.nextLine();
            
            switch (answer.toUpperCase()) {
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
        GeneratorConfiguration config = new GeneratorConfiguration();
        
        System.out.println("Value for number of concepts (integer):");
        config.setNumberOfConcepts(scan.nextLine());
        
        System.out.println("Value for number of services (integer):");
        config.setNumberOfServices(scan.nextLine());
        
        System.out.println("Value for is solvable (true or false):");
        boolean isSolvable = scan.nextBoolean();
        
        config.setSolvableProblem(isSolvable);
        
        if (isSolvable) {
            System.out.println("Value for solutions list (integers separated by ,):");
            config.setSolutionsList(scan.nextLine());
        }
        
        return config;
    }
}