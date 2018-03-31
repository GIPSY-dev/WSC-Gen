
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setWSLAFileName complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setWSLAFileName"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WSLAFileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setWSLAFileName", propOrder = {
    "wslaFileName"
})
public class SetWSLAFileName {

    @XmlElement(name = "WSLAFileName")
    protected String wslaFileName;

    /**
     * Gets the value of the wslaFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWSLAFileName() {
        return wslaFileName;
    }

    /**
     * Sets the value of the wslaFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWSLAFileName(String value) {
        this.wslaFileName = value;
    }

}
