package eu.sunfishproject.icsp.proxy.impl;


import java.io.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class CopyStreamThread extends Thread
{
    private final BufferedInputStream inputStream;
    private final OutputStream outputStream;

    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

    private static final int MAX_BUFFER = 4096;



    public CopyStreamThread(InputStream in, OutputStream out, String name) {
        super(name);
        if(in instanceof BufferedInputStream) {
            inputStream = (BufferedInputStream) in;
        } else {
            inputStream = new BufferedInputStream(in);
        }
        outputStream = out;

    }

    public CopyStreamThread(InputStream in, OutputStream out) {
        this(in, out, "Copy Thread");

    }

    public void run() {

        try {
            short idle = 0;

            while (true) {

                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

                int b;
                byte[] buffer = new byte[MAX_BUFFER];
                while ((b = inputStream.read(buffer, 0, MAX_BUFFER)) > 0) {

                    if(b == 0) {
                        idle++;
                    } else {
                        outputStream.write(buffer, 0, b);
                        idle = 0;
                    }

                    if (idle > 0) {
                        Thread.sleep(Math.max(10 * idle, 100));
                    }

                }

                if(b == -1) {
                    break;
                }


            }
        } catch (Exception e) {
            // Fail silently
            //e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
        }

        try {
            inputStream.close();
        } catch (IOException e) {
        }

        notifyListeners();
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
