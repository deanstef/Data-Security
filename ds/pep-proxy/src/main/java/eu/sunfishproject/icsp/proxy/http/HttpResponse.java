package eu.sunfishproject.icsp.proxy.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class HttpResponse extends HttpObject {


    private String httpVersion;
    private int statusCode;
    private String reasonPhrase;

    private static Pattern httpStatusLinePattern = Pattern.compile("^(HTTP/\\d\\.\\d) (\\d{3}) (.+)", Pattern.DOTALL);



    public HttpResponse() {
    }


    public HttpResponse(InputStream inputStream) throws IOException {

        init(inputStream);

    }


    @Override
    public void init(InputStream inputStream) throws IOException {

        String input = readLine(inputStream);
        this.initStatus(input);
        super.init(inputStream);

    }


    @Override
    public void writeTo(OutputStream outputStream) throws IOException {

        String output = this.httpVersion + " " + this.statusCode + " " + this.reasonPhrase + CRLF;

        outputStream.write(output.getBytes("ASCII"));

        super.writeTo(outputStream);

    }




    private void initStatus(String httpRequestLine) throws IllegalArgumentException {


        final Matcher httpMatcher = httpStatusLinePattern.matcher(httpRequestLine);

        if(httpMatcher.find()) {

            this.httpVersion = httpMatcher.group(1);
            this.statusCode = Integer.parseInt(httpMatcher.group(2));
            this.reasonPhrase = httpMatcher.group(3);


        } else {
            throw new IllegalArgumentException("Invalid Input!");
        }

    }


    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }
}
