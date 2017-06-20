package eu.sunfishproject.icsp.proxy.http;


import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.InvalidRequestException;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class HttpClient {


    private WebTarget target;
    private static final Logger log = LogManager.getLogger(HttpClient.class);


    public HttpClient(String targetHost) {


        Client client = ClientBuilder.newClient();
        target = client.target(targetHost);
    }


    public Response get(String path, MultivaluedMap<String, String> headers) throws ICSPException {


        Invocation.Builder builder = getInvocationBuilder(path, headers);

        try {
            javax.ws.rs.core.Response httpResponse = builder.get();
            return httpResponse;
        } catch (Exception e) {
            log.error("Error during http get:", e);
            throw new InvalidRequestException();
        }


    }




    public javax.ws.rs.core.Response post(String path, MultivaluedMap<String, String> headers, byte[] requestBody, String contentType) throws ICSPException {

        Invocation.Builder builder = getInvocationBuilder(path, headers);


        try {
            Entity entity = null;
            if(requestBody == null || requestBody.length == 0 ) {
                entity = Entity.json(null);
            } else {
                entity = Entity.entity(requestBody, contentType);
            }
            javax.ws.rs.core.Response httpResponse = builder.post(entity);
            return httpResponse;
        } catch (Exception e) {
            log.error("Error during http post:", e);
            throw new InvalidRequestException();
        }

    }

    public  Response put() {
        throw new NotImplementedException();
    }

    public Response delete() {
        throw new NotImplementedException();
    }

    public Response patch() {
        throw new NotImplementedException();
    }


    private Invocation.Builder getInvocationBuilder(String path, MultivaluedMap<String, String> headers) {

        Invocation.Builder builder = target.path(path).request();


        if(headers != null) {
            builder.headers(new MultivaluedHashMap<String, Object>(headers));
        }


        return builder;
    }




}
