package ca.concordia.cse.gipsy.ws.rest.RestJavaclient;

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
   
    
    public RestJavaClient(String urlRestApi) {
        restClient = ClientBuilder.newClient();
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
    
    private void getFile(String fileType) {
        WebTarget resource = restClient.target(this.urlRestApi + "/" + fileType);
        
        Response answer = resource.request("application/" + fileType)
                .get();
        
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
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
}
