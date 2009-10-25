package ca.concordia.pga.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;
import de.vs.unikassel.query.client.callback_interface.stub.CallbackService;
import de.vs.unikassel.query.client.callback_interface.stub.ClientCallbackInterfaceService;
import de.vs.unikassel.query.server.CompositionSystemInterface;

/**
 * This is the planning graph implementation of WSC'08 & '09's composition system
 * @author Ludeng Zhao(Eric)
 *
 */
@WebService(name = "CompositionSystem", targetNamespace = "http://server.query.unikassel.vs.de/")
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class CompositionSystemImpl extends CompositionSystemInterface {
	
	/**
	 * Indicates if the composition was stopped.
	 */
	private boolean stopped;

	PlanningGraph pg = new PlanningGraph();

	private static Map<String, Concept> conceptMap;
	private static Map<String, Thing> thingMap;
	private static Map<String, Service> serviceMap;
	private static Map<String, Param> paramMap;

		
	/**
	 * Creates a new Composition-System.
	 */
	public CompositionSystemImpl() {
		// Do nothing.
	}

	/**
	 * Parse taxonomy document from given URL
	 * @param conceptMap
	 * @param thingMap
	 * @param url
	 * @throws DocumentException
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	private static void parseTaxonomyDocument(Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, String url) throws DocumentException, URISyntaxException {
		File taxonomyFile = new File(new URI(url));
		SAXReader reader = new SAXReader();
		Document document = reader.read(taxonomyFile);
		Element taxonomyRoot = document.getRootElement();

		/**
		 * loop through semantic elements to check taxonomy
		 */
		for (Iterator i = taxonomyRoot.elementIterator(); i.hasNext();) {
			Element el = (Element) i.next();
			if (el.getName().equals("Class")) {
				Concept concept = new Concept(el.attribute("ID").getText());
				if (el.element("subClassOf") != null) {
					concept.setDirectParantName(el.element("subClassOf")
							.attribute("resource").getText()
							.replaceAll("#", ""));

				} else {
					concept.setRoot(true);
				}
				conceptMap.put(concept.getName(), concept);

			} else if (el.getName().equals("Thing")) {
				Thing thing = new Thing(el.attribute("ID").getText());

				thing.setType(el.element("type").attribute("resource")
						.getText().replaceAll("#", ""));

				thingMap.put(thing.getName(), thing);
			}
		}

		/**
		 * build indexing for concept
		 */
		for (String key : conceptMap.keySet()) {
			Concept concept = conceptMap.get(key);
			Concept varConcept = conceptMap.get(key);
			do {
				concept.addConceptToParentIndex(varConcept);
				varConcept.addConceptToChildrenIndex(concept);
				if (varConcept.isRoot()) {
					varConcept = null;
				} else {
					varConcept = conceptMap.get(varConcept
							.getDirectParantName());
				}

			} while (varConcept != null);

		}

	}

	/**
	 * Parse services document from given URL
	 * @param serviceMap
	 * @param paramMap
	 * @param conceptMap
	 * @param thingMap
	 * @param url
	 * @throws DocumentException
	 * @throws URISyntaxException 
	 */
	@SuppressWarnings("unchecked")
	private static void parseServicesDocument(Map<String, Service> serviceMap,
			Map<String, Param> paramMap, Map<String, Concept> conceptMap,
			Map<String, Thing> thingMap, String url) throws DocumentException, URISyntaxException {

		File ServicesFile = new File(new URI(url));
		SAXReader reader = new SAXReader();
		Document document = reader.read(ServicesFile);
		Element servicesRoot = document.getRootElement();
		Element semRoot = servicesRoot.element("semExtension");

		/**
		 * loop through semantic elements
		 */
		Service service = null;

		for (Iterator i = semRoot.elementIterator(); i.hasNext();) {
			Element semMsgExtEl = (Element) i.next();
			if (semMsgExtEl.getName().equals("semMessageExt")) {
				boolean isRequestParam;
				if (semMsgExtEl.attribute("id").getText().contains(
						"RequestMessage")) {
					service = new Service(semMsgExtEl.attribute("id").getText()
							.replaceAll("RequestMessage", ""));
					isRequestParam = true;
				} else {
					isRequestParam = false;
				}

				for (Iterator j = semMsgExtEl.elementIterator(); j.hasNext();) {
					Element semExtEl = (Element) j.next();
					if (semExtEl.getName().equals("semExt")) {
						Param param = new Param(semExtEl.attribute("id")
								.getText());
						Thing thing = thingMap.get(semExtEl.element(
								"ontologyRef").getText().replaceAll(
								"http://www.ws-challenge.org/wsc08.owl#", ""));

						param.setThing(thing);
						paramMap.put(param.getName(), param);
						if (isRequestParam) {
							service.addInputParam(param);
							service.addInputConcept(conceptMap.get(thing.getType()));
						} else {
							service.addOutputParam(param);
							for (Concept c : conceptMap.get(thing.getType())
									.getParentConceptsIndex()) {
								service.addOutputConcept(c);
							}
						}
					}
				}
				if (semMsgExtEl.attribute("id").getText().contains(
						"ResponseMessage")) {
					serviceMap.put(service.getName(), service);
				}

			}
		}
	}

	/**
	 * Build inverted indexing table: concept -> all services who accept the concept
	 * @param conceptMap
	 * @param serviceMap
	 */
	private static void buildInvertedIndex(Map<String, Concept> conceptMap,
			Map<String, Service> serviceMap) {
		for (String serviceKey : serviceMap.keySet()) {
			Service service = serviceMap.get(serviceKey);
			for (Param param : service.getInputParamSet()) {
				Concept concept = conceptMap.get(param.getThing().getType());
				for (Concept childrenConcept : concept
						.getChildrenConceptsIndex()) {
					childrenConcept.addServiceToIndex(service);
				}
			}
		}
	}

	/**
	 * Parse the challenge String given by client. Also convert the I/O params to concepts
	 * @param paramMap
	 * @param conceptMap
	 * @param thingMap
	 * @param pg
	 * @param url
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private static void parseChallengeDocument(Map<String, Param> paramMap,
			Map<String, Concept> conceptMap, Map<String, Thing> thingMap,
			PlanningGraph pg, String wsdl) throws DocumentException {

		Set<Concept> initPLevel = new HashSet<Concept>();
		Set<Concept> goalSet = new HashSet<Concept>();

		Document document = DocumentHelper.parseText(wsdl);
		Element servicesRoot = document.getRootElement();
		Element semRoot = servicesRoot.element("semExtension");

		for (Iterator i = semRoot.elementIterator(); i.hasNext();) {
			Element semMsgExtEl = (Element) i.next();
			if (semMsgExtEl.getName().equals("semMessageExt")) {
				boolean isRequestParam;
				if (semMsgExtEl.attribute("id").getText().contains(
						"RequestMessage")) {
					isRequestParam = true;
				} else {
					isRequestParam = false;
				}

				for (Iterator j = semMsgExtEl.elementIterator(); j.hasNext();) {
					Element semExtEl = (Element) j.next();
					if (semExtEl.getName().equals("semExt")) {
						Param param = new Param(semExtEl.attribute("id")
								.getText());
						Thing thing = thingMap.get(semExtEl.element(
								"ontologyRef").getText().replaceAll(
								"http://www.ws-challenge.org/wsc08.owl#", ""));

						param.setThing(thing);
						paramMap.put(param.getName(), param);
						if (isRequestParam) {
							for (Concept c : conceptMap.get(thing.getType())
									.getParentConceptsIndex()) {
								initPLevel.add(c);
							}
						} else {
							goalSet.add(conceptMap.get(thing.getType()));
						}
					}
				}
				if (semMsgExtEl.attribute("id").getText().contains(
						"ResponseMessage")) {
					pg.addPLevel(initPLevel);
					pg.setGoalSet(goalSet);
				}

			}
		}
		pg.addALevel(new HashSet<Service>());

	}

	/**
	 * Generate BPEL file from the solution
	 * @param pg
	 * @throws IOException 
	 */
	private static void generateSolution(PlanningGraph pg) throws IOException{
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement( "problemStructure" );
        Element solutions = root.addElement( "solutions" );
        Element solution = solutions.addElement("solution");
        Element sequenceRoot = solution.addElement("sequence");
        for(int i=1; i<pg.getALevels().size(); i++){
        	Set<Service> actionLevel = pg.getALevel(i);
        	Element parallel = sequenceRoot.addElement("parallel");
        	for(Service s : actionLevel){
            	Element serviceDesc = parallel.addElement("serviceDesc");
            	Element abstraction = serviceDesc.addElement("abstraction");            	
            	Element realizations = serviceDesc.addElement("realizations");
            	Element input = abstraction.addElement("input");
            	Element output = abstraction.addElement("output");
        		Element service = realizations.addElement("service");
        		service.addAttribute("name", s.getName());
        		for(Concept c : s.getInputConceptSet()){
        			input.addElement("concept").addAttribute("name", c.getName());
        		}
        		for(Concept c : s.getOutputConceptSet()){
        			output.addElement("concept").addAttribute("name", c.getName());
        		}
        	}
        }
        
        /**
         * write problem.xml to a file
         */
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter("/Users/ericzhao/Desktop/problem.xml"),format);
        writer.write(document);
        writer.close();
        
        /**
         * call BPEL creator to convert problem.xml to BPEL and save it to a file
         */
        BPEL_Creator bpelCreator = new BPEL_Creator("/Users/ericzhao/Desktop/problem.xml");
        bpelCreator.createBPELDocument();
        bpelCreator.saveBPELDocument("/Users/ericzhao/Desktop/Solution.bpel");

	}
	

	/**
	 * This method is called by the WSC-Client to initialize the Composition-System.
	 * @param wsdlServiceDescriptionsURL The URL of the WSDL-Service-Descriptions-document.
	 * @param owlTaxonomyURL The URL of the OWL-taxonomy-document.	
	 */
	@Override
	@WebMethod
	@Oneway
	public void initialize(@WebParam(name = "wsdlServiceDescriptionsURL") String wsdlServiceDescriptionsURL, 
			@WebParam(name = "owlTaxonomyURL") String owlTaxonomyURL,
			@WebParam(name = "wslaSLAgreementsURL") String wslaSLAgreementsURL) {
		
		Date initStart = new Date(); // initialization start checkpoint

		conceptMap = new HashMap<String, Concept>();
		thingMap = new HashMap<String, Thing>();
		serviceMap = new HashMap<String, Service>();
		paramMap = new HashMap<String, Param>();

		try {
			parseTaxonomyDocument(conceptMap, thingMap, owlTaxonomyURL);
			parseServicesDocument(serviceMap, paramMap, conceptMap, thingMap,
					wsdlServiceDescriptionsURL);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		buildInvertedIndex(conceptMap, serviceMap);

		Date initEnd = new Date(); // initialization end checkpoint

		/**
		 * Print out some useful status message
		 */

		System.out.println();
		System.out.println("***************************************");
		System.out.println("*******Finished Initialization*********");
		System.out.println("***************************************");
		System.out.println();
		System.out.println("Initialization Time: "
				+ (initEnd.getTime() - initStart.getTime()) + "ms");
		System.out.println("Concepts size: " + conceptMap.size());
		System.out.println("Things size: " + thingMap.size());
		System.out.println("Param size: " + paramMap.size());
		System.out.println("Services size: " + serviceMap.size());
		System.out.println();
		System.out.println("***************************************");
		System.out.println("*******Ready For Querying**************");
		System.out.println("***************************************");
		System.out.println();

	}

	/**
	 * This method is called by the WSC-Client to start the query.
	 * @param wsdlQuery The WSDL-Query-document.
	 * @param callbackURL The callback-URL of the WSC-Client.
	 */
	@Override
	@WebMethod
	@Oneway
	public void startQuery(@WebParam(name = "wsdlQuery") String wsdlQuery, 
			@WebParam(name = "callbackURL") String callbackURL) {
		
		this.stopped = false;

		Set<Service> invokedServiceSet = new HashSet<Service>();
		Set<Service> currInvokableServiceSet = new HashSet<Service>();
		Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
		Set<Concept> knownConceptSet; // shortcut to pg's current PLevel
		
		
		try {
			parseChallengeDocument(paramMap, conceptMap, thingMap, pg, wsdlQuery);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		/**
		 * print out the query infos
		 */
		System.out.println("*************Query Info Start****************");
		System.out.println("Given Concepts (size: " + pg.getPLevel(0).size() + "): ");
		for(Concept c : pg.getPLevel(0)){
			System.out.print(c + " | ");
		}
		System.out.println();
		System.out.println("Goal Concepts (size: " + pg.getGoalSet().size() + "): ");
		for(Concept c : pg.getGoalSet()){
			System.out.print(c + " | ");
		}
		System.out.println();
		System.out.println("*************Query Info End****************");


		// Do some calculations and create the BPEL-document.
		/**
		 * Flooding Algorithm Implementation
		 */
		Date compStart = new Date(); // start composition checkpoint
		int currentLevel = 0;
		do {
			/**
			 * point knownConceptSet to pg's current PLevel
			 */
			knownConceptSet = pg.getPLevel(currentLevel);
			currInvokableServiceSet = new HashSet<Service>();
			currNonInvokableServiceSet = new HashSet<Service>();
			Set<Concept> pLevel = new HashSet<Concept>();
			/**
			 * fetch all possible candidates
			 */
			for (Concept c : pg.getPLevel(currentLevel)) {
				currInvokableServiceSet.addAll(c.getServicesIndex());
			}
			/**
			 * remove those who have already been invoked
			 */
			currInvokableServiceSet.removeAll(invokedServiceSet);
			/**
			 * remove those whose invocation condition have not been satisfied
			 */
			for (Service s : currInvokableServiceSet) {
				if (!pg.getPLevel(currentLevel).containsAll(
						s.getInputConceptSet())) {
					currNonInvokableServiceSet.add(s);
				}
			}
			currInvokableServiceSet.removeAll(currNonInvokableServiceSet);
			if (currInvokableServiceSet.size() <= 0) {
				break;
			}
			/**
			 * invoked the services
			 */
			invokedServiceSet.addAll(currInvokableServiceSet);
			pg.addALevel(currInvokableServiceSet);
			/**
			 * generate PLevel
			 */
			for (Service s : currInvokableServiceSet) {
				knownConceptSet.addAll(s.getOutputConceptSet());
			}
			pLevel.addAll(knownConceptSet);
			pg.addPLevel(pLevel);
			/**
			 * increase the level and print out newly invoked services
			 */
			currentLevel++;
			System.out.println("\n*********Action Level " + currentLevel
					+ " *******");
			for (Service s : pg.getALevel(currentLevel)) {
				System.out.print(s + "|");
			}
			System.out.println();

		} while (!knownConceptSet.containsAll(pg.getGoalSet())
				& !currInvokableServiceSet.isEmpty());
		Date compEnd = new Date(); // end composition checkpoint		
		
		/**
		 * Print out the composition result
		 */
		if (knownConceptSet.containsAll(pg.getGoalSet())) {
			System.out.println("\n=========Goal Found=========");
			System.out.println("Composition Time: "
					+ (compEnd.getTime() - compStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceSet.size());
			System.out.println("=============================");
			
			try {
				generateSolution(pg);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("\n=========Goal @NOT@ Found=========");
		}

		// Read a test-bpel-document.
		String testBPELResultDocumentPath = "/Users/ericzhao/Desktop/Solution.bpel";
		BufferedReader bpelDocumentReader = null;
		try {
			bpelDocumentReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(testBPELResultDocumentPath)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer bpelDocument = new StringBuffer();
		String line = null;

		try {
			while ((line = bpelDocumentReader.readLine()) != null) {
				bpelDocument.append(line);
				bpelDocument.append("\n");
			}
		} catch (IOException exception) {
			exception.printStackTrace();
			return;
		}

		// Call the callback-interface.
		if(!this.stopped) {
			try {
				CallbackService callbackService = new ClientCallbackInterfaceService().getCallbackServicePort();
				setURL(callbackURL,callbackService);
				callbackService.result(bpelDocument.toString());
			}
			catch(Exception exception) {
				exception.printStackTrace();
			}
		}		
	}

	/**
	 * This method is called by the WSC-Client to stop the composition.
	 * @return "true" if Composition-System stopped successfully otherwise "false".
	 */
	@Override
	@WebMethod
	public boolean stopComposition() {
		System.out.println("Stop composition");
		this.stopped = true;
		
		return true;
	}
	
	/**
	 * A Web Service stub is instantiated with the default URL of the 
	 * related WSDL document. This is a convenient method to change the target URL of the
	 * Web Service stub.
	 * 
	 * @param url The URL of the Web Service.
	 * @param JaxWS The JAX WS stub instance.
	 * @return The actualized Web Service stub instance.
	 */
	public static Object setURL(String url,Object JaxWS) {
		return ((BindingProvider)JaxWS).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,url);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Start the service.
		String serviceURL = "http://localhost:8080/CompositionSystem";
		CompositionSystemImpl compositionSystemImpl = new CompositionSystemImpl();
				
		Endpoint serviceEndpoint = Endpoint.publish(serviceURL, compositionSystemImpl);
		System.out.println("Service started at "+serviceURL);
		
		System.out.println("Press any key to stop the service");
		
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(System.in);
			inputStreamReader.read();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		
		serviceEndpoint.stop();
		System.out.println("Service stopped.");
	}
}
