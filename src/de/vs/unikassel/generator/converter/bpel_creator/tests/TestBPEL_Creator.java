package de.vs.unikassel.generator.converter.bpel_creator.tests;

import de.vs.unikassel.generator.converter.bpel_creator.BPEL_Creator;

/**
 * A simple class to de.vs.unikassel.generator.test the "BPEL_Creator"-class.
 * @author Marc Kirchhoff
 *
 */
public class TestBPEL_Creator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		BPEL_Creator bpelCreator = new BPEL_Creator("de\\vs\\unikassel\\generator\\converter\\test_files\\solution_files\\problem.xml");
		BPEL_Creator bpelCreator = new BPEL_Creator("/Users/ericzhao/Desktop/output.xml");
		bpelCreator.createBPELDocument();
		bpelCreator.saveBPELDocument("/Users/ericzhao/Desktop/WSBpel.bpel");
		System.out.println(bpelCreator.toString());
	}

}
