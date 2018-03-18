package de.vs.unikassel.generator.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import de.vs.unikassel.generator.gui.GeneratorGUI;
import de.vs.unikassel.generator.gui.ProgressDialog;



/**
 * The listener of the GeneratorGUI-frame.
 * @author Marc Kirchhoff
 */
public class GeneratorGUIListener implements ActionListener, ItemListener{
	
	/**
	 * The boolean value that is used in WSDL_Creator to toggle the service type. 
	 */
	public static boolean gipsy;
	
	public static boolean isGipsy() {
		return gipsy;
	}

	private static void setGipsy(boolean gipsy) {
		GeneratorGUIListener.gipsy = gipsy;
	}

	/**
	 * The GeneratorGUI-frame.
	 */
	private GeneratorGUI generatorGUI;
	
	/**
	 * The path of the info-file which contains some contact-informations.
	 */
	public static String infoFilePath = "de/vs/unikassel/generator/gui/listener/infos.txt";
	
	/**
	 * The path of the warning-file which contains some contact-informations.
	 */
	public static String warningFilePath = "de/vs/unikassel/generator/gui/listener/warning.txt";
	
	/**
	 * Creates a new Listener with the given GeneratorGUI-frame.
	 * @param generatorGUI The GeneratorGUI-frame.
	 */
	public GeneratorGUIListener(GeneratorGUI generatorGUI) {
		this.generatorGUI = generatorGUI;
	}
	
	/**
	 * Handles the "Add Solution"-Button.
	 */
	private void addSolution() {
		// Get the solution-depth.
		String solutionDepthS = this.generatorGUI.getSolutionDepthjTextField().getText();
		this.generatorGUI.getSolutionDepthjTextField().setText("");
		
		// Do some error-checking.
		if(solutionDepthS == null || solutionDepthS.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the solution-depth!","Incorrect Solution-Depth",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getSolutionDepthjTextField().requestFocus();
			return;
		}
		
		// Convert the solution-depth into an integer.
		int solutionDepth = -1;
		try {
			solutionDepth = Integer.parseInt(solutionDepthS);
		}
		catch(NumberFormatException numberFormatException) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The solution-depth must be a number!","Incorrect Solution-Depth",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getSolutionDepthjTextField().requestFocus();
			return;
		}
		
