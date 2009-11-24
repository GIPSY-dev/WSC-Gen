package ca.concordia.pga.algorithm.test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dom4j.DocumentException;

import ca.concordia.pga.algorithm.PGAlgorithm;
import ca.concordia.pga.algorithm.RefinementAlgorithm;
import ca.concordia.pga.algorithm.RemovalAlgorithm;
import ca.concordia.pga.algorithm.RepairAlgorithm;
import ca.concordia.pga.algorithm.utils.DocumentParser;
import ca.concordia.pga.algorithm.utils.IndexBuilder;
import ca.concordia.pga.algorithm.utils.PGValidator;
import ca.concordia.pga.algorithm.utils.RepairingEvaluator;
import ca.concordia.pga.models.*;

/**
 * This class is for testing and experiment purpose
 * 
 * @author Ludeng Zhao(Eric)
 * 
 */
public class TestRepairingMainFinal {

	// change the Prefix URL according your environment
	//static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC2009_Testsets/Testset05/";
	static final String PREFIX_URL = "/Users/ericzhao/Desktop/WSC08_Dataset/Testset01/";
	static final String TAXONOMY_URL = PREFIX_URL + "Taxonomy.owl";
	static final String SERVICES_URL = PREFIX_URL + "Services.wsdl";
	// static final String WSLA_URL = PREFIX_URL +
	// "Servicelevelagreements.wsla";
	static final String CHALLENGE_URL = PREFIX_URL + "Challenge.wsdl";

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

		Set<Service> invokedServiceSet = new HashSet<Service>();
		Set<Service> currInvokableServiceSet = new HashSet<Service>();
		Set<Service> currNonInvokableServiceSet = new HashSet<Service>();
		Set<Concept> knownConceptSet = new HashSet<Concept>(); 

		Set<Concept> givenConceptSet = new HashSet<Concept>();
		Set<Concept> goalConceptSet = new HashSet<Concept>();

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
		 * begin executing algorithm
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
		Date compStart = new Date(); // start composition checkpoint

		boolean goalFound = PGAlgorithm.generatePG(knownConceptSet,
				currInvokableServiceSet, currNonInvokableServiceSet,
				invokedServiceSet, pg);

		Date compEnd = new Date(); // end composition checkpoint

