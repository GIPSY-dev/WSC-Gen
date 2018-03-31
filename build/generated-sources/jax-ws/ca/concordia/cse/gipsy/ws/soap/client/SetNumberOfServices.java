
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setNumberOfServices complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setNumberOfServices"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="numberOfServices" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setNumberOfServices", propOrder = {
    "numberOfServices"
})
public class SetNumberOfServices {

    protected int numberOfServices;

    /**
     * Gets the value of the numberOfServices property.
     * 
     */
    public int getNumberOfServices() {
        return numberOfServices;
    }

    /**
     * Sets the value of the numberOfServices property.
     * 
     */
    public void setNumberOfServices(int value) {
        this.numberOfServices = value;
    }

}
