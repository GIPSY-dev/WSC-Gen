package ca.concordia.cse.gipsy.ws.syslog;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jo
 */
@XmlRootElement
public class LogInput implements Serializable {
    public String message;
    public String fileType;  
    public long timestamp;        
    
    public LogInput() {
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setTimesteamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }
   
    public String getFileType() {
        return fileType;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimeStamp() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }
    
    public String getJSONRepresentation() {
        return "{" +
                "\"message\":\"" + this.message + "\"," +
                "\"fileType\":\"" + this.fileType + "\"," +
                "\"timestamp\":" + this.timestamp +
               "}";
    }
}
