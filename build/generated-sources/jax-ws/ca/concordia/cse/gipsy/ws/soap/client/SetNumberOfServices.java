
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
 * &lt;complexType name="setNumberOfServices">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numberOfServices" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
