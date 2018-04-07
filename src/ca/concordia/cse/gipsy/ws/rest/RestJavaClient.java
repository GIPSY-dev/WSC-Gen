package ca.concordia.cse.gipsy.ws.rest;

import ca.concordia.cse.gipsy.ws.syslog.LoggerUtility;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import javax.swing.JFileChooser;
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
public class RestJavaClient {
    private Client restClient;
    private String urlRestApi;
    private LoggerUtility logUtility;
   
    
    public RestJavaClient(String urlRestApi) {
        restClient =  ClientBuilder.newClient();
        logUtility = new LoggerUtility();
        this.urlRestApi = urlRestApi;
    }
    
    public void generateFiles(GeneratorConfiguration configs) {
        WebTarget resource = restClient.target(this.urlRestApi + "/gen");
        
         resource.request(MediaType.APPLICATION_JSON)
                 .put(Entity.json(configs));
    }
    
    public void getWSDL() {
        getFile("wsdl");
    }
    
    public void getWSLA() {
        getFile("wsla");
    }
    
    public void getOWL() {
        getFile("owl");
    }
    
    public void getBPEL() {
        getFile("bpel");
    }
    
    public void getLogFile(String fileType, String startTime, String endTime) {
        InputStream is;
        
        if (startTime.isEmpty() || endTime.isEmpty()) {
            is = logUtility.getLog(fileType);
        } else {
            is = logUtility.getLog(fileType, Long.parseLong(startTime), Long.parseLong(endTime));
        }
            
        File whereToSave = askWhereToSave();
            
        if (whereToSave == null) {
            System.out.println("You need to selected a file for where to download the " + fileType + " log file. Aborting.");
        } else {
            try {
                Files.copy(is, whereToSave.toPath());
            } catch (Exception ex) {
                System.out.println("Error saving the log file to the specified path. Error: " + ex.getMessage());
            }   
        }
    }
    
    private void getFile(String fileType) {
        WebTarget resource = restClient.target(this.urlRestApi + "/gen/" + fileType);
        
        Response answer = resource.request(MediaType.TEXT_PLAIN).get();
        
        if (answer.getStatus() == Response.Status.OK.getStatusCode()) {
            InputStream is = answer.readEntity(InputStream.class);
            
            File whereToSave = askWhereToSave();
            
            if (whereToSave == null) {
                System.out.println("You need to selected a file for where to download the " + fileType + " file. Aborting.");
            } else {
                try {
                    Files.copy(is, whereToSave.toPath());
                } catch (Exception ex) {
                    System.out.println("Error saving the file to the specified path. Error: " + ex.getLocalizedMessage());
                }   
            }
        } else {
            System.out.println("Error download the " + fileType + " file. Status code: " + answer.getStatus());
        }
    }
    
    private File askWhereToSave() {
        JFileChooser fileChooser = new JFileChooser();
        
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileChooser.setVisible(false);
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
}