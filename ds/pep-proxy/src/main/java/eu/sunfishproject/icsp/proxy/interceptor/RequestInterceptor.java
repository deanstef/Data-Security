package eu.sunfishproject.icsp.proxy.interceptor;

import eu.sunfishproject.icsp.proxy.impl.CopyStreamThread;

import java.io.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public abstract class RequestInterceptor extends Thread {

    private InputStream clientInputStream;
    private OutputStream clientOutputStream;

    protected boolean isSecure = false;
    protected String targetHost = null;
    protected int targetPort = -1;

    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

    public RequestInterceptor(InputStream clientInputStream, OutputStream clientOutputStream) {
        this.clientInputStream = clientInputStream;
        this.clientOutputStream = clientOutputStream;
    }


    public abstract byte[] generateResponse(InputStream inputStream) throws IOException, InterruptedException, IllegalArgumentException;

    protected abstract void onFinish();



    @Override
    public void run() {


        try {
            short idle = 0;

            while (true) {

                    byte[] responseBytes = generateResponse(clientInputStream);

                    if(responseBytes != null && responseBytes.length > 0) {
                        clientOutputStream.write(responseBytes, 0, responseBytes.length);
                    } else {
                        idle++;
                    }


                if (idle > 0) {
                    Thread.sleep(Math.min(idle * 100, 1000));
                }
            }
        } catch (Exception e) {
            // Fail silently
//            e.printStackTrace();
        }

        try {
            clientInputStream.close();
        } catch (IOException e) {
        }

        try {
            clientOutputStream.close();
        } catch (IOException e) {
        }

        notifyListeners();
        onFinish();
    }





    private int readFromInput(InputStream inputStream) throws IOException {

        int b = inputStream.read();
        if(b == -1) {
            throw new IOException("Socket Closed!");
        }

        return b;

    }

    protected void printArray(byte[] array, String description) {

        System.err.println("****************************");
        System.err.println(description + ":");
        for(int i = 0; i<array.length; i++) {
            System.err.print((char)array[i]);

        }
        System.err.println("");
        System.err.println("****************************");

    }


    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }



    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }
    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }
    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }


    public interface ThreadCompleteListener {
        void notifyOfThreadComplete(final Thread thread);
    }

}
