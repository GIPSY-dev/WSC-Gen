
package ca.concordia.cse.gipsy.ws.soap.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.11-b150120.1832
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "GeneratorWSService", targetNamespace = "http://soap.ws.gipsy.cse.concordia.ca/", wsdlLocation = "http://localhost:8080/WSC-Gen/GeneratorWSService?wsdl")
public class GeneratorWSService
    extends Service
{

    private final static URL GENERATORWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException GENERATORWSSERVICE_EXCEPTION;
    private final static QName GENERATORWSSERVICE_QNAME = new QName("http://soap.ws.gipsy.cse.concordia.ca/", "GeneratorWSService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/WSC-Gen/GeneratorWSService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        GENERATORWSSERVICE_WSDL_LOCATION = url;
        GENERATORWSSERVICE_EXCEPTION = e;
    }

    public GeneratorWSService() {
        super(__getWsdlLocation(), GENERATORWSSERVICE_QNAME);
    }

    public GeneratorWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns GeneratorWS
     */
    @WebEndpoint(name = "GeneratorWSPort")
    public GeneratorWS getGeneratorWSPort() {
        return super.getPort(new QName("http://soap.ws.gipsy.cse.concordia.ca/", "GeneratorWSPort"), GeneratorWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GeneratorWS
     */
    @WebEndpoint(name = "GeneratorWSPort")
    public GeneratorWS getGeneratorWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://soap.ws.gipsy.cse.concordia.ca/", "GeneratorWSPort"), GeneratorWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (GENERATORWSSERVICE_EXCEPTION!= null) {
            throw GENERATORWSSERVICE_EXCEPTION;
        }
        return GENERATORWSSERVICE_WSDL_LOCATION;
    }

}
