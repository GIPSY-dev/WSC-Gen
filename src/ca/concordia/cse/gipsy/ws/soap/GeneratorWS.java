/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.concordia.cse.gipsy.ws.soap;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author MGobran
 */
@WebService(serviceName = "GeneratorWS")
public class GeneratorWS {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "runGenerator")
    public void runGenerator() throws Exception {
        	Generator g = new Generator();
		
		// need first an output folder.
		g.browseOutputFolder();
                
		// ------------Example----------------------------
		// --------- if files already exist in folder --------
		// user input a name for bpelFileName
		String bpelFileName = "bpelFileName";
		if(g.containsFile(g.getOutputFolder(), bpelFileName) && !g.getOverrideFiles()){
			// if this check is not done, an exception is thrown.
			// you can either handle the exception in the controller or output a response here
			System.out.println("folder already exist...");
		}

		
		g.setDefault();

		// start to generate files and save them in folder.
		g.start();

		if(g.getErrorMessages() != null){
			for(int i=0; i< g.getErrorMessages().size(); i++){
				System.out.println(g.getErrorMessages().get(i));
			}
		}
		
    }
}
