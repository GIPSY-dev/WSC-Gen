package ca.concordia.cse.gipsy.ws.soap;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFileChooser;


public class Main {

	public static void main(String[] args) throws Exception {
            SoapClient client = new SoapClient();


            client.setDefault();
            
            Scanner scan = new Scanner(System.in);

            boolean finished = false;
            
            while (!finished) {
                System.out.println("");
                System.out.println("Choose your option (-1 to leave): ");
                System.out.println("1- calculateMinNumberConcepts");
                System.out.println("2- getErrorMessages");
                System.out.println("3- getFileGenerated");
                System.out.println("4- infoButton");
                System.out.println("5- setBpelFileName");
                System.out.println("6- setCompleteSolutionDepth");
                System.out.println("7- setDefault");
                System.out.println("8- setGenerateIntermediateFiles");
                System.out.println("9- setGipsy");
                System.out.println("10- setIgnoreMinimum");
                System.out.println("11- setNumberOfConcepts");
                System.out.println("12- setNumberOfServices");
                System.out.println("13- setOwlFileName");
                System.out.println("14- setServiceWSDLFileName");
                System.out.println("15- setSolvableProblem");
                System.out.println("16- setTaskWSDLFileName");
                System.out.println("17- setWSLAFileName");
                System.out.println("18- start");
                System.out.println("19- setSolutionsList");
                System.out.println("");

                String answer = scan.nextLine();

                try {
                    switch (answer) {
                        case "1":
                            client.calculateMinNumberConcepts();
                            break;
                        case "2":
                            System.out.println(client.getErrorMessages());
                            break;
                        case "3":
                            System.out.println("Enter file type (wsdl, wsla, owl, bpel): ");
                            String fileType = scan.nextLine();
                            
                            byte[] fileAsBytes = client.getFileGenerated(fileType);
                            saveToFile(fileAsBytes);
                            break;
                        case "4":
                            System.out.println(client.infoButton());
                            break;
                        case "5":
                            System.out.println("Enter bpel file name: ");
                            String bpelName = scan.nextLine();
                            
                            client.setBpelFileName(bpelName);
                            break;
                        case "6":
                            client.setCompleteSolutionDepth();
                            break;
                        case "7":
                            client.setDefault();
                            break;
                        case "8":
                            System.out.println("Enter true or false: ");
                            String generateIntermediate = scan.nextLine();
                            
                            client.setGenerateIntermediateFiles(Boolean.parseBoolean(generateIntermediate));
                            break;
                        case "9":
                            System.out.println("Enter true or false: ");
                            String gipsy = scan.nextLine();
                            
                            client.setGipsy(Boolean.parseBoolean(gipsy));
                            break;
                        case "10":
                            System.out.println("Enter true or false: ");
                            String ignoreMin = scan.nextLine();
                            
                            client.setIgnoreMinimum(Boolean.parseBoolean(ignoreMin));
                            break;
                        case "11":
                            System.out.println("Enter an integer: ");
                            String numberConcepts = scan.nextLine();
                            
                            client.setNumberOfConcepts(Integer.parseInt(numberConcepts));
                            break;
                        case "12":
                            System.out.println("Enter an integer: ");
                            String numberServices = scan.nextLine();
                            
                            client.setNumberOfServices(Integer.parseInt(numberServices));
                            break;
                        case "13":
                            System.out.println("Enter owl file name: ");
                            String owlName = scan.nextLine();
                            
                            client.setOwlFileName(owlName);
                            break;
                        case "14":
                            System.out.println("Enter service wsdl file name: ");
                            String serviceWSDL = scan.nextLine();
                            
                            client.setServiceWSDLFileName(serviceWSDL);
                            break;
                        case "15":
                            System.out.println("Enter true or false: ");
                            String isSolvable = scan.nextLine();
                            
                            client.setSolvableProblem(Boolean.parseBoolean(isSolvable));
                            break;
                        case "16":
                            System.out.println("Enter task wsdl file name: ");
                            String taskWSDL = scan.nextLine();
                            
                            client.setTaskWSDLFileName(taskWSDL);
                            break;
                        case "17":
                            System.out.println("Enter wsla file name: ");
                            String wslaName = scan.nextLine();
                            
                            client.setWSLAFileName(wslaName);
                            break;
                        case "18":
                            if (client.start()) {
                                System.out.println("Generation successful.");
                            } else {
                                System.out.println("Generation failed.");
                            }
                            break;
                        case "19":
                            System.out.println("Enter list of integer (separated by ,): ");
                            String[] solutionListEntered = scan.nextLine().split(",");
                            
                            ArrayList<Integer> solutionList = new ArrayList<>();
                            
                            for (String s : solutionListEntered) {
                                solutionList.add(Integer.parseInt(s));
                            }
                            
                            client.setSolutionsList(solutionList);
                            break;
                        case "-1":
                            finished = true;
                            break;
                        default:
                            System.out.println("Wrong choice. Please try again.");
                            break;
                    }
                } catch (Exception ex) {
                    System.out.println("Problem with executing your choice. Error: " + ex.getMessage());
                }
            }

            scan.close();

		
	}

        private static File saveToFile(byte[] bytes) throws IOException {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream os = null;
                
                try {
                    os = new FileOutputStream(fileChooser.getSelectedFile().toPath().toString());
                    os.write(bytes);                   
                } catch (Exception ex) {
                    System.out.println("Exception when writing the file to disk. Error " + ex.getMessage());
                } finally {
                    if (os != null) {
                        os.close();
                    }
                }
            }

            return null;
        }
}