package ca.concordia.cse.gipsy.ws.rest;

import ca.concordia.cse.gipsy.ws.soap.Generator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author Jo
 */
@Path("restGenerator")
public class RestGenerator {
    private Generator instance;
    
    public RestGenerator() {
        instance = new Generator();
        
        try {
            instance.setDefault();   
        } catch (Exception ex) {
            System.out.println("Problem when setting the defaults of the generator. Error: " + ex.getLocalizedMessage());
        }
    }
    
    @PUT
    @Path("gen")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generate(String jsonInput) {
        ObjectMapper ob = new ObjectMapper();
        
        try {
            GeneratorConfiguration config = ob.readValue(jsonInput, GeneratorConfiguration.class);
            
            instance.setNumberOfConcepts(config.getNumberOfConcepts());
            instance.setNumberOfServices(config.getNumberOfServices());
            instance.setSolvableProblem(config.isSolvableProblem());
            
            if (config.isSolvableProblem()) {
                instance.setSolutionsList(config.getSolutionsList());
                instance.setCompleteSolutionDepth();
            }
            
            instance.start();
            
            return Response.status(Response.Status.OK).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    
    @GET
    @Path("wsdl")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWSDL() {
        try {
            this.generateGetResponse(instance.getServiceWSDLFileName());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        
        return Response.status(Response.Status.BAD_GATEWAY).build();
    }
    
    @GET
    @Path("wsla")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWSLA() {
        try {
            this.generateGetResponse(instance.getWSLAFileName());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        
        return Response.status(Response.Status.BAD_GATEWAY).build();
    }
    
    @GET
    @Path("owl")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getOWL() {
        try {
            this.generateGetResponse(instance.getOwlFileName());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        
        return Response.status(Response.Status.BAD_GATEWAY).build();
    }
    
    @GET
    @Path("bpel")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBPEL() {
        try {
            this.generateGetResponse(instance.getBpelFileName());
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        
        return Response.status(Response.Status.BAD_GATEWAY).build();
    }
    
    private Response generateGetResponse(String fileName) {
        File toReturn = new File(fileName);

        ResponseBuilder response = Response.ok((Object) toReturn);
        response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return response.build();
    }
}
