package ca.concordia.cse.gipsy.ws.syslog;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
//import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
//@Stateless
@Path("log")
public class RestLogger {
    final String BASE_PATH_LOGS = System.getProperty("user.dir");
    final String ERROR_LOG_FILE_NAME = "errors.log";
    final String APPLICATION_LOG_FILE_NAME = "application.log";
    final String EVENTS_LOG_FILE_NAME = "critical_events.log";
    
    public RestLogger() {
        try {
            java.nio.file.Path logPath = Paths.get(BASE_PATH_LOGS + "/sys_logs");
            
            if (Files.notExists(logPath)) {
                Files.createDirectory(logPath);
            }
            
            if (Files.notExists(Paths.get(logPath.toString() + "/" + ERROR_LOG_FILE_NAME))) {
                Files.createFile(Paths.get(logPath.toString() + "/" + ERROR_LOG_FILE_NAME));
            }
            
            if (Files.notExists(Paths.get(logPath.toString() + "/" + APPLICATION_LOG_FILE_NAME))) {
                Files.createFile(Paths.get(logPath.toString() + "/" + APPLICATION_LOG_FILE_NAME));
            }
            
            if (Files.notExists(Paths.get(logPath.toString() + "/" + EVENTS_LOG_FILE_NAME))) {
                Files.createFile(Paths.get(logPath.toString() + "/" + EVENTS_LOG_FILE_NAME));
            }
        } catch (Exception ex) {
            System.out.println("Exception in RestLogger constructor. Error: " + ex.getMessage());
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void log(String jsonInput) {
        ObjectMapper ob = new ObjectMapper();
        PrintWriter pw = null;
        
        try {
            LogInput input = ob.readValue(jsonInput, LogInput.class);
            
            String fileName = getFileName(input.getFileType());
            
            File whereToSave = new File(BASE_PATH_LOGS + "/sys_logs/" + fileName);
            
            pw = new PrintWriter(new FileOutputStream(whereToSave, true));
            
            pw.append(input.getTimestamp() + " --> " + input.getMessage() + "\r\n");
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }   
             } catch (Exception ex) {
                 System.out.println("Exception when cleaning writer and reader. Error: " + ex.getMessage());
             }
        }
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{fileType}")
    public Response getLogFile(@PathParam("fileType") String fileType) {
        String fileName = "";
        
        try {
            fileName = getFileName(fileType);
            
            if (fileName.isEmpty()) {
                throw new Exception("Invalid file type to get.");
            }
            
            return this.generateGetResponse(fileName); 
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.BAD_GATEWAY).build();
        }
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{fileType}/{startTime}/{endTime}")
    public Response getLogFile(@PathParam("fileType") String fileType, 
            @PathParam("startTime") String startTime, @PathParam("endTime") String endTime) {
        String fileName = "";
        
        try {
            fileName = getFileName(fileType);
            
            if (fileName.isEmpty()) {
                throw new Exception("Invalid file type to get.");
            }
            
            return generateFileByTimestamp(fileName, Long.parseLong(startTime), Long.parseLong(endTime));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.BAD_GATEWAY).build();
        }
    }
    
    private Response generateGetResponse(String fileName) {
        File toReturn = new File(BASE_PATH_LOGS + "/sys_logs/" + fileName);
        
        ResponseBuilder response;
        
        if (toReturn != null) {
            response = Response.ok(toReturn, MediaType.TEXT_PLAIN);
            response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }
        
        return response.build();
    }
    
    private Response generateFileByTimestamp(String fileName, long startTime, long endTime) {
        File toRead = new File(BASE_PATH_LOGS + "/sys_logs/" + fileName);
        File toReturn = new File(BASE_PATH_LOGS + "/sys_logs/temp" + startTime + "_" + endTime + ".log");
        BufferedReader br = null;
        PrintWriter pw = null;
        
        ResponseBuilder response;
        
        if (toRead != null) {
            try {
                br = new BufferedReader(new FileReader(toRead));
                pw = new PrintWriter(new FileOutputStream(toReturn));
                String line = "";
                long timestamp;
                
                while ((line = br.readLine()) != null) {
                   timestamp = Long.parseLong(line.substring(0, line.indexOf(" --> ")));
                   
                   if (timestamp > endTime) {
                       break;
                   } else if (timestamp > startTime) {
                       pw.write(line + "\r\n");
                   }
                }

                br.close();
                pw.close();
                
                response = Response.ok(toReturn, MediaType.TEXT_PLAIN);
                response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");   
            } catch (Exception ex) {
                System.out.println("Error in generating file based on timestamp. Error: " + ex.getMessage());
                response = Response.status(Response.Status.BAD_REQUEST);
            } finally {
                try {
                   if (br != null) {
                       br.close();
                   }

                   if (pw != null) {
                       pw.close();
                   }   
                } catch (Exception ex) {
                    System.out.println("Exception when cleaning writer and reader. Error: " + ex.getMessage());
                }
            }
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }
        
        return response.build();
    }
    
    private String getFileName(String fileType) {
        String fileName = null;
        
        switch(fileType.toLowerCase()) {
            case "application":
                fileName = APPLICATION_LOG_FILE_NAME;
                break;
            case "error":
                fileName = ERROR_LOG_FILE_NAME;
                break;
            case "events":
                fileName = EVENTS_LOG_FILE_NAME;
                break;
        }
        
        return fileName;
    }

}