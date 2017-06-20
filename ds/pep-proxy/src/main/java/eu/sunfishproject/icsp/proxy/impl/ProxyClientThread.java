package eu.sunfishproject.icsp.proxy.impl;

import eu.sunfishproject.icsp.proxy.interceptor.CopyRequestInterceptor;
import eu.sunfishproject.icsp.proxy.interceptor.RequestInterceptor;
import eu.sunfishproject.icsp.proxy.interceptor.RequestInterceptorFactory;

import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class ProxyClientThread extends Thread {


    private ServerSocket proxyServerSocket;
    private String targetHost;
    private int targetPort;


    public ProxyClientThread(ServerSocket proxyServerSocket, String targetHost, int targetPort) {

        this.proxyServerSocket = proxyServerSocket;
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }


    @Override
    public void run() {

        try {

            // Proxy Socket -> InputStream --> RemoteSocket - OutputStream
            // Proxy Socket -> OutputStream --> RemoteSocket - InputStream
            final Socket proxySocket = this.proxyServerSocket.accept();

            RequestInterceptor requestInterceptor = RequestInterceptorFactory.getInstance().getDefaultHttpsRequestInterceptor(proxySocket.getInputStream(), proxySocket.getOutputStream(), targetHost, targetPort);
            requestInterceptor.start();


        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
