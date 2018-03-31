
package ca.concordia.cse.gipsy.ws.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setIgnoreMinimum complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setIgnoreMinimum"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ignoreMinimum" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setIgnoreMinimum", propOrder = {
    "ignoreMinimum"
})
public class SetIgnoreMinimum {

    protected boolean ignoreMinimum;

    /**
     * Gets the value of the ignoreMinimum property.
     * 
     */
    public boolean isIgnoreMinimum() {
        return ignoreMinimum;
    }

    /**
     * Sets the value of the ignoreMinimum property.
     * 
     */
    public void setIgnoreMinimum(boolean value) {
        this.ignoreMinimum = value;
    }

}