		if(solutionDepth <= 0) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The solution-depth must be greater than 0!","Incorrect Solution-Depth",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getSolutionDepthjTextField().requestFocus();
			return;
		}
		
		// Add the solution to the list.
		((DefaultListModel)this.generatorGUI.getSolutionsjList().getModel()).addElement(solutionDepth);
	}
	
	/**
	 * Handles the "Remove Solution"-button.
	 */
	private void removeSolution() {
		// Get the selected index.
		int selectedIndex = this.generatorGUI.getSolutionsjList().getSelectedIndex();
		if(selectedIndex == -1) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please select a solution!","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		((DefaultListModel)this.generatorGUI.getSolutionsjList().getModel()).remove(selectedIndex);
	}
	
	/**
	 * Handles the "Browse Output Folder"-button.
	 */
	private void browseOutputFolder() {
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
		int option = fileChooser.showOpenDialog(this.generatorGUI);
		
		// Check the return-status.
		if(option == JFileChooser.APPROVE_OPTION) {
			String pathOutputFolder = fileChooser.getSelectedFile().getAbsolutePath();
			this.generatorGUI.getOutputFolderjTextField().setText(pathOutputFolder);
		}
	}
	
	/**
	 * Handles the "Start"-button.
	 */
	private void startButton() {
		// Read the number of concepts.
		String numberOfConceptsS = this.generatorGUI.getNumberOfConceptsjTextField().getText();
		
		if(numberOfConceptsS == null || numberOfConceptsS.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the number of concepts!","Missing Number of Concepts",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
			return;
		}
		
		// Convert number of concepts.
		int numberOfConcepts = -1;
		try {
			numberOfConcepts = Integer.parseInt(numberOfConceptsS);
		}
		catch(NumberFormatException numberFormatException) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Concepts\" must be a number!","Incorrect Number-Of-Concepts",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
			return;
		}
		
		if(numberOfConcepts <= 0) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Concepts\" must be greater than 0!","Incorrect Number-Of-Concepts",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
			return;
		}
		
		// Read the number of services.
		String numberOfServicesS = this.generatorGUI.getNumberOfServicesjTextField().getText();
		
		if(numberOfServicesS == null || numberOfServicesS.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the number of services!","Missing Number of Services",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfServicesjTextField().requestFocus();
			return;
		}
		
		
		
		// Convert number of services.
		int numberOfServices = -1;
		try {
			numberOfServices = Integer.parseInt(numberOfServicesS);
		}
		catch(NumberFormatException numberFormatException) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Services\" must be a number!","Incorrect Number-Of-Services",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfServicesjTextField().requestFocus();
			return;
		}
		
		if(numberOfServices <= 0) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The \"Number-Of-Services\" must be greater than 0!","Incorrect Number-Of-Services",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getNumberOfServicesjTextField().requestFocus();
			return;
		}
		
		
		
		// Read the solvable-problem-checkbox.
		boolean solvableProblem = this.generatorGUI.getSolvablejCheckBox().isSelected();
		
		// Read the solutions-list.
		int[] solutionDepths = null;			
		if(solvableProblem) {
			
			// Get the solution-list-elements.
			Object[] solutionDepthsO = ((DefaultListModel)this.generatorGUI.getSolutionsjList().getModel()).toArray();
			
			if(solutionDepthsO == null || solutionDepthsO.length < 1) {
				JOptionPane.showMessageDialog(this.generatorGUI, "Please enter a solution!","Missing Solution",JOptionPane.ERROR_MESSAGE);
				this.generatorGUI.getSolutionDepthjTextField().requestFocus();
				return;
			}
			
			solutionDepths = new int[solutionDepthsO.length];
			int completeSolutionDepth = 0;
			
			// Convert the values to integer.
			for(int i = 0;i < solutionDepthsO.length; ++i) {
				solutionDepths[i] = (Integer)solutionDepthsO[i];
				completeSolutionDepth += solutionDepths[i];
			}
			
			// Calculate the min number of concepts.
			int minNumberOfConcepts = completeSolutionDepth * completeSolutionDepth;			
			if(numberOfConcepts < minNumberOfConcepts) {
				int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The \"Number-Of-Concepts\" should be at least "+minNumberOfConcepts+"!\n Ignore?","Incorrect Number-Of-Concepts", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if(result != JOptionPane.YES_OPTION) {
					this.generatorGUI.getNumberOfConceptsjTextField().setText(String.valueOf(minNumberOfConcepts));
					this.generatorGUI.getNumberOfConceptsjTextField().requestFocus();
					return;
				}
			}
		}
		
		setGipsy(this.generatorGUI.getGIPSYCheckbox().isSelected());
		
		// Read the output-folder-path.
		String outputFolderPath = this.generatorGUI.getOutputFolderjTextField().getText();
		
		if(outputFolderPath == null || outputFolderPath.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the path of the output-folder!","Incorrect Output-Folder",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOutputFolderjTextField().requestFocus();
			return;
		}
		
		File outputFolder = new File(outputFolderPath);
		if(!outputFolder.exists()) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The output-folder doesn't exist!","Incorrect Output-Folder",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOutputFolderjTextField().requestFocus();
			return;
		}
		
		if(!outputFolder.isDirectory()) {
			JOptionPane.showMessageDialog(this.generatorGUI, "The output-folder must be a directory!","Incorrect Output-Folder",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOutputFolderjTextField().requestFocus();
			return;
		}
					
		
		
		// Read the name of the bpel-file.
		String bpelFileName = this.generatorGUI.getBpelFileNamejTextField().getText();
		
		if(bpelFileName == null || bpelFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the BPEL-file!","Missing BPEL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getBpelFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!bpelFileName.endsWith(".bpel")) {
			bpelFileName = bpelFileName+".bpel";
		}			
		
		// Check if the file already exists.
		if(containsFile(outputFolder, bpelFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+bpelFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getBpelFileNamejTextField().requestFocus();
				return;
			}
		}
		
		
		
		// Read the owl-file-name.
		String owlFileName = this.generatorGUI.getOwlFileNamejTextField().getText();
		
		if(owlFileName == null || owlFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the OWL-file!","Missing OWL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getOwlFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!owlFileName.endsWith(".owl")) {
			owlFileName = owlFileName+".owl";
		}			
		
		// Check if the file already exists.
		if(containsFile(outputFolder, owlFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+owlFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getOwlFileNamejTextField().requestFocus();
				return;
			}
		}
		
		// Read name of the task-wsdl-file.
		String taskWSDLFileName = this.generatorGUI.getTaskWSDLFileNamejTextField().getText();
		
		if(taskWSDLFileName == null || taskWSDLFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the Task-WSDL-file!","Missing Task-WSDL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getTaskWSDLFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!taskWSDLFileName.endsWith(".wsdl")) {
			taskWSDLFileName = taskWSDLFileName+".wsdl";
		}
		
		// Check if the file already exists.
		if(containsFile(outputFolder, taskWSDLFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+taskWSDLFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getTaskWSDLFileNamejTextField().requestFocus();
				return;
			}
		}	
		
		// Read name of the task-wsla-file.
		String WSLAFileName = this.generatorGUI.getWslaFileNamejTextField().getText();
		
		if(WSLAFileName == null || WSLAFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the Task-WSLA-file!","Missing Task-WSLA-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getWslaFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!WSLAFileName.endsWith(".wsla")) {
			WSLAFileName = WSLAFileName+".wsla";
		}
		
		// Check if the file already exists.
		if(containsFile(outputFolder, WSLAFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+WSLAFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getWslaFileNamejTextField().requestFocus();
				return;
			}
		}	
		
		// Read the name of the services-wsdl-file.
		String serviceWSDLFileName = this.generatorGUI.getServicesWSDLFileNamejTextField().getText();
		
		if(serviceWSDLFileName == null || serviceWSDLFileName.trim().equals("")) {
			JOptionPane.showMessageDialog(this.generatorGUI, "Please enter the name of the Services-WSDL-file!","Missing Services-WSDL-File-Name",JOptionPane.ERROR_MESSAGE);
			this.generatorGUI.getServicesWSDLFileNamejTextField().requestFocus();
			return;
		}
		
		// Correct file-extension.
		if(!serviceWSDLFileName.endsWith(".wsdl")) {
			serviceWSDLFileName = serviceWSDLFileName+".wsdl";
		}
		
		// Check if the file already exists.
		if(containsFile(outputFolder, serviceWSDLFileName)) {
			int result = JOptionPane.showConfirmDialog(this.generatorGUI, "The file "+serviceWSDLFileName+" already exists!\n Overwrite?","File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(result != JOptionPane.YES_OPTION) {
				this.generatorGUI.getServicesWSDLFileNamejTextField().requestFocus();
				return;
			}
		}			
		
		// Get the intermediate-files-checkbox.
		boolean generateIntermediateFiles = this.generatorGUI.getIntermediateFilesjCheckBox().isSelected();
		
		// Create the "TaskGeneratorThread".
		// This thread creates the files.
		TaskGenerator taskGenerator = new TaskGenerator(numberOfConcepts, solvableProblem, solutionDepths, numberOfServices,
				outputFolder, bpelFileName, serviceWSDLFileName, taskWSDLFileName, owlFileName, WSLAFileName, generateIntermediateFiles);
		
		Thread taskGeneratorThread = new Thread(taskGenerator);
		
		// Create a progress-dialog which shows an progress-bar with an cancel-button to stop the creation.
		ProgressDialog progressDialog = new ProgressDialog(this.generatorGUI, taskGeneratorThread);
		taskGenerator.setProgressDialog(progressDialog);
		
		taskGeneratorThread.start();
		
		progressDialog.setVisible(true);		
	}

	
	/**
	 * Handles the "Info"-button.
	 * Displays some informations about us.
	 */
	private void infoButton() {
		// Read info-file.
		BufferedReader infoFileReader = new BufferedReader(new InputStreamReader(GeneratorGUIListener.class.getClassLoader().getResourceAsStream(GeneratorGUIListener.infoFilePath)));
		StringBuffer infoFileText = new StringBuffer();
		String line = null;
		
		try {
			while((line = infoFileReader.readLine()) != null) {
				infoFileText.append(line);
				infoFileText.append("\n");
			}
		} catch (IOException exception) {
			System.err.println("GeneratorGUIListener: An error occurred during the reading of the info-file at "+GeneratorGUIListener.infoFilePath);
			exception.printStackTrace();
			return;
		}
		
		JOptionPane.showMessageDialog(this.generatorGUI, infoFileText.toString(),"Web Service Challenge 2008 Test Generator",JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * This method is called when a button is pressed on the GeneratorGUI-frame.
	 */
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		
		// Adds a new solutions to the solution-list.
		if(actionEvent.getActionCommand().equals("Add Solution")) {			
			addSolution();
		}
		// Remove a solution from the list.
		else if(actionEvent.getActionCommand().equals("Remove Solution")) {
			removeSolution();
		}
		// Display a file-chooser.		
		else if(actionEvent.getActionCommand().equals("Browse Output Folder")) {
			browseOutputFolder();			
		}
		// Start the creation of the documents.
		else if(actionEvent.getActionCommand().equals("Start")) {
			startButton();
			System.gc();
		}
		else if(actionEvent.getActionCommand().equals("Info")) {
			infoButton();
		}
	}
	
	/**
	 * Removes the given services from a service-description-file.
	 * @param servicesInputStream The service-description-file.
	 * @param services The names of the services.
	 * @return
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
	 * Returns "true" if the directory contains the given file.
	 * @param directory The directory.
	 * @param fileName The name of the file.
	 * @return "true" if the directory contains the given file otherwise "false".
	 */
	private boolean containsFile(File directory, String fileName) {
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
	 * This method is called when the user changes the state of the "solvable"-Checkbox.
	 * @param itemEvent The event that is thrown.
	 */
	@Override
	public void itemStateChanged(ItemEvent itemEvent) {
		if(itemEvent.getItem() instanceof JCheckBox) {
			JCheckBox checkBox = (JCheckBox)itemEvent.getItem();
			
			// Chance the state of the solution-components.
			if(checkBox.getName().equals("solvableCheckBox")) {
				if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
					this.generatorGUI.getSolutionsjLabel().setEnabled(true);
					this.generatorGUI.getSolutionsjList().setEnabled(true);					
					this.generatorGUI.getSolutionsHelpjLabel().setEnabled(true);
					this.generatorGUI.getSolutionsColonjLabel().setEnabled(true);					
					this.generatorGUI.getSolutionDepthjLabel().setEnabled(true);
					this.generatorGUI.getSolutionDepthjTextField().setEnabled(true);
					this.generatorGUI.getSolutionDepthHelpjLabel().setEnabled(true);
					this.generatorGUI.getSolutionDepthColonjLabel().setEnabled(true);
					this.generatorGUI.getAddSolutionjButton().setEnabled(true);
					this.generatorGUI.getRemoveSolutionjButton().setEnabled(true);
				}
				else {
					this.generatorGUI.getSolutionsjLabel().setEnabled(false);
					this.generatorGUI.getSolutionsjList().setEnabled(false);
					this.generatorGUI.getSolutionsHelpjLabel().setEnabled(false);
					this.generatorGUI.getSolutionsColonjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthHelpjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthColonjLabel().setEnabled(false);
					this.generatorGUI.getSolutionDepthjTextField().setEnabled(false);
					this.generatorGUI.getAddSolutionjButton().setEnabled(false);
					this.generatorGUI.getRemoveSolutionjButton().setEnabled(false);
				}
			}
		}
	}
}
