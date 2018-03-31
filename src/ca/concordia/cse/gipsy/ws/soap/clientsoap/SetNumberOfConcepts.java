
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setNumberOfConcepts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setNumberOfConcepts"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numberOfConcepts" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setNumberOfConcepts", propOrder = {
    "numberOfConcepts"
})
public class SetNumberOfConcepts {

    protected int numberOfConcepts;

    /**
     * Gets the value of the numberOfConcepts property.
     * 
     */
    public int getNumberOfConcepts() {
        return numberOfConcepts;
    }

    /**
     * Sets the value of the numberOfConcepts property.
     * 
     */
    public void setNumberOfConcepts(int value) {
        this.numberOfConcepts = value;
    }

}
