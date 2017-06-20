package eu.sunfishproject.icsp.proxy.interceptor;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class RequestInterceptorFactory {

    private static RequestInterceptorFactory instance;

    private static final String SUNFISH_REQUEST_INTERCEPTOR = "SunfishRequestInterceptor";
    private static final String COPY_REQUEST_INTERCEPTOR = "CopyRequestInterceptor";


    private static final String DEFAULT_INTERCEPTOR = SUNFISH_REQUEST_INTERCEPTOR;



    private RequestInterceptorFactory() {

    }


    public static RequestInterceptorFactory getInstance() {
        if (instance == null) {
            instance = new RequestInterceptorFactory();
        }
        return instance;
    }


    public RequestInterceptor getDefaultHttpRequestInterceptor(InputStream clientInputStream, OutputStream clientOutputStream) {


        RequestInterceptor requestInterceptor = createInterceptor(DEFAULT_INTERCEPTOR, clientInputStream, clientOutputStream);

        return requestInterceptor;


    }


    public RequestInterceptor getDefaultHttpsRequestInterceptor(InputStream clientInputStream, OutputStream clientOutputStream, String targetHost, int targetPort) {

        RequestInterceptor requestInterceptor = createInterceptor(DEFAULT_INTERCEPTOR, clientInputStream, clientOutputStream);

        requestInterceptor.setSecure(true);
        requestInterceptor.setTargetHost(targetHost);
        requestInterceptor.setTargetPort(targetPort);

        return requestInterceptor;

    }


    public RequestInterceptor createInterceptor(String name, InputStream clientInputStream, OutputStream clientOutputStream) {


        RequestInterceptor interceptor;

        switch(name) {
            case COPY_REQUEST_INTERCEPTOR:
                interceptor = new CopyRequestInterceptor(clientInputStream, clientOutputStream);
                break;
            case SUNFISH_REQUEST_INTERCEPTOR:
                interceptor = new SunfishRequestInterceptor(clientInputStream, clientOutputStream);
                break;
            default:
                throw new IllegalArgumentException("Unknown Interceptor");
        }

        return interceptor;

    }


}
