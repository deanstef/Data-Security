package eu.sunfishproject.icsp.proxy.interceptor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sunfishproject.icsp.proxy.http.HttpClient;
import eu.sunfishproject.icsp.proxy.http.HttpRequest;
import javax.ws.rs.core.Response;

import eu.sunfishproject.icsp.proxy.http.HttpResponse;
import eu.sunfishproject.icsp.proxy.util.Constants;
import eu.sunfishproject.icsp.proxy.util.ProxyConfig;
import org.sunfish.icsp.common.exceptions.ICSPException;
import org.sunfish.icsp.common.exceptions.InvalidRequestException;
import org.sunfish.icsp.common.rest.SunfishServices;
import org.sunfish.icsp.common.rest.model.*;
import org.sunfish.icsp.common.util.CommonUtil;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SunfishRequestInterceptor  extends RequestInterceptor {

    private InputStream targetInputStream;
    private OutputStream targetOutputStream;



    public SunfishRequestInterceptor(InputStream clientInputStream, OutputStream clientOutputStream) {
        super(clientInputStream, clientOutputStream);
    }

    @Override
    public  byte[] generateResponse(InputStream inputStream) throws IOException, InterruptedException, IllegalArgumentException {


        HttpRequest request = new HttpRequest(inputStream);

//        System.out.println("Generating response for: " + request.getRequestURI() + " (" + System.currentTimeMillis()+")");


        HttpResponse response = null;

        try {
            switch (request.getHttpMethod()) {
                case GET:
                case POST:
                case PUT:
                case DELETE:
                case HEAD:
                case OPTIONS:
                    response = postToPEP(request);
                    break;
                default:
                    throw new IOException("Cannot handle!");
            }
        } catch(ICSPException e){
            throw new IOException("Internal Server error: " + e.getStatus());
        }


        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        response.writeTo(byteOutputStream);

        byte[] responseBytes = byteOutputStream.toByteArray();

//        System.out.println("Response generated  for: " + request.getRequestURI() + " (" + System.currentTimeMillis()+")");


        return responseBytes;

    }


    @Override
    protected void onFinish() {

        try {
            if(targetInputStream!=null) {
                targetInputStream.close();
            }
        } catch (IOException e) {
        }

        try {
            if(targetOutputStream != null) {
                targetOutputStream.close();
            }
        } catch (IOException e) {
        }
    }



    private HttpResponse postToPEP(HttpRequest request) throws ICSPException {

        MultivaluedMap<String, String> headers = createSunfishHeaders(request);

        HttpClient client = new HttpClient(ProxyConfig.getInstance().getPEPUrl());


        String contentType = null;
        if(request.getBody() != null && request.getBody().length != 0) {
            contentType = request.getHeaders().get("Content-Type");
        }


        Response response = client.post(SunfishServices.PEP_PATH_APP_REQUEST, headers, request.getBody(), contentType);


        return createHttpResponse(response);


    }

    private HttpResponse createHttpResponse(Response response) {

        HttpResponse httpResponse = new HttpResponse();

        httpResponse.setStatusCode(response.getStatus());
        httpResponse.setHttpVersion("HTTP/1.1");
        httpResponse.setReasonPhrase(response.getStatusInfo().getReasonPhrase());

        httpResponse.setHeaders(CommonUtil.convert(response.getHeaders()));
        httpResponse.setBody(response.readEntity(byte[].class));

        return httpResponse;
    }



    private MultivaluedMap<String, String> createSunfishHeaders(HttpRequest request) throws ICSPException {


        URI requestURI = request.requestURI();

        String host = requestURI.getHost();
        int port = requestURI.getPort();
        String path = requestURI.getPath();
        String query = requestURI.getQuery();
        String scheme = requestURI.getScheme();
        String method = request.getHttpMethod().toString();
        Map<String, String> headers = request.getHeaders();


        if(host == null) {
            host = this.targetHost == null ? request.getHeaders().get("Host") : this.targetHost;
        }

        if(port == -1) {
            port = isSecure ? 443 : 80;
        }

        if(scheme == null) {
            scheme = isSecure ? "https://" : "http://";
        }

        MultivaluedMap<String, String> sunfishHeaders = new MultivaluedHashMap<>();


        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            SunfishIssuer sunfishIssuer = createSunfishIssuer();
            SunfishService sunfishService = createSunfishService(host);
            SunfishRequest sunfishRequest = createSunfishRequest(method, port, path, scheme);
            SunfishRequestParameters sunfishRequestParameters = createSunfishRequestParameters();
            SunfishRequestData sunfishRequestData = createSunfishRequestData(headers, query);
            SunfishSignature sunfishSignature = createSunfishSignature();


            String headerSunfishIssuer = mapper.writeValueAsString(sunfishIssuer);
            String headerSunfishService = mapper.writeValueAsString(sunfishService);
            String headerSunfishRequest = mapper.writeValueAsString(sunfishRequest);
            String headerSunfishRequestParameters = mapper.writeValueAsString(sunfishRequestParameters);
            String headerSunfishRequestData = sunfishRequestData.toJson();
            String headerSunfishSignature = mapper.writeValueAsString(sunfishSignature);


            sunfishHeaders.putSingle(SunfishServices.HEADER_ISSUER, headerSunfishIssuer);
            sunfishHeaders.putSingle(SunfishServices.HEADER_SERVICE, headerSunfishService);
            sunfishHeaders.putSingle(SunfishServices.HEADER_REQUEST, headerSunfishRequest);
            sunfishHeaders.putSingle(SunfishServices.HEADER_REQUEST_PARAMETERS, headerSunfishRequestParameters);
            sunfishHeaders.putSingle(SunfishServices.HEADER_REQUEST_DATA, headerSunfishRequestData);
            sunfishHeaders.putSingle(SunfishServices.HEADER_SIGNATURE, headerSunfishSignature);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InvalidRequestException();
        }

        return sunfishHeaders;

    }


    private SunfishIssuer createSunfishIssuer() {

        SunfishIssuer sunfishIssuer = new SunfishIssuer();

        sunfishIssuer.setMethod("jwt");
        sunfishIssuer.setToken("eyJpc3MiOiJzZmExNzI4MyIsInN1YiI6InNmYTE3MjgzIiwiYXVkIjoic2ZlOTgyIiwiaWF0IjoxNDY3OTg1OTY4LCJleHAiOjE0Njc5ODYwMjB9");

        return sunfishIssuer;
    }

    private SunfishService createSunfishService(String baseUri) {

        SunfishService sunfishService = new SunfishService();

        sunfishService.setId(baseUri);

        return sunfishService;
    }

    private SunfishRequest createSunfishRequest(String method, int port, String path, String protocol) {

        SunfishRequest sunfishRequest = new SunfishRequest();

        sunfishRequest.setMethod(method);
        sunfishRequest.setPort(port);
        sunfishRequest.setPath(path);
        sunfishRequest.setProtocol(protocol);

        return sunfishRequest;

    }


    private SunfishRequestParameters createSunfishRequestParameters() {

        SunfishRequestParameters sunfishRequestParameters = new SunfishRequestParameters();

        return sunfishRequestParameters;

    }



    private SunfishRequestData createSunfishRequestData(Map<String, String> headers, String query) {

        SunfishRequestData sunfishRequestData = new SunfishRequestData();

        sunfishRequestData.setHeaders(headers);
        sunfishRequestData.setQueryString(query);

        return sunfishRequestData;
    }




    private SunfishSignature createSunfishSignature() {

        SunfishSignature sunfishSignature = new SunfishSignature();

        sunfishSignature.setSignature("");

        return sunfishSignature;

    }


}
