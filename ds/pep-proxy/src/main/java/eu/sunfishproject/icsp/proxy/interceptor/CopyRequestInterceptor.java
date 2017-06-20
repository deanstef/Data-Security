package eu.sunfishproject.icsp.proxy.interceptor;

import eu.sunfishproject.icsp.proxy.http.HttpRequest;
import eu.sunfishproject.icsp.proxy.http.HttpResponse;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class CopyRequestInterceptor extends RequestInterceptor {

    private InputStream targetInputStream;
    private OutputStream targetOutputStream;


    Socket targetSocket = null;



    public CopyRequestInterceptor(InputStream clientInputStream, OutputStream clientOutputStream) {
        super(clientInputStream, clientOutputStream);
    }

    @Override
    public  byte[] generateResponse(InputStream inputStream) throws IOException, InterruptedException, IllegalArgumentException {

        HttpRequest request = new HttpRequest(inputStream);
        setupInterceptor(request);

        request.writeTo(targetOutputStream);

        HttpResponse response = new HttpResponse(targetInputStream);

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        response.writeTo(byteOutputStream);

        byte[] responseBytes = byteOutputStream.toByteArray();

        return responseBytes;
    }

    @Override
    protected void onFinish() {

        try {
            targetInputStream.close();
        } catch (IOException e) {
        }

        try {
            targetOutputStream.close();
        } catch (IOException e) {
        }
    }



    private void setupInterceptor(HttpRequest request) throws IOException {


        if(isSecure) {
            if(targetSocket == null) {
                targetSocket = connectToSSLRemote(this.targetHost, this.targetPort);
                this.targetInputStream = this.targetSocket.getInputStream();
                this.targetOutputStream = this.targetSocket.getOutputStream();
            }
        } else {

            String hostString = request.getHeaders().get("Host");

            if(hostString == null) {
                throw new IOException("Invalid request");
            }

            String[] hostArray = hostString.split(":");
            String host = hostArray[0];
            int port = hostArray.length > 2 ? Integer.parseInt(hostArray[1]) : 80;


            if(!host.equals(this.targetHost) || port != this.targetPort) {
                this.targetHost = host;
                this.targetPort = port;
                targetSocket = new Socket(this.targetHost, this.targetPort);

                this.targetInputStream = this.targetSocket.getInputStream();
                this.targetOutputStream = this.targetSocket.getOutputStream();
            }

        }

    }


    private SSLSocket connectToSSLRemote(String remoteHost, int remotePort) throws IOException {

        SSLSocket remoteSocket = null;

        SSLSocketFactory factory= (SSLSocketFactory) SSLSocketFactory.getDefault();
        remoteSocket = (SSLSocket) factory.createSocket(remoteHost, remotePort);
        remoteSocket.startHandshake();

        return remoteSocket;

    }


}
