package ca.concordia.cse.gipsy.ws.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.sfc.utils.Utils;

import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;
import de.vs.unikassel.generator.converter.owl_creator.OWL_Creator;
import de.vs.unikassel.generator.converter.wsdl_creator.ServiceConverter;
import de.vs.unikassel.generator.converter.wsdl_creator.TaskConverter;
import de.vs.unikassel.generator.converter.wsla_creator.WSLASingleton;
import test.org.sigoa.wsc.c2008.generator.Concept;
import test.org.sigoa.wsc.c2008.generator.Problem;
import test.org.sigoa.wsc.c2008.generator.Rand;

/**
 * 
 * Generator refactor functions from GeneratorGui.java but without the GUI component for a web application.
 * Also included a copy of TaskGenerator to generate files.
 * 
 * 
 * 
 * Using TaskGenerator from de.vs.unikassel.generator.gui.listener
 * A class to create some test-files.
 * TaskGenerator @author Marc Kirchhoff
 *
 */
class TaskGenerator implements Runnable {

	/**
	 * The number of concepts the taxonomy should contain.
	 */
	private int numberOfConcepts;
	
	/**
	 * Should the problem be solvable.
	 */
	private boolean solvableProblem;
	
	/**
	 * The depths of the solutions.
	 */
	private int[] solutionDepths;
	
	/**
	 * The number of services the generator should create.
	 */
	private int numberOfServices;
	
	/**
	 * The output-folder that should contain the output-files.
	 */
	private File outputFolder;
	
	/**
	 * The name of the BPEL-file that the generator should create.
	 */
	private String bpelFileName;
	
	/**
	 * The name of the Services-WSDL-File that the generator should create.
	 */
	private String serviceWSDLFileName;
	
	/**
	 * The name of the Task-WSDL-file that the generator should create.
	 */
	private String taskWSDLFileName;
	
	/**
	 * The name of the QoS-WSLA-file that the generator should create.
	 */
	private String WSLAFileName;
	
	/**
	 * The name of the OWL-File that the generator should create.
	 */
	private String owlFileName;
	
	/**
	 * Create the intermediate-files?
	 */
	private boolean generateIntermediateFiles;
	
	/**
	 * The dialog that displays the progress-panel.
	 */
	//private ProgressDialog progressDialog;
	
	/**
	 * Creates a TaskGenerator.
	 * @param numberOfConcepts The number of concepts the taxonomy should contain.
	 * @param solvableProblem Should the problem be solvable.
	 * @param solutionDepths The depths of the solutions.
	 * @param numberOfServices The number of services the generator should create.
	 * @param outputFolder The output-folder that should contain the output-files.
	 * @param bpelFileName The name of the BPEL-file that the generator should create.
	 * @param serviceWSDLFileName The name of the Services-WSDL-File that the generator should create.
	 * @param taskWSDLFileName The name of the Task-WSDL-file that the generator should create.
	 * @param owlFileName The name of the OWL-File that the generator should create.
	 * @param generateIntermediateFiles The dialog that displays the progress-panel.
	 */
	public TaskGenerator(int numberOfConcepts, boolean solvableProblem, int[] solutionDepths, int numberOfServices,
			File outputFolder, String bpelFileName, String serviceWSDLFileName, String taskWSDLFileName, String owlFileName, String wslafilename, boolean generateIntermediateFiles) {
		this.numberOfConcepts = numberOfConcepts;
		this.solvableProblem = solvableProblem;
		this.solutionDepths = solutionDepths;
		this.numberOfServices = numberOfServices;
		this.outputFolder = outputFolder;
		this.bpelFileName = bpelFileName;
		this.serviceWSDLFileName = serviceWSDLFileName;
		this.taskWSDLFileName = taskWSDLFileName;
		this.owlFileName = owlFileName;
		this.WSLAFileName = wslafilename;
		this.generateIntermediateFiles = generateIntermediateFiles;
	}
	
	/**
	 * Removes the given services from a service-description-file.
	 * @param servicesInputStream The service-description-file.
	 * @param services The names of the services.
	 * @return The service-description-document without the given services.
	 */
	private ByteArrayOutputStream removeServices(InputStream servicesInputStream, Vector<String> services) {

		
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Remove the given service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			for(Element element : serviceElements) {
				if(services.contains(element.getAttributeValue("name"))) {
					doc.getRootElement().removeContent(element);
					break;
				}
			}
			
			ByteArrayOutputStream wsdl = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, wsdl );
			
