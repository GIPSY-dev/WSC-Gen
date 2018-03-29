package ca.concordia.cse.gipsy.ws.rest;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jo
 */
@XmlRootElement
public class GeneratorConfiguration implements Serializable {
    public int numberOfConcepts;
    public int numberOfServices;  
    public boolean solvableProblem;                          
    public int[] solutionsList ;         
    
    public GeneratorConfiguration() {
        
    }

    public void setNumberOfConcepts(String valueInput) {
        this.numberOfConcepts = getInt(valueInput, 10000, "Number of concepts");
    }

    public void setNumberOfServices(String valueInput) {
        this.numberOfServices =  getInt(valueInput, 4000, "Number of services");
    }

    public void setSolvableProblem(boolean solvableProblem) {
        this.solvableProblem = solvableProblem;
    }

    public void setSolutionsList(String valueInput) {
        String[] differentDepths = valueInput.split(",");
        
        solutionsList = new int[differentDepths.length];
        
        for (int i = 0; i < differentDepths.length; i++) {
            solutionsList[i] = getInt(differentDepths[i], 10, "Solution list depth");
        }
    }

    public int getNumberOfConcepts() {
        return numberOfConcepts;
    }

    public int getNumberOfServices() {
        return numberOfServices;
    }

    public boolean isSolvableProblem() {
        return solvableProblem;
    }

    public int[] getSolutionsList() {
        return solutionsList;
    }
   
    private int getInt(String value, int defaultVal, String paramName) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            System.out.println("Invalid value for " + paramName + ", taking default value: " + defaultVal);
            return defaultVal;
        }
    }
    
}
