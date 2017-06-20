package eu.sunfishproject.icsp.proxy.http;

import org.apache.commons.httpclient.ChunkedInputStream;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class HttpObject {

    protected Map<String, String> headers;
    protected byte[] body;


    protected static final String CRLF = "\r\n";


    public HttpObject() {

    }


    public HttpObject(InputStream inputStream) throws IOException {

        init(inputStream);

    }


    public void init(InputStream inputStream) throws IOException {
        this.headers = parseHTTPHeaders(inputStream);
        this.body = parseBody(inputStream);
    }


    public void writeTo(OutputStream outputStream) throws IOException {

        writeHeaders(outputStream);
        outputStream.write(CRLF.getBytes("ASCII"));
        outputStream.write(body);
    }


    protected String readLine(InputStream inputStream) throws IOException {

        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int byteRead = readByte(inputStream);
            if (byteRead == '\r') {
                int ignore = readByte(inputStream);
                break;
            } else {
                stringBuffer.append((char) byteRead);
            }
        }

        String input = stringBuffer.toString();
        return input;

    }

    protected int readByte(InputStream inputStream) throws IOException {

        int b = inputStream.read();

        if(b == -1) {
            throw new IOException();
        }

        return b;
    }




    private Map<String, String> parseHTTPHeaders(InputStream inputStream) throws IOException {

        // http://snippets.dinduks.com/java-parse-http-headers-from-an-inputstream

        int charRead;
        int bytesRead = 0;
        StringBuffer sb = new StringBuffer();
        while (true) {
            sb.append((char) (charRead = inputStream.read()));
            if ((char) charRead == '\r') {
                sb.append((char) inputStream.read());
                charRead = inputStream.read();
                if (charRead == '\r') {
                    sb.append((char) inputStream.read());
                    break;
                } else {
                    sb.append((char) charRead);
                }
            }
        }

        String[] headersArray = sb.toString().split("\\r?\\n");

        Map<String, String> headers = new HashMap<>();

        for(String headerString : headersArray) {
            String[] header = headerString.split(": ");
            String key = header[0];
            String value = header.length >= 2 ? header[1] : "";
            headers.put(key, value);
        }


        return headers;
    }



    private byte[] parseBody(InputStream inputStream) throws IOException {


        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

        String contentLengthString = this.headers.get("Content-Length");
        int contentLength = (contentLengthString != null && !contentLengthString.isEmpty()) ? Integer.parseInt(contentLengthString) : -1;


        String transferEncodingString = this.headers.get("Transfer-Encoding");
        if(transferEncodingString != null && transferEncodingString.equals("chunked")) {

            ChunkedInputStream chunkedInputStream = new ChunkedInputStream(inputStream);

            int b;
            while((b = chunkedInputStream.read()) != -1) {
                byteOutputStream.write(b);
            }



        } else if(contentLength > 0) {


            byte[] buffer = new byte[contentLength];

            DataInputStream dataInputStream = new DataInputStream(inputStream);
            dataInputStream.readFully(buffer);
            byteOutputStream.write(buffer);


        }


        byte[] result =  byteOutputStream.toByteArray();

        return result;

    }



    private void writeHeaders(OutputStream outputStream) throws IOException {

        Iterator<String> iterator = this.headers.keySet().iterator();


        StringBuffer stringBuffer = new StringBuffer();

        while(iterator.hasNext()) {
            String key = iterator.next();

            if(!key.equals("Transfer-Encoding")) {
                String value = this.headers.get(key);
                stringBuffer.append(key + ": " + value + CRLF);
            } else if(this.body != null && this.body.length > 0) {
                stringBuffer.append("Content-Length: " + this.body.length + CRLF);
            }
        }

        outputStream.write(stringBuffer.toString().getBytes("ASCII"));


    }



    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }




}
