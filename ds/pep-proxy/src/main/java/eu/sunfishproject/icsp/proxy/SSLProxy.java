package eu.sunfishproject.icsp.proxy;

import eu.sunfishproject.icsp.proxy.impl.ProxyThread;
import eu.sunfishproject.icsp.proxy.util.Constants;
import eu.sunfishproject.icsp.proxy.util.ProxyConfig;
//import iaik.security.provider.IAIK;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class SSLProxy {

    private static final Logger log  = LogManager.getLogger(ProxyConfig.class);



    public static void main(String[] args) {


//        Security.addProvider(new BouncyCastleProvider());
//        Security.addProvider(new IAIK());

        int processors = Runtime.getRuntime().availableProcessors();

        log.info("Using: " + processors + " Threads");

        ExecutorService executorService = Executors.newFixedThreadPool(processors);


        String proxyIP = ProxyConfig.getInstance().getProxyIP();
        int proxyPort = ProxyConfig.getInstance().getProxyPort();


        try {
            InetAddress address = InetAddress.getByName(proxyIP);
            ServerSocket serverSocket = new ServerSocket(proxyPort, 50, address);

//            ServerSocket serverSocket = new ServerSocket(10000);
            while(true) {
                ProxyThread thread = new ProxyThread(serverSocket.accept());
                executorService.submit(thread);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("attempt to shutdown executor");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!executorService.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executorService.shutdownNow();
            System.out.println("shutdown finished");
        }


    }


}
