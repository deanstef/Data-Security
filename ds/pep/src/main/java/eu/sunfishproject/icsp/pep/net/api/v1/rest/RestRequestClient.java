package eu.sunfishproject.icsp.pep.net.api.v1.rest;

import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.InvalidRequestException;
import org.sunfish.icsp.common.rest.CommonSetup;
import org.sunfish.icsp.common.rest.model.SunfishRequest;
import org.sunfish.icsp.common.rest.model.SunfishRequestData;
import org.sunfish.icsp.common.rest.pdp.RestPDPClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dziegler on 6/13/2016.
 */
public class RestRequestClient {


    private SunfishRequest request;
    private SunfishRequestData requestData;
    private byte[] requestBody;
    private WebTarget target;
    private static final Logger log = LogManager.getLogger(RestRequestClient.class);


    public RestRequestClient(SunfishRequest request, String targetHost, SunfishRequestData requestData, byte[] requestBody) throws ICSPException {

        this.request = request;
        this.requestData = requestData;
        this.requestBody = requestBody;
        Client client = CommonSetup.createClient();

        String resource = getResource(targetHost);
        target = client.target(resource);
    }


    public javax.ws.rs.core.Response request() throws ICSPException {

        javax.ws.rs.core.Response result = null;

        Invocation.Builder builder = getInvocationBuilder();

        log.error("Issuing Request to {}", target.getUri());


        String method = request.getMethod().toUpperCase();

        try {
            switch (method) {
                case "GET":
                    result = builder.get();
                    break;
                case "HEAD":
                    result = builder.head();
                    break;
                case "POST":
                    result = builder.post(Entity.entity(requestBody, request.getContentType()));
                    break;
                case "PUT":
                    result = builder.put(Entity.entity(requestBody, request.getContentType()));
                    break;
                case "DELETE":
                    result = builder.delete();
                    break;
                default:
                    throw new UnsupportedOperationException();

            }
        } catch (Exception e) {
            log.error("Could not issue request to: {}", target.getUri());
            log.error("Error during HTTP " + method + " :", e);
            throw new InvalidRequestException();
        }

        return result;
    }


    private Invocation.Builder getInvocationBuilder() {


        String queryString = requestData.getQueryString();

        if(queryString != null) {

            queryString = queryString.replaceFirst("\\?", "");

            String[] queryParams = queryString.split("&");
            for (String value : queryParams) {
                String[] queryParam = value.split("=");
                if (queryParam.length == 2) {
                    String paramName = queryParam[0];
                    String paramValue = queryParam[1];
                    target = target.queryParam(paramName, paramValue);
                }
            }
        }


        // issue request
        Invocation.Builder builder = target.path(request.getPath())
                .request(request.getContentType());




        MultivaluedMap<String, Object> headers = requestData.getMultivaluedHeaders();
        if(headers != null) {
            builder.headers(headers);
        }

        return builder;
    }



    private String getResource(String targetHost) throws InvalidRequestException {


        String resource = "";
        if(!targetHost.contains("://")) {
            String protocol = request.getProtocol().toLowerCase();

            if (!protocol.endsWith("://")) {
                protocol += "://";
            }
            resource += protocol + targetHost;

        } else {
            resource = targetHost;
        }


        try {
            URL url = new URL(resource);
            if(url.getPort() == -1) {
                url = UriBuilder.fromUri(url.toURI()).port(request.getPort()).build().toURL();
            }
            return url.toString();
        } catch(Exception e) {
            log.error("Could not create valid url!");
            throw new InvalidRequestException();
        }
    }

}
