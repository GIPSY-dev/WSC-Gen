package ca.concordia.cse.gipsy.ws.syslog;

import java.io.InputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jo
 */
public class LoggerUtility {
    public static enum LOG_TYPES {
        APPLICATION,
        ERROR,
        EVENTS
    };
    
    final String ENDPOINT_URL = "http://localhost:8080/WSC-Gen/resources/log";
    private Client restClient;
     
    public LoggerUtility() {
        restClient =  ClientBuilder.newClient();
    }
    
    public void log(String message, LOG_TYPES logType) {
        String logTypeStr = getFileNameFromEnum(logType);
        
        LogInput input = new LogInput();
        input.message = message;
        input.fileType = logTypeStr;
        input.setTimeStamp();
        
        WebTarget target = restClient.target(ENDPOINT_URL);
        
        //target.request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }
    
    public InputStream getLog(LOG_TYPES logType) {
        String logTypeStr = getFileNameFromEnum(logType);
        
        WebTarget resource = restClient.target(ENDPOINT_URL + "/" + logTypeStr);
        
        Response answer = resource.request(MediaType.TEXT_PLAIN).get();
        
        if (answer.getStatus() == Response.Status.OK.getStatusCode()) {
            return answer.readEntity(InputStream.class);
        } else {
            System.out.println("Error download the " + logTypeStr + " file. Status code: " + answer.getStatus());
            return null;
        }
    }
    
    public InputStream getLog(String logTypeStr) {
        WebTarget resource = restClient.target(ENDPOINT_URL + "/" + logTypeStr);
        
        Response answer = resource.request(MediaType.TEXT_PLAIN).get();
        
        if (answer.getStatus() == Response.Status.OK.getStatusCode()) {
            return answer.readEntity(InputStream.class);
        } else {
            System.out.println("Error download the " + logTypeStr + " file. Status code: " + answer.getStatus());
            return null;
        }
    }
    
    public InputStream getLog(LOG_TYPES logType, long startTime, long endTime) {
        String logTypeStr = getFileNameFromEnum(logType);
        
        WebTarget resource = restClient.target(ENDPOINT_URL + "/" + logTypeStr + "/" + startTime + "/" + endTime);
        
        Response answer = resource.request(MediaType.TEXT_PLAIN).get();
        
        if (answer.getStatus() == Response.Status.OK.getStatusCode()) {
            return answer.readEntity(InputStream.class);
        } else {
            System.out.println("Error download the " + logTypeStr + " file. Status code: " + answer.getStatus());
            return null;
        }
    }
    
    public InputStream getLog(String logTypeStr, long startTime, long endTime) {
        WebTarget resource = restClient.target(ENDPOINT_URL + "/" + logTypeStr + "/" + startTime + "/" + endTime);
        
        Response answer = resource.request(MediaType.TEXT_PLAIN).get();
        
        if (answer.getStatus() == Response.Status.OK.getStatusCode()) {
            return answer.readEntity(InputStream.class);
        } else {
            System.out.println("Error download the " + logTypeStr + " file. Status code: " + answer.getStatus());
            return null;
        }
    }
     
    private String getFileNameFromEnum(LOG_TYPES logType) {
        switch (logType) {
            case APPLICATION:
                return "application";
            case EVENTS:
                return "events";
            default:
                return "error";
        }
    }
}
