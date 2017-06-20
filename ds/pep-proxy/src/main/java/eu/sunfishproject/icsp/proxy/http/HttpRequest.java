package eu.sunfishproject.icsp.proxy.http;

import eu.sunfishproject.icsp.proxy.util.ProxyConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class HttpRequest extends HttpObject {


    private static final Logger log  = LogManager.getLogger(HttpRequest.class);


    //private static Pattern httpRequestLinePattern = Pattern.compile("^([A-Z]+)[ \t]+(http://)?([^:]+):?/?(\\d*) (HTTP/\\d.\\d)", Pattern.DOTALL);
    private static Pattern httpRequestLinePattern = Pattern.compile("^([A-Z]+)[ \t]+([^\\s]+) (HTTP/\\d.\\d)", Pattern.DOTALL);

    private HttpMethod httpMethod;
    private String requestURI;
    private String httpVersion;



    public HttpRequest(String headers) throws IllegalArgumentException {
        this.initConnectionInfo(headers);
    }


    public HttpRequest(InputStream inputStream) throws IOException {
            init(inputStream);
    }


    @Override
    public void init(InputStream inputStream) throws IOException {

        String input = readLine(inputStream);
        this.initConnectionInfo(input);
        super.init(inputStream);

    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {


        String output = this.httpMethod.name() + " " + requestURI + " " + httpVersion + CRLF;

        outputStream.write(output.getBytes("ASCII"));

        super.writeTo(outputStream);

    }



    private void initConnectionInfo(String httpRequestLine) throws IllegalArgumentException {


        final Matcher httpMatcher = httpRequestLinePattern.matcher(httpRequestLine);

        if(httpMatcher.find()) {

            this.httpMethod = HttpMethod.valueOf(httpMatcher.group(1));
            this.requestURI = httpMatcher.group(2);
            this.httpVersion = httpMatcher.group(3);

        } else {
            throw new IllegalArgumentException("Invalid Input!");
        }

    }




    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }


    public URI requestURI() {


        URI uri = null;
        try {
            uri = new URI(requestURI);
        } catch(Exception e) {
            log.error(e);
        }

        return uri;
    }


}
