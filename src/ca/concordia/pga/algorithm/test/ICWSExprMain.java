package ca.concordia.pga.algorithm.test;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.DocumentException;

import ca.concordia.pga.algorithm.PGAlgorithm;
import ca.concordia.pga.algorithm.BackwardSearchAlgorithm;
import ca.concordia.pga.algorithm.RepairAlgorithm;
import ca.concordia.pga.algorithm.utils.DocumentParser;
import ca.concordia.pga.algorithm.utils.IndexBuilder;
import ca.concordia.pga.algorithm.utils.PGValidator;
import ca.concordia.pga.algorithm.utils.PlanStabilityEvaluator;
import ca.concordia.pga.models.Concept;
import ca.concordia.pga.models.Param;
import ca.concordia.pga.models.PlanningGraph;
import ca.concordia.pga.models.Service;
import ca.concordia.pga.models.Thing;

public class ICWSExprMain {
	
	// change the Prefix URL according your environment
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC/WSC08_Dataset/Testset01/";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	// static final String WSLA_URL = PREFIX_URL + "Servicelevelagreements.wsla";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";
	
	
	private static void calculateCommonOutputs(PlanningGraph leanSolutionPG){
		
		int currentLevel = leanSolutionPG.getALevels().size() - 1;
		do{
			if(currentLevel == leanSolutionPG.getALevels().size() - 1){
				for(Service s : leanSolutionPG.getALevel(currentLevel)){
					s.getCommonOutputs().addAll(s.getOutputConceptSet());
					s.getCommonOutputs().retainAll(leanSolutionPG.getGoalSet());
				}
			}else{
				Set<Concept> commonOutputs = new HashSet<Concept>();
				Set<Service> allServicesAtHigherLevel = leanSolutionPG.getAllServciesAtHigerLevel(currentLevel);
				for(Service s : allServicesAtHigherLevel){
					commonOutputs.addAll(s.getInputConceptSet());
				}
				commonOutputs.addAll(leanSolutionPG.getGoalSet());
				for(Service s : leanSolutionPG.getALevel(currentLevel)){

					s.getCommonOutputs().addAll(s.getOutputConceptSet());
					s.getCommonOutputs().retainAll(commonOutputs);
				}
			}
			
			currentLevel--;
		}while(currentLevel > 0);
		
	}
	
	/**
	 * return the action level of given s
	 * @param s
	 * @param pg
	 * @return
	 */
	private static Set<Service> getServiceLevel(Service s, PlanningGraph pg){
		
		for(Set<Service> aLevel : pg.getALevels()){
			if(aLevel.contains(s)){
				return aLevel;
			}
		}
		return null;
	}
	
	/**
	 * calculate equivalent service set for given services 
	 * @param services
	 * @param pg
	 */
	private static void calculateBackups(Set<Service> services, PlanningGraph pg){
		for(Service s : services){
			Set<Service> aLevel = getServiceLevel(s,pg);
			for(Service candidate : aLevel){
				Set<Concept> outputs = new HashSet<Concept>();
				outputs.addAll(s.getOutputConceptSet());
				outputs.retainAll(candidate.getOutputConceptSet());
				if(s.getInputConceptSet().equals(candidate.getInputConceptSet())
					& candidate.getOutputConceptSet().containsAll(s.getCommonOutputs())
					& !s.equals(candidate)){
					s.getBackupServiceSet().add(candidate);
				}
			}	
			System.out.println("service " + s + "'s backups:");
			for(Service b : s.getBackupServiceSet()){
				System.out.println(b);
			}
			System.out.println("---------------------------------------\n");
		}
	}

