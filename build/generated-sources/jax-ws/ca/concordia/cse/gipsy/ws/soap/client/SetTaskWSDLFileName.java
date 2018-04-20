
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setTaskWSDLFileName complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setTaskWSDLFileName">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="taskWSDLFileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setTaskWSDLFileName", propOrder = {
    "taskWSDLFileName"
})
public class SetTaskWSDLFileName {

    protected String taskWSDLFileName;

    /**
     * Gets the value of the taskWSDLFileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskWSDLFileName() {
        return taskWSDLFileName;
    }

    /**
     * Sets the value of the taskWSDLFileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskWSDLFileName(String value) {
        this.taskWSDLFileName = value;
    }

}