			return wsdl;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Removes all services from the service-description-file that provide at least one of the specified output-instances.
	 * @param servicesInputStream The service-description-file.
	 * @param outputs The output-instance.
	 * @return The service-description-document without the services.
	 */
	private ByteArrayOutputStream removeAllServicesWithSpecifiedOutput(InputStream servicesInputStream, Vector<String> outputs) {
		
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Remove the given service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			Vector<Element> removeServicesElements = new Vector<Element>();
			
			for(Element serviceElement : serviceElements) {
				Element outputElement = serviceElement.getChild("outputs");
				List<Element> outputInstanceElements = outputElement.getChildren("instance");
				
				for(Element outputInstanceElement : outputInstanceElements) {
					
					if(outputs.contains(outputInstanceElement.getAttributeValue("name"))) {
						//doc.getRootElement().removeContent(serviceElement);
						removeServicesElements.add(serviceElement);
						break;
					}
				}
			}
			
			for(Element serviceElement : removeServicesElements) {
				doc.getRootElement().removeContent(serviceElement);
			}
			
			ByteArrayOutputStream wsdl = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, wsdl );
			
			return wsdl;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates a new concept and a new instance and adds it to the taxonomy below the first concept.
	 * @param taxonomyInputStream The taxonomy-file.
	 * @param newInstanceName This parameter will contain the name of the new generated instance after the execution.
	 * @return The taxonomy-file with the new concept and new instance. 
	 */
	private ByteArrayOutputStream createNewConceptAndInstance(InputStream taxonomyInputStream, StringBuffer newInstanceName) {
				
		try {
			Document doc = new SAXBuilder().build( taxonomyInputStream );
			
			// Get the first concept-element.
			Element firstConceptElement = doc.getRootElement().getChild("concept");
			
			// Create the new concept-element.
			String conceptName = "con"+Rand.nextId();
			String instanceName = "inst"+Rand.nextId();
			newInstanceName.append(instanceName);
			
			Element conceptElement = new Element("concept");
			conceptElement.setAttribute("name", conceptName);
			
			Element instanceElement = new Element("instance");
			instanceElement.setAttribute("name", instanceName);
			
			conceptElement.addContent(instanceElement);
			
			// Add the new concept to the first concept-element of the taxonomy.
			firstConceptElement.addContent(conceptElement);
			
			ByteArrayOutputStream taxonomy = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, taxonomy );
			return taxonomy;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	private ByteArrayOutputStream addNewInputInstance(InputStream servicesInputStream, Vector<String> outputConcepts, String newInstanceName) {
		try {
			Document doc = new SAXBuilder().build( servicesInputStream );
			
			// Get all service-elements.
			List<Element> serviceElements = doc.getRootElement().getChildren("service");
			
			for(Element serviceElement : serviceElements) {
				Element outputElement = serviceElement.getChild("outputs");
				List<Element> outputInstanceElements = outputElement.getChildren("instance");
				
				// Check if this service provides one of the specified output-elements.
				boolean found = false;
				for(Element outputInstanceElement : outputInstanceElements) {					
					if(outputConcepts.contains(outputInstanceElement.getAttributeValue("name"))) {
						found = true;
						break;
					}
				}
				
				// Add the new instance to the input-elements.
				if(found) {					
					// Create the new input-element.
					Element newInputElement = new Element("instance");
					newInputElement.setAttribute("name", newInstanceName);
					
					// Add the new input-element to the input-instances.
					Element inputsElement = serviceElement.getChild("inputs");
					inputsElement.addContent(newInputElement);					
				}				
			}
			
			ByteArrayOutputStream serviceDescription = new ByteArrayOutputStream();
			XMLOutputter out = new XMLOutputter( Format.getPrettyFormat() ); 
			out.output( doc, serviceDescription );
			
			return serviceDescription;
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Collects all sub-concepts of the specified concept.
	 * @param concept The concept that sub-concepts should be added to the specified vector.
	 * @param allConcepts Contains all concepts after the execution of this method.
	 */
	private void getAllConcepts(Concept concept, Vector<String> allConcepts) {
		if(concept.getM_children() == null) {
			return;
		}
		
		for(int i = 0; i < concept.getM_children().size(); ++i )  {
			Concept currentConcept = concept.getM_children().get(i);
			if(currentConcept == null) {
				continue;
			}
			
			// Add the name of the concept to the vector.
			StringBuilder conceptName = new StringBuilder();
			currentConcept.toStringBuilder(conceptName);
			allConcepts.add(conceptName.toString());
			
			this.getAllConcepts(currentConcept, allConcepts);
		}
	}
		
	/**
	 * Creates a new task.
	 */
	private void createTask() {
		// Create the intermediate-files.
		Concept concepts = null;		
		Problem problem = null;
		
		do {
			concepts = null;	
			problem = null;
			
			Utils.invokeGC();
			concepts = Concept.createConceptTree(this.numberOfConcepts);
			problem =  Problem.buildProblem(concepts, this.solvableProblem ? this.solutionDepths : new int[] { 1 });
			System.out.println("Please wait...");
		} while (problem == null);
		
		ByteArrayOutputStream conceptsByteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream problemArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream servicesArrayOutputStream = new ByteArrayOutputStream();			
		
		concepts.serialize(conceptsByteArrayOutputStream);		
		problem.serializeProblemFile(problemArrayOutputStream);
		problem.serializeServiceFile(servicesArrayOutputStream);
		problem.generateServices(this.numberOfServices);
		
		//ByteArrayInputStream conceptsByteArrayInputStream = new ByteArrayInputStream(conceptsByteArrayOutputStream.toByteArray());
		//ByteArrayInputStream problemByteArrayInputStream = new ByteArrayInputStream(problemArrayOutputStream.toByteArray());		
		
		if(!this.solvableProblem) {
			
			StringBuffer newInstanceName = new StringBuffer();
			conceptsByteArrayOutputStream = this.createNewConceptAndInstance(new ByteArrayInputStream(conceptsByteArrayOutputStream.toByteArray()), newInstanceName);
			
			Concept oneNecessaryOutputConcept = problem.getM_out().get(0);
			Vector<String> allSubConccepts = new Vector<String>();
			this.getAllConcepts(oneNecessaryOutputConcept, allSubConccepts);
			
			servicesArrayOutputStream = this.addNewInputInstance(new ByteArrayInputStream(servicesArrayOutputStream.toByteArray()), allSubConccepts, newInstanceName.toString());
			
		}	
				
		// Create the service-wsdl-document.
		ServiceConverter serviceConverter = new ServiceConverter(new ByteArrayInputStream(servicesArrayOutputStream.toByteArray()));
		Thread serviceConverterThread = new Thread(serviceConverter);
		serviceConverterThread.start();
		
		// Create the wsdl-request-document.
		TaskConverter taskConverter = new TaskConverter(new ByteArrayInputStream(problemArrayOutputStream.toByteArray()));
		Thread taskConverterThread = new Thread(taskConverter);
		taskConverterThread.start();
					
		// Create the owl-document.			
		OWL_Creator owlCreator = new OWL_Creator(new ByteArrayInputStream(conceptsByteArrayOutputStream.toByteArray()));
		Thread owlCreatorThread = new Thread(owlCreator);
		owlCreatorThread.start();
		
		// Wait until all threads are stopped.
		try {
			if(this.solvableProblem) {
				// Create the bpel-document.
				BPEL_Creator bpelCreator = new BPEL_Creator(new ByteArrayInputStream(problemArrayOutputStream.toByteArray()));
				Thread bpelCreatorThread = new Thread(bpelCreator);
				bpelCreatorThread.start();
				bpelCreatorThread.join();
				bpelCreator.saveBPELDocument(new File(this.outputFolder, this.bpelFileName));
			}
			
			serviceConverterThread.join();
			taskConverterThread.join();
			owlCreatorThread.join();
			
		} catch (InterruptedException exception) {
			System.err.println("GeneratorGUI: An error occurred during creation of the documents.");
			exception.printStackTrace();
			return;
		}
		
		// Save the created documents.
		serviceConverter.saveWSDLFile(new File(this.outputFolder,this.serviceWSDLFileName));
		taskConverter.saveWSDLFile(new File(this.outputFolder, this.taskWSDLFileName));		
		owlCreator.saveOWLFile(new File(this.outputFolder, this.owlFileName));
		WSLASingleton.saveWSLAFile(new File(this.outputFolder,this.WSLAFileName));
		
		// Save Intermediate-Files?
		if(generateIntermediateFiles) {
			File taxonomyFile = new File(this.outputFolder, "taxonomy.xml");
			File problemFile = new File(this.outputFolder, "problem.xml");
			File servicesFile = new File(this.outputFolder, "services.xml");
							
			try {
				FileWriter taxonomyFileWriter = new FileWriter(taxonomyFile);
				FileWriter problemFileWriter = new FileWriter(problemFile);
				FileWriter servicesFileWriter = new FileWriter(servicesFile);
				
				taxonomyFileWriter.write(conceptsByteArrayOutputStream.toString());
				problemFileWriter.write(problemArrayOutputStream.toString());
				servicesFileWriter.write(servicesArrayOutputStream.toString());
				
				taxonomyFileWriter.flush();
				problemFileWriter.flush();
				servicesFileWriter.flush();
				
				taxonomyFileWriter.close();
				problemFileWriter.close();
				servicesFileWriter.close();
				
			} catch (IOException exception) {
				System.err.println("GeneratorGUI: An error occurred during saving of the intermediate-files.");
				exception.printStackTrace();
				return;
			}
		}		
	}
	
	/**
	 * Starts the creation of the new task.
	 */
	@Override
	public void run() {
		
		// Create the new task.
		createTask();
		
		// Disable the stop-button and the progress-bar.		
		//this.progressDialog.getStopjButton().setEnabled(false);
		//this.progressDialog.getCreationjProgressBar().setIndeterminate(false);
	}

	/**
	 * @param progressDialog the progressDialog to set
	 */
	//public void setProgressDialog(ProgressDialog progressDialog) {
	//	this.progressDialog = progressDialog;
	//}	
}


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
	private File outputFolder = null;
	private String bpelFileName = null;
	private String owlFileName = null;
	private String taskWSDLFileName = null;
	private String WSLAFileName = null;
	private String serviceWSDLFileName = null;
	private boolean generateIntermediateFiles = false;
	private TaskGenerator taskGenerator = null;
	private ArrayList<String> errorMessages = null;
	private boolean overrideFiles = true;
	private boolean ignoreMinimum = false;
	
	Generator() {
        
	}
	
	
	/**
	 * Call this function to set default values for the Generator. 
	 * @see BrowseOutputFolder() also - need to be called
	 * @throws Exception
	 */
	public void setDefault() throws Exception{
		
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
	public void start() throws Exception{
		taskGenerator = new TaskGenerator(getNumberOfConcepts(), getSolvableProblem(), getSolutionsList(), getNumberOfServices(),
				getOutputFolder(), getBpelFileName(), getServiceWSDLFileName(), getTaskWSDLFileName(), getOwlFileName(), getWSLAFileName(), getGenerateIntermediateFiles());
		Thread taskGeneratorThread = new Thread((Runnable) taskGenerator);
		taskGeneratorThread.start();
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
			errorMessages.add("Please enter a number greater than 0 for the number of concepts");
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
			errorMessages.add("Please enter a number greater than 0 for number of services");
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
			errorMessages.add("Get Solutions list available only if solvable problem set to True");
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
			errorMessages.add("Set Solutions list available only if solvable problem set to True");
		}
		
		if(depthNumbers.length < 1){
			errorMessages.add("Please enter a number bigger than 1 for a solution");
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
			errorMessages.add("Complete solution depth available only if solvable problem set to True");
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
			errorMessages.add("Please enter a valid name for the output path");
		}
		
		// file
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			errorMessages.add("Path doesn't exist or is incorrect for the output folder");
			
		}
		
		if(!outputFolder.isDirectory()) {
			errorMessages.add("Folder must be a directory");
			
		}
		this.outputFolder = outputFolder;
	}
	
	public File getOutputFolder(){
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
	public boolean containsFile(File directory, String fileName) {
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
			throw new Exception("task-wsdl-file must have a valid name");
		}
		// Correct file-extension.
		if(!taskWSDLFileName.endsWith(".wsdl")) {
			taskWSDLFileName = taskWSDLFileName+".wsdl";
		}
		// Check if the file already exists.
		if(containsFile(outputFolder, taskWSDLFileName) && !getOverrideFiles()) {
			
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
			throw new Exception("WSLA file name must be valid");
			
		}
		
		// Correct file-extension.
		if(!WSLAFileName.endsWith(".wsla")) {
			WSLAFileName = WSLAFileName+".wsla";
		}
		if(containsFile(outputFolder, WSLAFileName) && !getOverrideFiles()) {
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
			throw new Exception("service wsdl file must be valid");
		}
		// Correct file-extension.
		if(!serviceWSDLFileName.endsWith(".wsdl")) {
			serviceWSDLFileName = serviceWSDLFileName+".wsdl";
		}
		// Check if the file already exists.
		if(containsFile(outputFolder, serviceWSDLFileName) && !getOverrideFiles()) {
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
			errorMessages.add("Number of Concepts should be at least "+minNumberConcepts+" based on the added values of solution...Please check the box if you want to ignore");
		}
	}
	
	/**
	 * Browse folders....Refactored method from GeneratorGUIListener.java
	 */
	public void browseOutputFolder(){
		// Create a file-chooser.
		JFileChooser fileChooser=new JFileChooser();
		
		// Restrict the selection to directories.
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		// Set the file-filter. Only directories are shown.
		fileChooser.setFileFilter(new FileFilter() {	
			
			@Override
			public boolean accept(File file) {
				if(file.isDirectory()) {
					return true;
				}
				else {
					return false;
				}
			}

			@Override
			public String getDescription() {
				return "Folders";					
			}
		});
		
		// Display the file-chooser.
		int option = fileChooser.showOpenDialog(fileChooser);
		
		// Check the return-status.
		if(option == JFileChooser.APPROVE_OPTION) {
			String pathOutputFolder = fileChooser.getSelectedFile().getAbsolutePath();
			setOutputFolder(pathOutputFolder);
		}
	}


	public ArrayList getErrorMessages(){
		return this.errorMessages;
	}

}