	/**
	 * @param args
	 * @throws DocumentException
	 */
	public static void main(String[] args) {
		PlanningGraph pg = new PlanningGraph();

		Map<String, Concept> conceptMap = new HashMap<String, Concept>();
		Map<String, Thing> thingMap = new HashMap<String, Thing>();
		Map<String, Service> serviceMap = new HashMap<String, Service>();
		Map<String, Param> paramMap = new HashMap<String, Param>();

		Map<String, Service> serviceMapReserved = new HashMap<String, Service>();

		Set<Concept> givenConceptSet = new HashSet<Concept>();
		Set<Concept> goalConceptSet = new HashSet<Concept>();
		
//		Set<PlanningGraph> leanSolutions = new HashSet<PlanningGraph>();
		List<PlanningGraph> leanSolutions = new LinkedList<PlanningGraph>();
		
		Date initStart = new Date();
		try {
			DocumentParser.parseTaxonomyDocument(conceptMap, thingMap, TAXONOMY_URL);
			DocumentParser.parseServicesDocument(serviceMap, paramMap, conceptMap, thingMap,
					SERVICES_URL);
			// parseWSLADocument(serviceMap, WSLA_URL);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		Date initEnd = new Date();
		
		System.out.println("Initializing Time "
				+ (initEnd.getTime() - initStart.getTime()));
		System.out.println("Concepts size " + conceptMap.size());
		System.out.println("Things size " + thingMap.size());
		System.out.println("Param size " + paramMap.size());
		System.out.println("Services size " + serviceMap.size());
		
		
		/**
		 * begin parsing process
		 */
		try {
			DocumentParser.parseChallengeDocument(paramMap, conceptMap, thingMap, pg,
					CHALLENGE_URL);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("Given Concepts: ");
		for (Concept c : pg.getPLevel(0)) {
			givenConceptSet.add(c);
			System.out.print(c + " | ");
		}
		pg.getGivenConceptSet().addAll(pg.getPLevel(0));
		System.out.println();
		System.out.println("Goal Concepts: ");
		for (Concept c : pg.getGoalSet()) {
			goalConceptSet.add(c);
			System.out.print(c + " | ");
		}
		System.out.println();
		
		
		/**
		 * PG Algorithm Implementation
		 */
		
		System.out.println("============================================");
		System.out.println("============ ICWS Repairing Expr ===========");				
		System.out.println("============================================");
		
		serviceMapReserved.putAll(serviceMap);
		
		Set<Service> invokedServiceSet = new HashSet<Service>();
		Set<Service> currInvokableServiceSet = new HashSet<Service>();
		Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
		Set<Concept> knownConceptSet = new HashSet<Concept>(); 
		
		Date compStart = new Date(); // start composition checkpoint
		boolean goalFound = PGAlgorithm.generatePG(knownConceptSet,
				currInvokableServiceSet, currNonInvokableServiceSet,
				invokedServiceSet, pg);
		Date compEnd = new Date(); // end composition checkpoint
		
		if(goalFound) {

			/**
			 * printout PG status (before pruning)
			 */
			System.out.println("\n=========Goal Found=========");
			System.out.println("PG Composition Time: "
					+ (compEnd.getTime() - compStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceSet.size());
			System.out.println("=============================");


			/**
			 * validate PG
			 */
			PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
			
			/**
			 * reserve full PG status
			 */
			PlanningGraph fullPG = pg.clone();
			
			/**
			 * prune PG
			 */
			Vector<Integer> routesCounters = BackwardSearchAlgorithm.extractSolution(pg);
			
			/**
			 * printout backward search status
			 */
			Date refineEnd = new Date(); //refinement end checkpoint

			System.out.println();
			System.out.println("===================================");
			System.out.println("===========After Pruning===========");
			System.out.println("===================================");

			int invokedServiceCount = 0;
			for(int i=1; i<pg.getALevels().size(); i++){
				System.out.println("\n*********Action Level " + i
						+ " (alternative routes:" 
						+ routesCounters.get(routesCounters.size() - i) + ") *******");
				for (Service s : pg.getALevel(i)) {
					System.out.println(s);
					invokedServiceCount++;
				}
			}
			System.out.println("\n=================Status=================");
			System.out.println("Total(including PG) Composition Time: "
					+ (refineEnd.getTime() - compStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (pg.getALevels().size() - 1));
			System.out.println("Services Invoked: " + invokedServiceCount);			
			System.out.println("==================End===================");

			/**
			 * reserve lean solution status
			 */
			PlanningGraph leanSolutionPG = pg.clone();
			leanSolutions.add(leanSolutionPG);
			
			/**
			 * mark solution services in PG
			 */
			for(Service s : leanSolutionPG.getAllServices()){
				s.setSolutionService(true);
			}
			
			/**
			 * calculate backups for each solution services
			 */
			calculateCommonOutputs(leanSolutionPG);
			calculateBackups(leanSolutionPG.getAllServices(),fullPG);
			
			pg = fullPG;

			/**
			 * if all backup services in any removed services' backup list are removed, 
			 * then add all solution services as well as their backups
			 * to removed services list 
			 */
			
		}
		
		serviceMap.clear();
		serviceMap.putAll(serviceMapReserved);
		
		
		System.out.println();
		System.out.println("***************************************************");
		System.out.println("Number of lean solutions indexed: " + leanSolutions.size());
		System.out.println("***************************************************");
		
		/**
		 * Sending Removal Query
		 */
		
		
		Set<Service> candidates = new HashSet<Service>();
		Set<String> removedServiceKeySet = new HashSet<String>();
		Set<Service> removedServices = new HashSet<Service>();

		candidates.add(serviceMap.get("serv1056747493"));
		candidates.add(serviceMap.get("serv1126179726"));
		candidates.add(serviceMap.get("serv1195611959"));//cause failed
		candidates.add(serviceMap.get("serv502928173"));
		candidates.add(serviceMap.get("serv2096592482"));//cause failed
		candidates.add(serviceMap.get("serv18541048"));
		candidates.add(serviceMap.get("serv87973281"));
		candidates.add(serviceMap.get("serv850089338"));//cause failed
		candidates.add(serviceMap.get("serv1612205357"));
		candidates.add(serviceMap.get("serv919521571"));
		

		do{
			for(Service s : candidates){
				if (Math.random() <= 0.10) {
					removedServiceKeySet.add(s.getName());
					break;
				}		
			}
		}while(removedServiceKeySet.size() < 8);



		for (String key : removedServiceKeySet) {
			removedServices.add(serviceMap.get(key));
			serviceMap.remove(key);
		}
		
		
		TestSERARepairingMain.commonRemovedServices.addAll(removedServices);
		TestReplanningMain.commonRemovedServices.addAll(removedServices);
		
//		removedServices.add(serviceMap.get("serv157405514"));//break solution 1
//		removedServices.add(serviceMap.get("serv1612205357"));
//		removedServices.add(serviceMap.get("serv2103146582"));//break solution 2
//		removedServices.add(serviceMap.get("serv1197250503"));//break solution 3
//		removedServices.add(serviceMap.get("serv1889934289"));
//		removedServices.add(serviceMap.get("serv1762539517"));//break solution 4

		
		System.out.println("=================================");
		System.out.println("============ Removing ===========");				
		System.out.println("=================================");
		System.out.println("Removed Service: (" + removedServices.size() +")");
		for(Service s : removedServices){
			serviceMap.remove(s.toString());
			System.out.println(s);
		}
		
		/**
		 * reset inverted index
		 */
		for(String key : conceptMap.keySet()){
			Concept concept = conceptMap.get(key);
			concept.resetServiceIndex();
		}
		IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
		
		/**
		 * preserve all service as well as their backups in the lean solution 
		 */
		Set<Service> leanSolutionServices = leanSolutions.get(0).getAllServicesAndTheirBackups();

		/**
		 * checking if solution break
		 */
		Date fixStart = new Date();
		PlanningGraph fixedPG = null;
		for(PlanningGraph g : leanSolutions){
			Set<Service> brokenServices = new HashSet<Service>();
			brokenServices.addAll(g.getAllServices());
			brokenServices.retainAll(removedServices);
			boolean fixed = true;
			for(Service s : brokenServices){
				Set<Service> backups = s.getBackupServiceSet();
				backups.removeAll(removedServices);
				if(!backups.isEmpty()){
					Service backup = backups.iterator().next();
					backup.getBackupServiceSet().addAll(s.getBackupServiceSet());
					backup.getBackupServiceSet().remove(backup);
					g.replaceService(s, backup);
					
				}else{
					fixed = false;
					System.out.println("service " + s + " doesn't has available backups,\njump to next solution");
					break;
				}
			}
			if(fixed){
				fixedPG = g;
				break;
			}
		}
		Date fixEnd = new Date();

		if(fixedPG != null){
			System.out.println("=================================");
			System.out.println("============ Fixed PG ===========");				
			System.out.println("=================================");
			int currentLevel = 1;
			while(currentLevel < fixedPG.getALevels().size()){
				Set<Service> aLevel = fixedPG.getALevel(currentLevel);
				System.out.println("=========== Action Level " + currentLevel + " ===========");
				for(Service s : aLevel){
					System.out.println(s);
				}
				System.out.println();
				
				currentLevel++;
			}
			
			/**
			 * printout PG status (before pruning)
			 */
			System.out.println("\n=========Goal Found=========");
			System.out.println("PG Repairing Time: "
					+ (fixEnd.getTime() - fixStart.getTime()) + "ms");
			System.out.println("Execution Length: "
					+ (fixedPG.getALevels().size() - 1));
			System.out.println("Services Invoked: " + fixedPG.getAllServices().size());
			System.out.println("=============================");
		}else{
			System.out.println("No backup found, going to repairing mode!");
			
			PlanningGraph oldpg = leanSolutions.get(0).clone();

			leanSolutionServices.addAll(removedServices);
			pg.removeServices(leanSolutionServices);
			for(Service s : leanSolutionServices){
				serviceMap.remove(s.toString());
				System.out.println(s + " removed!");
			}
			/**
			 * reset inverted index
			 */
			for(String key : conceptMap.keySet()){
				Concept concept = conceptMap.get(key);
				concept.resetServiceIndex();
			}
			IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
			
			/**
			 * validate PG
			 */
			PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
			
			Date repairStart = new Date();
			
//			if(RepairAlgorithm.repair(pg, serviceMap, conceptMap, thingMap, paramMap)){
			if(RepairAlgorithm.repairICWS(pg, serviceMap, conceptMap, thingMap, paramMap)){
				System.out.println("Repair Succeed!");

				System.out.println();
				System.out.println("===================================");
				System.out.println("===========Repair Result===========");
				System.out.println("=========(Before Refinement)=======");					
				System.out.println("===================================");
				int invokedServiceCount = 0;
				for (int i = 1; i < pg.getALevels().size(); i++) {
					System.out.println("\n*********Action Level " + i);
					for (Service s : pg.getALevel(i)) {
						System.out.println(s);
						invokedServiceCount++;
					}
				}
				Date repairEnd = new Date(); // refine end checkpoint
				System.out.println("\n=================Status=================");
				System.out.println("Repair Time: "
						+ (repairEnd.getTime() - repairStart.getTime()) + "ms");
				System.out.println("Execution Length: "
						+ (pg.getALevels().size() - 1));
				System.out.println("Services Invoked: " + invokedServiceCount);
				System.out.println("==================End===================");	

				/**
				 * do backward search to remove redundancy (pruning PG)
				 */
				for(String key : conceptMap.keySet()){
					Concept concept = conceptMap.get(key);
					concept.getOriginServiceSet().clear();
				}
				Vector<Integer> routesCounters = BackwardSearchAlgorithm.extractSolution(pg);
				Date refineEnd = new Date(); // refine end checkpoint

				
				/**
				 * printout backward search status
				 */
				System.out.println();
				System.out.println("===================================");
				System.out.println("===========Repair Result===========");
				System.out.println("=========(After Refinement)========");					
				System.out.println("===================================");

				invokedServiceCount = 0;
				for (int i = 1; i < pg.getALevels().size(); i++) {
					System.out.println("\n*********Action Level " + i
							+ " (alternative routes:"
							+ routesCounters.get(routesCounters.size() - i)
							+ ") *******");
					for (Service s : pg.getALevel(i)) {
						System.out.println(s);
						invokedServiceCount++;
					}
				}
				
				/**
				 * compute plan distance
				 */
				int planDistance;
				planDistance = PlanStabilityEvaluator.evaluate(oldpg, pg);
				
				System.out.println("\n=================Status=================");
				System.out.println("Total(Repair + Refinement) Composition Time: "
						+ (refineEnd.getTime() - repairStart.getTime()) + "ms");
				System.out.println("Execution Length: "
						+ (pg.getALevels().size() - 1));
				System.out.println("Services Invoked: " + invokedServiceCount);
				System.out.println("Plan Distance: " + planDistance);					
				System.out.println("==================End===================");	
				
				PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
				

			}else{
				Date repairFailed = new Date();
				System.out.println("Repair Failed!");
				System.out.println("Repair Failed Time: "
						+ (repairFailed.getTime() - repairStart.getTime()) + "ms");					
			}
		}
		

		
		
//		System.out.println("===========================================");
//		System.out.println("============ non-indexed ICWS Repairing Expr ===========");				
//		System.out.println("===========================================");
//		TestICWSRepairingAlgorithmMain.main(args);
		
		System.out.println("===========================================");
		System.out.println("============ SERA Repairing Expr ===========");				
		System.out.println("===========================================");
		TestSERARepairingMain.main(args);
		
		System.out.println("========================================");
		System.out.println("============ Replanning Expr ===========");				
		System.out.println("========================================");
		TestReplanningMain.main(args);
		
		
		

	}//end main method
}//end class
