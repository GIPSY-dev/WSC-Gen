package ca.concordia.cse.gipsy.ws.soap;


import ca.concordia.cse.gipsy.ws.syslog.LoggerUtility;
import de.vs.unikassel.generator.gui.listener.GeneratorGUIListener;
import java.io.File;
import de.vs.unikassel.generator.gui.listener.TaskGenerator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Generator based on GeneratorGUIListener.java in de.vs.unikassel.generator.gui.listener
 * @author mrtnchps
 *
 */
public class Generator {

	public static boolean gipsy = false;                             
	private int numberOfConcepts = 10000;                            
	private int numberOfServices = 4000;                             
	private boolean solvableProblem = true;                          
	private int[] solutionDepths = null;                             
	private int[] solutionsList = null;                      		
	private int completeSolutionDepth = -1;                          
	private int minNumberConcepts = -1;  
	private String outputFolder = null;
	private String bpelFileName = null;
	private String owlFileName = null;
	private String taskWSDLFileName = null;
	private String WSLAFileName = null;
	private String serviceWSDLFileName = null;
	private boolean generateIntermediateFiles = false;
	private TaskGenerator taskGenerator = null;
	private String errorMessages = null;
	private boolean overrideFiles = true;
	private boolean ignoreMinimum = false;
        private boolean isRunning = false;

        private LoggerUtility logUtility;
        
	public Generator() {
            this.logUtility = new LoggerUtility();
	}


	/**
	 * Call this function to set default values for the Generator. 
	 * @throws Exception
	 */
	public void setDefault() throws Exception{
            setOutputFolder(System.getProperty("user.dir"));
            setOverrideFiles(true);	
            
            setNumberOfConcepts(10000);
            setNumberOfServices(4000);
            setSolvableProblem(true);
            solutionDepths = new int[1];
            solutionDepths[0] = 10;
            setSolutionsList(solutionDepths);
            setCompleteSolutionDepth();
            calculateMinNumberConcepts();
            setGipsy(false);
            setBpelFileName("Solution");
            setServiceWSDLFileName("Services");
            setOwlFileName("Taxonomy");
            setTaskWSDLFileName("Challenge");
            setWSLAFileName("Servicelevelagreements");
            setGenerateIntermediateFiles(false);
        }

	/**
	 * Called when all the elements for the generator is setup
	 * @see BrowseOutputFolder() also - this function need to be called or setup before start()
	 * @throws Exception
	 */
	public boolean start() throws Exception{
           this.isRunning = true; 
                       
            File outputFolderPath = new File(this.getOutputFolder());
            taskGenerator = new TaskGenerator(getNumberOfConcepts(), getSolvableProblem(), getSolutionsList(), getNumberOfServices(),
                            outputFolderPath, getBpelFileName(), getServiceWSDLFileName(), getTaskWSDLFileName(), getOwlFileName(), getWSLAFileName(), getGenerateIntermediateFiles());

            taskGenerator.setIsGUI(false);

            try {
                logUtility.log("Main thread in Generator class (start) is started.", LoggerUtility.LOG_TYPES.EVENTS);
            
                Thread taskGeneratorThread = new Thread((Runnable) taskGenerator);
                taskGeneratorThread.start();
                
                taskGeneratorThread.join();
                logUtility.log("Main thread in Generator class (start) is finished.", LoggerUtility.LOG_TYPES.EVENTS);
                this.isRunning = false;
                return true;
            } catch (InterruptedException ex) {
                logUtility.log("Exception when waiting for main thread in generator. " + ex.getMessage(), LoggerUtility.LOG_TYPES.ERROR);
                System.out.println("Exception when waiting for main thread in generator. " + ex.getMessage());
                this.isRunning = false;
                return false;
            }
	}

	/**
	 * Default is 10 000 - This is the number of concepts the user wants
	 * 
	 * If the number is less than 0 - an error will be added in @param errorMessages
	 * @param numberOfConcepts
	 */
	public void setNumberOfConcepts(int numberOfConcepts) {
		if(numberOfConcepts <= 0) {
			// --- > exception could be replaced with an error message in JSP
			errorMessages+=("\n\tPlease enter a number greater than 0 for the number of concepts");
		}else{
			this.numberOfConcepts = numberOfConcepts;
		}
	}
	/**
	 * Getter for numberOfConcepts
	 * 
	 * @return numberOfConcepts
	 */
	public int getNumberOfConcepts(){
		return numberOfConcepts;
	}

