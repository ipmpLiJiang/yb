
package cc.mrbird.febs.cn.webxml.sms;

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
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "smsService", targetNamespace = "http://service.core.weikang.com", wsdlLocation = "http://192.168.64.52:8288/sms_service/services/smsService?WSDL")
public class SmsService
    extends Service
{

    private final static URL SMSSERVICE_WSDL_LOCATION;
    private final static WebServiceException SMSSERVICE_EXCEPTION;
    private final static QName SMSSERVICE_QNAME = new QName("http://service.core.weikang.com", "smsService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.64.52:8288/sms_service/services/smsService?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SMSSERVICE_WSDL_LOCATION = url;
        SMSSERVICE_EXCEPTION = e;
    }

    public SmsService() {
        super(__getWsdlLocation(), SMSSERVICE_QNAME);
    }

    public SmsService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SMSSERVICE_QNAME, features);
    }

    public SmsService(URL wsdlLocation) {
        super(wsdlLocation, SMSSERVICE_QNAME);
    }

    public SmsService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SMSSERVICE_QNAME, features);
    }

    public SmsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SmsService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SmsServicePortType
     */
    @WebEndpoint(name = "smsServiceHttpPort")
    public SmsServicePortType getSmsServiceHttpPort() {
        return super.getPort(new QName("http://service.core.weikang.com", "smsServiceHttpPort"), SmsServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SmsServicePortType
     */
    @WebEndpoint(name = "smsServiceHttpPort")
    public SmsServicePortType getSmsServiceHttpPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.core.weikang.com", "smsServiceHttpPort"), SmsServicePortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SMSSERVICE_EXCEPTION!= null) {
            throw SMSSERVICE_EXCEPTION;
        }
        return SMSSERVICE_WSDL_LOCATION;
    }

}
