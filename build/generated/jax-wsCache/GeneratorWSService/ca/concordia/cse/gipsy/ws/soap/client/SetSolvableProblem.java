
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setSolvableProblem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setSolvableProblem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="solvableProblem" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setSolvableProblem", propOrder = {
    "solvableProblem"
})
public class SetSolvableProblem {

    protected boolean solvableProblem;

    /**
     * Gets the value of the solvableProblem property.
     * 
     */
    public boolean isSolvableProblem() {
        return solvableProblem;
    }

    /**
     * Sets the value of the solvableProblem property.
     * 
     */
    public void setSolvableProblem(boolean value) {
        this.solvableProblem = value;
    }

}