	/**
	 * Setter for number of services
	 * 
	 * If the number is less than 0 - an error will be added in @param errorMessages
	 * @param numberOfServices
	 */
	public void setNumberOfServices(int numberOfServices){
		if(numberOfServices <=0){
			errorMessages+=("\n\tPlease enter a number greater than 0 for number of services");
		}else{
			this.numberOfServices = numberOfServices;
		}
	}

	/**
	 * Getter for numberOfServices
	 * @return
	 */
	public int getNumberOfServices(){
		return numberOfServices;
	}

	/**
	 * Setter for solvableProblem
	 * 
	 * If set to false, solutionDepth, completeSolutionDepth and minNumberOfConcepts are not usable
	 * @param solvableProblem is a boolean that enable the calculation of solutionDepths
	 */
	public void setSolvableProblem( boolean solvableProblem){
		this.solvableProblem = solvableProblem;
	}

	/**
	 *  Getter for solvableProblem
	 * @return solvableProblem
	 */
	public boolean getSolvableProblem(){
		return solvableProblem;
	}

	/**
	 * Getter for solutionsList
	 * @return solutionsList is an array of integer that represent solutionDepths asked by the user
	 */
	public int[] getSolutionsList() {
		if(getSolvableProblem() == false){
			errorMessages+=("\n\tGet Solutions list available only if solvable problem set to True");
		}
		if (solutionsList == null) {
			solutionsList = new int[1];
			solutionsList[0]=10;
		}

		return solutionsList;
	}

	/**
	 * Handle the "Add solutions" button
	 * 
	 * If a user input many depth Numbers, it is added to the list
	 * @param depthNumbers is an array of integers bigger than 0
	 */
	public void setSolutionsList(int[] depthNumbers ) {
		int[] solutionsList;
		if(getSolvableProblem() == false){
			errorMessages+=("\n\tSet Solutions list available only if solvable problem set to True");
		}

		if(depthNumbers.length < 1){
			errorMessages+=("\n\tPlease enter a number bigger than 1 for a solution");
		}
		solutionsList = new int[depthNumbers.length];
		for(int i=0; i < depthNumbers.length; i++){
			solutionsList[i] =depthNumbers[i];
		}
		this.solutionsList = solutionsList;
	}

	/**
	 * Total solutionDepth number
	 * 
	 * @return completeSolutionDepth
	 */
	public int getCompleteSolutionDepth(){
		return this.completeSolutionDepth;
	}

	/**
	 * Sum the total solutionDepthList indexes to calculate the completeSolutionDepth
	 */
	public void setCompleteSolutionDepth(){
		int completeSolutionDepth = 0;
		if(getSolvableProblem() == false){
			errorMessages+=("\n\tComplete solution depth available only if solvable problem set to True");
		}
		for(int i=0; i < getSolutionsList().length; i++){
			completeSolutionDepth += getSolutionsList()[i];
		}
		this.completeSolutionDepth = completeSolutionDepth;
	}

	/**
	 * Setter for Gipsy
	 * @param _gipsy
	 */
	public void setGipsy(boolean _gipsy){
		gipsy = _gipsy;
	}

	/**
	 * Getter for Gipsy
	 * @return gipsy
	 */
	public boolean getGispy(){
		return gipsy;
	}

	/**
	 * Set the folder path to save the files
	 * @param outputFolderPath
	 */
	public void setOutputFolder(String outputFolderPath) {
		if(outputFolderPath == null || outputFolderPath.trim().equals("")){
			errorMessages+=("\n\tPlease enter a valid name for the output path");
		}

		// file
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			errorMessages+=("\n\tPath doesn't exist or is incorrect for the output folder");

		}

