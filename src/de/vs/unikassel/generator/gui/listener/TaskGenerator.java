/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.vs.unikassel.generator.gui.listener;

import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;
import de.vs.unikassel.generator.converter.owl_creator.OWL_Creator;
import de.vs.unikassel.generator.converter.wsdl_creator.ServiceConverter;
import de.vs.unikassel.generator.converter.wsdl_creator.TaskConverter;
import de.vs.unikassel.generator.converter.wsla_creator.WSLASingleton;
import de.vs.unikassel.generator.gui.ProgressDialog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.sfc.utils.Utils;
import test.org.sigoa.wsc.c2008.generator.Concept;
import test.org.sigoa.wsc.c2008.generator.Problem;
import test.org.sigoa.wsc.c2008.generator.Rand;

/**
 * A class to create some test-files.
 * @author Marc Kirchhoff
 *
 */
public class TaskGenerator implements Runnable {

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
	private ProgressDialog progressDialog;
	
        /**
         * Represents if the task needs to handle the progress dialog or not.
         */
        private boolean isGUI;
        
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
                this.isGUI = true;
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
	//@Override
	public void run() {
		
		// Create the new task.
		createTask();
		
		// Disable the stop-button and the progress-bar.	
                if (this.isGUI) {
                    this.progressDialog.getStopjButton().setEnabled(false);
                    this.progressDialog.getCreationjProgressBar().setIndeterminate(false);   
                }
	}

	/**
	 * @param progressDialog the progressDialog to set
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}	
        
        public void setIsGUI(boolean isGUI) {
            this.isGUI = isGUI;
        }
}