		/**
		 * Print out the composition result
		 */
		if (goalFound) {

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
			 * remove services from serviceMap
			 */
			int originalServiceMapSize;
			int changedServiceMapSize;
			float changedRate;
			originalServiceMapSize = serviceMap.size();
			Set<String> removedServiceKeySet = new HashSet<String>();
			Set<Service> removedServiceSet = new HashSet<Service>();
//			for (String key : serviceMap.keySet()) {
//				if (Math.random() <= 0.10) {
//					removedServiceKeySet.add(key);
//				}
//			}
			
//			PGValidator.debugService(pg, serviceMap, conceptMap, thingMap, paramMap, "serv1056747493");

			
			removedServiceSet.add(serviceMap.get("serv18541048"));
			removedServiceSet.add(serviceMap.get("serv1612205357"));
			removedServiceSet.add(serviceMap.get("serv919521571"));
//	
			serviceMap.remove("serv18541048");
			serviceMap.remove("serv1612205357");
			serviceMap.remove("serv919521571");

			for (String key : removedServiceKeySet) {
				removedServiceSet.add(serviceMap.get(key));
				serviceMap.remove(key);
			}
			

			/**
			 * print out status after removal
			 */
			changedServiceMapSize = serviceMap.size();
			changedRate = (float) (originalServiceMapSize - changedServiceMapSize)
					/ (float) originalServiceMapSize * 100;
			
			System.out.println();
			System.out.println("======================================");
			System.out.println("===========Removing Services==========");
			System.out.println("======================================");
			System.out.println("Original ServiceMap size: "
					+ originalServiceMapSize);
			System.out.println("Updated ServiceMap size: " + changedServiceMapSize);
			System.out.println("Removed Service Size: " + removedServiceSet.size());
			System.out.println("Changed rate: " + changedRate + "%");
			System.out.println("Removed Services: ");
			for(Service s : removedServiceSet){
				System.out.println(s + " | ");
			}
			System.out.println("======================================");
			System.out.println("===========Repairing Start===========");
			System.out.println("======================================");

			
			/**
			 * mark concept that in initial PLevel as given
			 */
			for(Concept c : pg.getPLevel(0)){
				c.setRin(true);
			}
			
			/**
			 * compute services in PG
			 */
			Set<Service> servicesInPG = new HashSet<Service>();
			for(Set<Service> aLevel : pg.getALevels()){
				for(Service s : aLevel){
					servicesInPG.add(s);
				}
			}
			
			/**
			 * compute producedby/usedby for each concept in PG
			 */
			int currentLevel = 0;
			do{
				for(Concept c : pg.getPLevel(currentLevel)){
					//producedby
					for(Service s : pg.getALevel(currentLevel)){
						if(s.getOutputConceptSet().contains(c)){
							c.getProducedByServices().add(s);
						}
					}	
					
					//usedby
					if(currentLevel != pg.getPLevels().size()-1){
						c.getUsedByServices().addAll(c.getServicesIndex());
						c.getUsedByServices().retainAll(servicesInPG);			
					}
				}
				currentLevel++;
			}while(currentLevel < pg.getPLevels().size());
			
			/**
			 * rebuild inverted index
			 */
			for (String key : conceptMap.keySet()) {
				Concept concept = conceptMap.get(key);
				concept.resetServiceIndex();
			}
			IndexBuilder.buildInvertedIndex(conceptMap, serviceMap);
			
			
			/**
			 * compute concepts in PG
			 */
			Set<Concept> conceptsInPG = new HashSet<Concept>();
			conceptsInPG.addAll(pg.getPLevel(0));
			currentLevel = 1;
			do{
				for(Service s : pg.getALevel(currentLevel)){
					conceptsInPG.addAll(s.getOutputConceptSet());
				}
				currentLevel++;
			}while(currentLevel < pg.getALevels().size());
			
			for(String key : conceptMap.keySet()){
				Concept concept = conceptMap.get(key);
				concept.getOriginServiceSet().clear();
			}
			RefinementAlgorithm.refineSolution(pg);

			/**
			 * remove related services in PG
			 */
			Set<Concept> bp = RemovalAlgorithm.removeServcesFromPG(pg, removedServiceSet);
			
			System.out.println("BP: " + bp.size());
			for(Concept c : bp){
				System.out.print(c + " | ");
			}
			
						
			/**
			 * validate PG
			 */
			boolean pgvalid = PGValidator.comboValidate(pg, serviceMap, conceptMap, thingMap, paramMap, givenConceptSet);
			
			if(pgvalid){
				/**
				 * do backward search to remove redundancy (pruning PG)
				 */
				Date refineStart = new Date();
				for(String key : conceptMap.keySet()){
					Concept concept = conceptMap.get(key);
					concept.getOriginServiceSet().clear();
				}
				Vector<Integer> routesCounters = RefinementAlgorithm.refineSolution(pg);
				Date refineEnd = new Date(); // refinement end checkpoint

				/**
				 * printout backward search status
				 */
				System.out.println();
				System.out.println("===================================");
				System.out.println("===========After Pruning===========");
				System.out.println("===================================");

				int invokedServiceCount = 0;
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
				System.out.println("\n=================Status=================");
				System.out.println("Total(including PG) Composition Time: "
						+ (refineEnd.getTime() - refineStart.getTime()) + "ms");
				System.out.println("Execution Length: "
						+ (pg.getALevels().size() - 1));
				System.out.println("Services Invoked: " + invokedServiceCount);
				System.out.println("==================End===================");		
			}else { //if PG is not valid or Goal is not satisfied
		
				pg.getALevel(8).add(serviceMap.get("serv783934155"));
				
				Date repairStart = new Date();
				if(RepairAlgorithm.repair(pg, serviceMap, conceptMap, thingMap, paramMap)){
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
					/**
					 * do backward search to remove redundancy (pruning PG)
					 */
					for(String key : conceptMap.keySet()){
						Concept concept = conceptMap.get(key);
						concept.getOriginServiceSet().clear();
					}
					Vector<Integer> routesCounters = RefinementAlgorithm.refineSolution(pg);
					Date repairEnd = new Date(); // repair end checkpoint

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
					System.out.println("\n=================Status=================");
					System.out.println("Total(Removal + Repair) Composition Time: "
							+ (repairEnd.getTime() - repairStart.getTime()) + "ms");
					System.out.println("Execution Length: "
							+ (pg.getALevels().size() - 1));
					System.out.println("Services Invoked: " + invokedServiceCount);
					System.out.println("==================End===================");	
				}else{
					System.out.println("Repair Failed!");
				}
					
			}


		} else {
			System.out.println("\n=========Goal @NOT@ Found=========");
		}


		

	}

}
