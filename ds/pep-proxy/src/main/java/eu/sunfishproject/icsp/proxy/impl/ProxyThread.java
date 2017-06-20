package eu.sunfishproject.icsp.proxy.impl;

import eu.sunfishproject.icsp.proxy.certificate.CertificateUtil;
import eu.sunfishproject.icsp.proxy.http.HttpRequest;
import eu.sunfishproject.icsp.proxy.interceptor.RequestInterceptor;
import eu.sunfishproject.icsp.proxy.interceptor.RequestInterceptorFactory;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.Certificate;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class ProxyThread implements Runnable {


    private Socket clientSocket = null;
    private BufferedInputStream clientInputStream = null;
    private OutputStream clientOutputStream = null;

    protected final int PEEK_SIZE = 4096;

    protected final int MAX_IDLE_SECONDS = 30;


    public ProxyThread(Socket socket) {
        this.clientSocket = socket;
    }



    @Override
    public void run() {

        System.out.println("Incoming connection! " + System.currentTimeMillis());

        HttpRequest clientRequest = null;
        try {

            this.clientInputStream = new BufferedInputStream(clientSocket.getInputStream());
            this.clientOutputStream = clientSocket.getOutputStream();


            byte[] buffer = new byte[PEEK_SIZE];
            clientInputStream.mark(PEEK_SIZE);

            System.out.println("Trying to read buffer!");

            int idle = 0;
            while(clientInputStream.available() == 0) {
                idle++;
                if(idle >= MAX_IDLE_SECONDS) {
                    System.err.println("Timeout reached trying to read buffer!");
                    throw new Exception("Timeout!");
                }
                if (idle > 0) {
                    Thread.sleep(1000);
                }
            }

            int bytesRead = clientInputStream.read(buffer, 0, buffer.length);

            System.out.println("Buffer Read: " + bytesRead + " bytes");

            clientInputStream.reset();

            if(bytesRead > 0) {

                String input = new String(buffer, 0, bytesRead, "ASCII");

                clientRequest = new HttpRequest(input);

                switch (clientRequest.getHttpMethod()) {

                    case GET:
                    case POST:
                    case PUT:
                    case DELETE:
                        handleHttpRequest();
                        break;

                    case CONNECT:
                        connect();

                        break;

                    default:
                        throw new Exception("Not Implemented!");
                }
            }


        } catch(IOException ignore) {
            //
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sendClientResponse("504 Gateway Timeout", clientRequest.getRequestURI());
            } catch(Exception ex) {
                // Fail silently
            }
            try {
                clientSocket.close();
            } catch (Exception ignore) {
            }
        }

        System.out.println("Finished Connection! " + System.currentTimeMillis());
    }


    private void handleHttpRequest() throws IOException {

        RequestInterceptor requestInterceptor = RequestInterceptorFactory.getInstance().getDefaultHttpRequestInterceptor(this.clientInputStream, this.clientOutputStream);

        requestInterceptor.addListener(new RequestInterceptor.ThreadCompleteListener() {

            @Override
            public void notifyOfThreadComplete(Thread thread) {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    int a = 0;
                }
            }
        });

        requestInterceptor.start();

    }


    private void connect() throws IOException {


//        HttpRequest clientRequest = new HttpRequest(this.clientInputStream);
//
//
//        //byte[] requestBytes = readAllBytes(clientInputStream, false);
//
//
//        String requestURI = clientRequest.getRequestURI();
//
//        if(!requestURI.startsWith("https://")) {
//            requestURI = "https://" + requestURI;
//        }
//
//        URL url = new URL(requestURI);
//
//        String targetHost = url.getHost();
//        int targetPort = url.getPort();
//
//
//        final String target = targetHost + ":" + targetPort;
//        System.err.println("******* Establishing a new HTTPS proxy connection to " + target);
//
//
//
//        // TODO: JUST FOR DEBUG
//        // This is how the correct proxy works
////        SocketFactory factory= SocketFactory.getDefault();
////        Socket remoteSocket = factory.createSocket(remoteHost, remotePort);
////
////        InputStream remoteInputStream = remoteSocket.getInputStream();
////        OutputStream remoteOutputStream = remoteSocket.getOutputStream();
////
////        new CopyStreamThread(remoteInputStream, clientOutputStream, "Copy to client from " + target).start();
////        new CopyStreamThread(clientInputStream, remoteOutputStream, "Copy from client to " + target).start();
//
//
//
//        SSLContext sslContext;
//        try {
//            SSLSocket targetSocket = connectToSSLRemote(targetHost, targetPort);
//            Certificate targetCertificate = targetSocket.getSession().getPeerCertificates()[0];
//            sslContext = CertificateUtil.createSSLContext(targetCertificate);
//            targetSocket.close();
//
//        } catch (UnknownHostException e) {
//            sslContext = CertificateUtil.createSSLContext(targetHost);
//        }
//
//
//        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
//
//        final ServerSocket proxyServerSocket = sslServerSocketFactory.createServerSocket(0);
//
//        new ProxyClientThread(proxyServerSocket, targetHost, targetPort).start();
//
//        try {Thread.sleep(10);} catch (Exception ignore) {}
//
//
//        final Socket proxyClientSocket = SocketFactory.getDefault().createSocket(proxyServerSocket.getInetAddress(), proxyServerSocket.getLocalPort());
//
//        OutputStream clientProxyOutput = proxyClientSocket.getOutputStream();
//        InputStream clientProxyInput = proxyClientSocket.getInputStream();
//
//
//
//        // Forward everything from client to proxy --> Handshake is done automatically
//        CopyStreamThread toClientThread = new CopyStreamThread(this.clientInputStream, clientProxyOutput, "Copy from client to proxy");
//        toClientThread.addListener(new CopyStreamThread.ThreadCompleteListener() {
//            @Override
//            public void notifyOfThreadComplete(Thread thread) {
//                try {
//                    proxyServerSocket.close();
//                } catch (Exception ignore) {
//                    int a = 0;
//                }
//                try {
//                    proxyClientSocket.close();
//                } catch (Exception ignore) {
//                    int a = 0;
//                }
//                try {
//                    clientSocket.close();
//                } catch (Exception ignore) {
//                    int a = 0;
//                }
//
//            }
//        });
//        toClientThread.start();
//        new CopyStreamThread(clientProxyInput, this.clientOutputStream, "Copy to client from proxy").start();
//
//        sendClientResponse("200 OK",requestURI);

    }



    private void sendClientResponse(String msg, String requestURI) throws IOException {
        final StringBuffer response = new StringBuffer();
        response.append("HTTP/1.0 ").append(msg).append("\r\n");
        response.append("Host: " +requestURI+ "\r\n");
        response.append("Proxy-agent: SUNFISH-Proxy1.0\r\n");
        response.append("\r\n");
        this.clientOutputStream.write(response.toString().getBytes());
        this.clientOutputStream.flush();
    }



    private SSLSocket connectToSSLRemote(String remoteHost, int remotePort) throws IOException {

        SSLSocket remoteSocket = null;

        SSLSocketFactory factory= (SSLSocketFactory) SSLSocketFactory.getDefault();
        remoteSocket = (SSLSocket) factory.createSocket(remoteHost, remotePort);
        remoteSocket.startHandshake();

        return remoteSocket;

    }


    private void clearClientBuffer() throws  IOException {
        final byte[] buffer = new byte[4096];

        while (this.clientInputStream.read(buffer, 0, this.clientInputStream.available()) > 0) {
        }
    }


    protected byte[] readAllBytes(InputStream inputStream, boolean blocking) throws IOException {


        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

        if(blocking) {
            int b = inputStream.read();
            byteOutputStream.write(b);
        }

        while (inputStream.available() != 0) {
            int b = inputStream.read();
            byteOutputStream.write(b);
        }

        return byteOutputStream.toByteArray();


    }


}
