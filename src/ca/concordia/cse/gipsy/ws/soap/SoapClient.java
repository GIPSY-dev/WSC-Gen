/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.concordia.cse.gipsy.ws.soap;

import ca.concordia.cse.gipsy.ws.soap.client.Exception_Exception;
import ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Jo
 */
public class SoapClient {
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/WSC-Gen/GeneratorWSService.wsdl")
    private GeneratorWSService service;

    public void calculateMinNumberConcepts() {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.calculateMinNumberConcepts();
    }

    public String getErrorMessages() {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        return port.getErrorMessages();
    }

    public byte[] getFileGenerated(java.lang.String fileType) throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        return port.getFileGenerated(fileType);
    }

    public String infoButton() {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        return port.infoButton();
    }

    public void setBpelFileName(java.lang.String bpelFileName) throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setBpelFileName(bpelFileName);
    }

    public void setCompleteSolutionDepth() {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setCompleteSolutionDepth();
    }

    public void setDefault() throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setDefault();
    }

    public void setGenerateIntermediateFiles(boolean generateIntermediateFiles) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setGenerateIntermediateFiles(generateIntermediateFiles);
    }

    public void setGipsy(boolean gipsy) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setGipsy(gipsy);
    }

    public void setIgnoreMinimum(boolean ignoreMinimum) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setIgnoreMinimum(ignoreMinimum);
    }

    public void setNumberOfConcepts(int numberOfConcepts) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setNumberOfConcepts(numberOfConcepts);
    }

    public void setNumberOfServices(int numberOfServices) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setNumberOfServices(numberOfServices);
    }

    public void setOwlFileName(java.lang.String owlFileName) throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setOwlFileName(owlFileName);
    }

    public void setServiceWSDLFileName(java.lang.String serviceWSDLFileName) throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setServiceWSDLFileName(serviceWSDLFileName);
    }

    public void setSolutionsList(java.util.List<java.lang.Integer> depthNumbers) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setSolutionsList(depthNumbers);
    }

    public void setSolvableProblem(boolean solvableProblem) {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setSolvableProblem(solvableProblem);
    }

    public void setTaskWSDLFileName(java.lang.String taskWSDLFileName) throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setTaskWSDLFileName(taskWSDLFileName);
    }

    public void setWSLAFileName(java.lang.String wslaFileName) throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        port.setWSLAFileName(wslaFileName);
    }

    public boolean start() throws Exception_Exception {
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService service = new ca.concordia.cse.gipsy.ws.soap.client.GeneratorWSService();
        ca.concordia.cse.gipsy.ws.soap.client.GeneratorWS port = service.getGeneratorWSPort();
        return port.start();
    }
}
