
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setGipsy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setGipsy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="_gipsy" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setGipsy", propOrder = {
    "gipsy"
})
public class SetGipsy {

    @XmlElement(name = "_gipsy")
    protected boolean gipsy;

    /**
     * Gets the value of the gipsy property.
     * 
     */
    public boolean isGipsy() {
        return gipsy;
    }

    /**
     * Sets the value of the gipsy property.
     * 
     */
    public void setGipsy(boolean value) {
        this.gipsy = value;
    }

}
