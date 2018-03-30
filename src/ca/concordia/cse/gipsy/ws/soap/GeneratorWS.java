package ca.concordia.cse.gipsy.ws.soap;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Generator based on GeneratorGUIListener.java in de.vs.unikassel.generator.gui.listener
 * @author mrtnchps / jdesorm
 *
 */
@WebService(name="GeneratorWS")
public class GeneratorWS {
    private Generator instance;

    public GeneratorWS() {
        instance = new Generator();

        try {
            instance.setDefault();   
        } catch (Exception ex) {
            System.out.println("Problem when setting the defaults of the generator. Error: " + ex.getMessage());
        }
    }

    @WebMethod(operationName="getFileGenerated")
    public DataHandler getFileGenerated(@WebParam(name="fileType") String fileType) throws Exception {
        String fileName = "";
        
        switch(fileType.toLowerCase()) {
           case "wsdl":
               fileName = instance.getServiceWSDLFileName();
               break;
           case "owl":
               fileName = instance.getOwlFileName();
               break;
           case "wsla":
               fileName = instance.getWSLAFileName();
               break;
           case "bpel":
               fileName = instance.getBpelFileName();
               break;
       }

       if (fileName.isEmpty()) {
           throw new Exception("Invalid file type to get.");
       }

       FileDataSource dataSource = new FileDataSource(instance.getOutputFolder() + "/" + fileName);
       return new DataHandler(dataSource);
    }
    
    /**
     * Call this function to set default values for the Generator. 
     * @see BrowseOutputFolder() also - need to be called
     * @throws Exception
     */
    @WebMethod(operationName="setDefault")
    public void setDefault() throws Exception {
        instance.setDefault();
    }

    /**
     * Called when all the elements for the generator is setup
     * @see BrowseOutputFolder() also - this function need to be called or setup before start()
     * @throws Exception
     */
    @WebMethod(operationName="start")
    public boolean start() throws Exception {
        return instance.start();
    }

    /**
     * Default is 10 000 - This is the number of concepts the user wants
     * 
     * If the number is less than 0 - an error will be added in @param errorMessages
     * @param numberOfConcepts
     */
    @WebMethod(operationName="setNumberOfConcepts")
    public void setNumberOfConcepts(@WebParam(name= "numberOfConcepts") int numberOfConcepts) {
        instance.setNumberOfConcepts(numberOfConcepts);
    }

    /**
     * Setter for number of services
     * 
     * If the number is less than 0 - an error will be added in @param errorMessages
     * @param numberOfServices
     */
    @WebMethod(operationName="setNumberOfServices")
    public void setNumberOfServices(@WebParam(name= "numberOfServices")int numberOfServices){
        instance.setNumberOfServices(numberOfServices);
    }

    /**
     * Setter for solvableProblem
     * 
     * If set to false, solutionDepth, completeSolutionDepth and minNumberOfConcepts are not usable
     * @param solvableProblem is a boolean that enable the calculation of solutionDepths
     */
    @WebMethod(operationName="setSolvableProblem")
    public void setSolvableProblem(@WebParam(name="solvableProblem")  boolean solvableProblem){
        instance.setSolvableProblem(solvableProblem);
    }


    /**
     * Handle the "Add solutions" button
     * 
     * If a user input many depth Numbers, it is added to the list
     * @param depthNumbers is an array of integers bigger than 0
     */
    @WebMethod(operationName="setSolutionsList")
    public void setSolutionsList(@WebParam(name="depthNumbers") int[] depthNumbers ) {
        instance.setSolutionsList(depthNumbers);
    }

    /**
     * Sum the total solutionDepthList indexes to calculate the completeSolutionDepth
     */
    @WebMethod(operationName="setCompleteSolutionDepth")
    public void setCompleteSolutionDepth(){
        instance.setCompleteSolutionDepth();
    }

    /**
     * Setter for Gipsy
     * @param _gipsy
     */
    @WebMethod(operationName="setGipsy")
    public void setGipsy(@WebParam(name= "_gipsy")boolean _gipsy){
        instance.setGipsy(_gipsy);
    }


    /**
     * Setter for Bpel file name
     * @param bpelFileName
     * @throws Exception if the user didn't check the UI box to override a file with the same name
     */
    @WebMethod(operationName="setBpelFileName")
    public void setBpelFileName(@WebParam(name= "bpelFileName")String bpelFileName) throws Exception {
        instance.setBpelFileName(bpelFileName);
    }

    /**
     * Setter for OwlFileName
     * @param owlFileName
     * @throws Exception if file name is null or empty, also if the user didn't ask to override and the operation will override files
     */
    @WebMethod(operationName="setOwlFileName")
    public void setOwlFileName(@WebParam(name="owlFileName") String owlFileName) throws Exception{
        instance.setOwlFileName(owlFileName);
    }

    /**
     * Setter for taskWSDLFileName
     * @param taskWSDLFileName
     * @throws Exception if file is null or empty, also if the user didn't ask to override and the operation will override files
     */
    @WebMethod(operationName="setTaskWSDLFileName")
    public void setTaskWSDLFileName(@WebParam(name="taskWSDLFileName") String taskWSDLFileName) throws Exception{
        instance.setTaskWSDLFileName(taskWSDLFileName);
    }

    /**
     * Refactored function from GeneratorGUIListener.java
     * 
     * Setter for WSLAFileName
     * @param WSLAFileName
     * @throws Exception if empty or null string, also if file will be override and the user didn't ask for it
     */
    @WebMethod(operationName="setWSLAFileName")
    public void setWSLAFileName(@WebParam(name="WSLAFileName") String WSLAFileName) throws Exception{
        instance.setWSLAFileName(WSLAFileName);
    }


    /**
     * Setter
     * @param serviceWSDLFileName
     * @throws Exception if file is null or empty, also if the file will be override and the user didn't ask for it
     */
    @WebMethod(operationName="setServiceWSDLFileName")
    public void setServiceWSDLFileName(@WebParam(name="serviceWSDLFileName")String serviceWSDLFileName) throws Exception{
        instance.setServiceWSDLFileName(serviceWSDLFileName);
    }

    /**
     * User can decide if intermediateFiles need to be created or not
     * 
     * Setter
     * @param generateIntermediateFiles
     */
    @WebMethod(operationName="setGenerateIntermediateFiles")
    public void setGenerateIntermediateFiles(@WebParam(name= "generateIntermediateFiles")boolean generateIntermediateFiles){
        instance.setGenerateIntermediateFiles(generateIntermediateFiles);
    }


    /**
     * User can decide if it wants to ignore the calculated minimum based on the solutiondepths numbers
     * 
     * @param ignoreMinimum
     */
    @WebMethod(operationName="setIgnoreMinimum")
    public void setIgnoreMinimum(@WebParam(name= "ignoreMinimum")boolean ignoreMinimum){
        instance.setIgnoreMinimum(ignoreMinimum);
    }


    /**
     * Calculate the minimum number concepts 
     */
    @WebMethod(operationName="calculateMinNumberConcepts")
    public void calculateMinNumberConcepts() {
        instance.calculateMinNumberConcepts();
    }

    @WebMethod(operationName="getErrorMessages")
    public String getErrorMessages(){
        return instance.getErrorMessages();
    }
    
    /**
     * 
     * @return information about the generator
     */
    @WebMethod(operationName="infoButton")
    public String infoButton(){
        return instance.infoButton();
    }
}
