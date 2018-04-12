
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setGenerateIntermediateFiles complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setGenerateIntermediateFiles">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="generateIntermediateFiles" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setGenerateIntermediateFiles", propOrder = {
    "generateIntermediateFiles"
})
public class SetGenerateIntermediateFiles {

    protected boolean generateIntermediateFiles;

    /**
     * Gets the value of the generateIntermediateFiles property.
     * 
     */
    public boolean isGenerateIntermediateFiles() {
        return generateIntermediateFiles;
    }

    /**
     * Sets the value of the generateIntermediateFiles property.
     * 
     */
    public void setGenerateIntermediateFiles(boolean value) {
        this.generateIntermediateFiles = value;
    }

}