		if(!outputFolder.isDirectory()) {
			errorMessages+=("\n\tFolder must be a directory");

		}
		this.outputFolder = outputFolderPath;
	}

	public String getOutputFolder(){
		return outputFolder;
	}

	/**
	 * Refactored function from GeneratorGUIListerner.java
	 * 
	 * Returns "true" if the directory contains the given file.
	 * @param directory The directory.
	 * @param fileName The name of the file.
	 * @return "true" if the directory contains the given file otherwise "false".
	 */
	public boolean containsFile(String outputFolderPath, String fileName) {
		File directory = new File(outputFolderPath);
		if(!directory.isDirectory()) {
			return false;
		}

		File[] files = directory.listFiles();
		for(File file : files) {
			if(file.getName().equals(fileName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Refactored function from GeneratorGUIListener.java
	 * 
	 * Setter for Bpel file name
	 * 
	 * 1- Need to be a valid name.
	 * 2- Adding .bpel at the end of the name if it doesn't have it
	 * 
	 * @param bpelFileName
	 * @throws Exception if the user didn't check the UI box to override a file with the same name
	 */
	public void setBpelFileName(String bpelFileName) throws Exception {
		if(bpelFileName == null || bpelFileName.trim().equals("")){
			throw new Exception("Bpel file name must be a valid name");
		}
		if(!bpelFileName.endsWith(".bpel")){
			bpelFileName = bpelFileName+".bpel";
		}
		if(containsFile(getOutputFolder(), bpelFileName) && !getOverrideFiles()){
                    logUtility.log("Files with same name already exist: bpel. Please check box to override or change directory.", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("Files with same name already exist: bpel. Please check box to override or change directory");

		}else{
			// --------------- should ask the user if it is ok to override files
			this.bpelFileName = bpelFileName;	
		}

	}

	/**
	 * 
	 * @return bpel file name
	 * @throws Exception if file name is null or empty
	 */
	public String getBpelFileName() throws Exception{
		if(bpelFileName == null || bpelFileName.trim().equals("")){
                    logUtility.log("bpel file name must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("bpel file name must be valid");
		}
		return bpelFileName;
	}

	/**
	 * Setter for overridingFiles
	 * @param override will allow overriding files with same names in directory
	 */
	public void setOverrideFiles(boolean override){
		this.overrideFiles = override;
	}

	/**
	 * Getter for overriding files
	 * @return overrideFiles
	 */
	public boolean getOverrideFiles(){
		return overrideFiles;
	}

	/**
	 * Setter for OwlFileName
	 * @param owlFileName
	 * @throws Exception if file name is null or empty, also if the user didn't ask to override and the operation will override files
	 */
	public void setOwlFileName(String owlFileName) throws Exception{
		if(owlFileName == null || owlFileName.trim().equals("")){
			throw new Exception("Owl file name must be valid");
		}
		if(!owlFileName.endsWith(".owl")){
			owlFileName = owlFileName+".owl";
		}
		// Check if the file already exists.
		if(containsFile(getOutputFolder(), owlFileName) && !getOverrideFiles()) {
                    logUtility.log("Files with same name already exist: owl. Please check box to override or change directory.", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("Files with same name already exist: owl. Please check box to override or change directory");

		}else{
			this.owlFileName = owlFileName;
		}

	}
	/**
	 * Getter for OwlFileName
	 * @return the owlfilename
	 * @throws Exception if the file is null or empty
	 */
	public String getOwlFileName() throws Exception{
		if(owlFileName == null || owlFileName.trim().equals("")){
                    logUtility.log("owl file name must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("owl file name must be valid");
		}
		return owlFileName;
	}

	/**
	 * Setter for taskWSDLFileName
	 * @param taskWSDLFileName
	 * @throws Exception if file is null or empty, also if the user didn't ask to override and the operation will override files
	 */
	public void setTaskWSDLFileName(String taskWSDLFileName) throws Exception{
		if(taskWSDLFileName == null || taskWSDLFileName.trim().equals("")){
                    logUtility.log("task-wsdl-file must have a valid name", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("task-wsdl-file must have a valid name");
		}
		// Correct file-extension.
		if(!taskWSDLFileName.endsWith(".wsdl")) {
			taskWSDLFileName = taskWSDLFileName+".wsdl";
		}
		// Check if the file already exists.
		if(containsFile(outputFolder, taskWSDLFileName) && !getOverrideFiles()) {
                    logUtility.log("Files with same name already exist: taskWsdlFileName. Please check box to override or change directory.", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("Files with same name already exist: taskWsdlFileName. Please check box to override or change directory");

		}else{
			this.taskWSDLFileName = taskWSDLFileName;
		}

	}

	/**
	 *  Getter
	 * @return taskWSDLFileName
	 * @throws Exception if the file name is empty or null
	 */
	public String getTaskWSDLFileName() throws Exception{
		if(taskWSDLFileName == null || taskWSDLFileName.trim().equals("")){
                    logUtility.log("taskWSDLFileName file name must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("taskWSDLFileName file name must be valid");
		}
		return this.taskWSDLFileName;
	}

	/**
	 * Refactored function from GeneratorGUIListener.java
	 * 
	 * Setter for WSLAFileName
	 * @param WSLAFileName
	 * @throws Exception if empty or null string, also if file will be override and the user didn't ask for it
	 */
	public void setWSLAFileName(String WSLAFileName) throws Exception{
		if(WSLAFileName == null || WSLAFileName.trim().equals("")) {
                    logUtility.log("WSLA file name must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("WSLA file name must be valid");

		}

		// Correct file-extension.
		if(!WSLAFileName.endsWith(".wsla")) {
			WSLAFileName = WSLAFileName+".wsla";
		}
		if(containsFile(outputFolder, WSLAFileName) && !getOverrideFiles()) {
                    logUtility.log("Files with same name already exist: WSLAFileName. Please check box to override or change directory.", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("Files with same name already exist: WSLAFileName. Please check box to override or change directory");

		}else{
			this.WSLAFileName = WSLAFileName;
		}
	}


	/**
	 * Getter
	 * @return WSLAFileName
	 * @throws Exception if string is  null or empty
	 */
	public String getWSLAFileName() throws Exception{
		if(WSLAFileName == null || WSLAFileName.trim().equals("")){
                    logUtility.log("WSLAFileName file name must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("WSLAFileName file name must be valid");
		}
		return this.WSLAFileName;
	}

	/**
	 * Setter
	 * @param serviceWSDLFileName
	 * @throws Exception if file is null or empty, also if the file will be override and the user didn't ask for it
	 */
	public void setServiceWSDLFileName(String serviceWSDLFileName) throws Exception{

		if(serviceWSDLFileName == null || serviceWSDLFileName.trim().equals("")) {
                    logUtility.log("service wsdl file must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("service wsdl file must be valid");
		}
		// Correct file-extension.
		if(!serviceWSDLFileName.endsWith(".wsdl")) {
			serviceWSDLFileName = serviceWSDLFileName+".wsdl";
		}
		// Check if the file already exists.
		if(containsFile(outputFolder, serviceWSDLFileName) && !getOverrideFiles()) {
                    logUtility.log("Files with same name already exist: serviceWSDLFileName. Please check box to override or change directory.", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("Files with same name already exist: serviceWSDLFileName. Please check box to override or change directory");
		}else{
			this.serviceWSDLFileName = serviceWSDLFileName;
		}

	}

	/**
	 * Getter
	 * @return serviceWSDLFileName
	 * @throws Exception
	 */
	public String getServiceWSDLFileName() throws Exception{
		if(serviceWSDLFileName == null || serviceWSDLFileName.trim().equals("")){
                    logUtility.log("serviceWSDLFileName file name must be valid", LoggerUtility.LOG_TYPES.ERROR);
			throw new Exception("serviceWSDLFileName file name must be valid");
		}
		return this.serviceWSDLFileName;
	}

	/**
	 * User can decide if intermediateFiles need to be created or not
	 * 
	 * Setter
	 * @param generateIntermediateFiles
	 */
	public void setGenerateIntermediateFiles(boolean generateIntermediateFiles){
		this.generateIntermediateFiles = generateIntermediateFiles;
	}

	/**
	 * Getter
	 * @return generateIntermediateFiles
	 */
	public boolean getGenerateIntermediateFiles(){
		return this.generateIntermediateFiles;
	}

	/**
	 * User can decide if it wants to ignore the calculated minimum based on the solutiondepths numbers
	 * 
	 * @param ignoreMinimum
	 */
	public void setIgnoreMinimum(boolean ignoreMinimum){
		this.ignoreMinimum = ignoreMinimum;
	}

	/**
	 * Getter
	 * @return ignoreMinimum
	 */
	public boolean getIgnoreMinimum(){
		return ignoreMinimum;
	}

	/**
	 * Calculate the minimum number concepts 
	 */
	public void calculateMinNumberConcepts() {
		int depth = getCompleteSolutionDepth();
		minNumberConcepts = depth * depth;

		if(getNumberOfConcepts() < minNumberConcepts && !getIgnoreMinimum()){		
			errorMessages+=("\n\tNumber of Concepts should be at least "+minNumberConcepts+" based on the added values of solution...Please check the box if you want to ignore");
		}
	}

	public String getErrorMessages(){
		return this.errorMessages;
	}
        
        public File getFile(String filePath) {
            if (this.isRunning) {
                return null;
            }
            
            return new File(this.outputFolder + "/" + filePath);
        }
        
        /**
	 * Handles the "Info"-button.
	 * Displays some informations about us.
	 */
	public String infoButton() {
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(GeneratorGUIListener.class.getClassLoader().getResourceAsStream(GeneratorGUIListener.infoFilePath)));
		StringBuilder infoFileText = new StringBuilder();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
                    logUtility.log("GeneratorGUIListener: An error occurred during the reading of the info-file at "+GeneratorGUIListener.infoFilePath, LoggerUtility.LOG_TYPES.ERROR);
			System.err.println("GeneratorGUIListener: An error occurred during the reading of the info-file at "+GeneratorGUIListener.infoFilePath);
			return "message error";
		}
		return infoFileText.toString();
		
	}
}
