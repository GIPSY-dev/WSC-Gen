
package ca.concordia.cse.gipsy.ws.soap.client;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10-b140803.1500
 * Generated source version: 2.1
 * 
 */
@WebService(name = "GeneratorWS", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface GeneratorWS {


    /**
     * 
     * @return
     *     returns boolean
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "start", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.Start")
    @ResponseWrapper(localName = "startResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.StartResponse")
    public boolean start()
        throws Exception_Exception
    ;

    /**
     * 
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setDefault", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetDefault")
    @ResponseWrapper(localName = "setDefaultResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetDefaultResponse")
    public void setDefault()
        throws Exception_Exception
    ;

    /**
     * 
     * @param fileType
     * @return
     *     returns byte[]
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getFileGenerated", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.GetFileGenerated")
    @ResponseWrapper(localName = "getFileGeneratedResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.GetFileGeneratedResponse")
    public byte[] getFileGenerated(
        @WebParam(name = "fileType", targetNamespace = "")
        String fileType)
        throws Exception_Exception
    ;

    /**
     * 
     * @param numberOfConcepts
     */
    @WebMethod
    @RequestWrapper(localName = "setNumberOfConcepts", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetNumberOfConcepts")
    @ResponseWrapper(localName = "setNumberOfConceptsResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetNumberOfConceptsResponse")
    public void setNumberOfConcepts(
        @WebParam(name = "numberOfConcepts", targetNamespace = "")
        int numberOfConcepts);

    /**
     * 
     * @param numberOfServices
     */
    @WebMethod
    @RequestWrapper(localName = "setNumberOfServices", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetNumberOfServices")
    @ResponseWrapper(localName = "setNumberOfServicesResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetNumberOfServicesResponse")
    public void setNumberOfServices(
        @WebParam(name = "numberOfServices", targetNamespace = "")
        int numberOfServices);

    /**
     * 
     * @param solvableProblem
     */
    @WebMethod
    @RequestWrapper(localName = "setSolvableProblem", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetSolvableProblem")
    @ResponseWrapper(localName = "setSolvableProblemResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetSolvableProblemResponse")
    public void setSolvableProblem(
        @WebParam(name = "solvableProblem", targetNamespace = "")
        boolean solvableProblem);

    /**
     * 
     * @param depthNumbers
     */
    @WebMethod
    @RequestWrapper(localName = "setSolutionsList", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetSolutionsList")
    @ResponseWrapper(localName = "setSolutionsListResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetSolutionsListResponse")
    public void setSolutionsList(
        @WebParam(name = "depthNumbers", targetNamespace = "")
        List<Integer> depthNumbers);

    /**
     * 
     */
    @WebMethod
    @RequestWrapper(localName = "setCompleteSolutionDepth", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetCompleteSolutionDepth")
    @ResponseWrapper(localName = "setCompleteSolutionDepthResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetCompleteSolutionDepthResponse")
    public void setCompleteSolutionDepth();

    /**
     * 
     * @param gipsy
     */
    @WebMethod
    @RequestWrapper(localName = "setGipsy", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetGipsy")
    @ResponseWrapper(localName = "setGipsyResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetGipsyResponse")
    public void setGipsy(
        @WebParam(name = "_gipsy", targetNamespace = "")
        boolean gipsy);

    /**
     * 
     * @param bpelFileName
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setBpelFileName", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetBpelFileName")
    @ResponseWrapper(localName = "setBpelFileNameResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetBpelFileNameResponse")
    public void setBpelFileName(
        @WebParam(name = "bpelFileName", targetNamespace = "")
        String bpelFileName)
        throws Exception_Exception
    ;

    /**
     * 
     * @param owlFileName
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setOwlFileName", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetOwlFileName")
    @ResponseWrapper(localName = "setOwlFileNameResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetOwlFileNameResponse")
    public void setOwlFileName(
        @WebParam(name = "owlFileName", targetNamespace = "")
        String owlFileName)
        throws Exception_Exception
    ;

    /**
     * 
     * @param taskWSDLFileName
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setTaskWSDLFileName", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetTaskWSDLFileName")
    @ResponseWrapper(localName = "setTaskWSDLFileNameResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetTaskWSDLFileNameResponse")
    public void setTaskWSDLFileName(
        @WebParam(name = "taskWSDLFileName", targetNamespace = "")
        String taskWSDLFileName)
        throws Exception_Exception
    ;

    /**
     * 
     * @param wslaFileName
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setWSLAFileName", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetWSLAFileName")
    @ResponseWrapper(localName = "setWSLAFileNameResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetWSLAFileNameResponse")
    public void setWSLAFileName(
        @WebParam(name = "WSLAFileName", targetNamespace = "")
        String wslaFileName)
        throws Exception_Exception
    ;

    /**
     * 
     * @param serviceWSDLFileName
     * @throws Exception_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "setServiceWSDLFileName", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetServiceWSDLFileName")
    @ResponseWrapper(localName = "setServiceWSDLFileNameResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetServiceWSDLFileNameResponse")
    public void setServiceWSDLFileName(
        @WebParam(name = "serviceWSDLFileName", targetNamespace = "")
        String serviceWSDLFileName)
        throws Exception_Exception
    ;

    /**
     * 
     * @param generateIntermediateFiles
     */
    @WebMethod
    @RequestWrapper(localName = "setGenerateIntermediateFiles", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetGenerateIntermediateFiles")
    @ResponseWrapper(localName = "setGenerateIntermediateFilesResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetGenerateIntermediateFilesResponse")
    public void setGenerateIntermediateFiles(
        @WebParam(name = "generateIntermediateFiles", targetNamespace = "")
        boolean generateIntermediateFiles);

    /**
     * 
     * @param ignoreMinimum
     */
    @WebMethod
    @RequestWrapper(localName = "setIgnoreMinimum", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetIgnoreMinimum")
    @ResponseWrapper(localName = "setIgnoreMinimumResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.SetIgnoreMinimumResponse")
    public void setIgnoreMinimum(
        @WebParam(name = "ignoreMinimum", targetNamespace = "")
        boolean ignoreMinimum);

    /**
     * 
     */
    @WebMethod
    @RequestWrapper(localName = "calculateMinNumberConcepts", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.CalculateMinNumberConcepts")
    @ResponseWrapper(localName = "calculateMinNumberConceptsResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.CalculateMinNumberConceptsResponse")
    public void calculateMinNumberConcepts();

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getErrorMessages", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.GetErrorMessages")
    @ResponseWrapper(localName = "getErrorMessagesResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.GetErrorMessagesResponse")
    public String getErrorMessages();

    /**
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "infoButton", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.InfoButton")
    @ResponseWrapper(localName = "infoButtonResponse", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", className = "ca.concordia.cse.gipsy.ws.soap.client.InfoButtonResponse")
    public String infoButton();

}
