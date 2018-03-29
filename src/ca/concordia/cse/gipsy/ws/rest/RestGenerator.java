package ca.concordia.cse.gipsy.ws.rest;

import ca.concordia.cse.gipsy.ws.soap.Generator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author Jo
 */
@Stateless
@Path("restGenerator")
public class RestGenerator {
    private Generator instance;
    
    public RestGenerator() {
        instance = new Generator();
        
        try {
            instance.setDefault();   
        } catch (Exception ex) {
            System.out.println("Problem when setting the defaults of the generator. Error: " + ex.getMessage());
        }
    }
    
    @PUT
    @Path("gen")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generate(String jsonInput) {
        ObjectMapper ob = new ObjectMapper();
        
        try {
            GeneratorConfiguration config = ob.readValue(jsonInput, GeneratorConfiguration.class);
            
            //instance.setNumberOfConcepts(config.getNumberOfConcepts());
            //instance.setNumberOfServices(config.getNumberOfServices());
            //instance.setSolvableProblem(config.isSolvableProblem());
            
//            if (config.isSolvableProblem()) {
//                instance.setSolutionsList(config.getSolutionsList());
//                instance.setCompleteSolutionDepth();
//            }
            
            instance.start();
            
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("gen/{fileType}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getFileGenerated(@PathParam("fileType") String fileType) {
        String fileName = "";
        
        try {
            switch(fileType.toLowerCase()) {
                case "wsdl":
                    fileName = instance.getServiceWSDLFileName();
                    break;
                case "owl":
                    fileName = instance.getOwlFileName();
                    break;
                case "wsla":
                    fileName = instance.getWSLAFileName();
                    break;
                case "bpel":
                    fileName = instance.getBpelFileName();
                    break;
            }
            
            if (fileName.isEmpty()) {
                throw new Exception("Invalid file type to get.");
            }
            
            return this.generateGetResponse(fileName); 
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.BAD_GATEWAY).build();
        }
    }
        
    private Response generateGetResponse(String fileName) {
        File toReturn = instance.getFile(fileName);
        
        ResponseBuilder response;
        
        if (toReturn != null) {
            response = Response.ok(toReturn, MediaType.TEXT_PLAIN);
            response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        } else {
            response = Response.status(Response.Status.BAD_GATEWAY);
        }
        
        return response.build();
    }
}