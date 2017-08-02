package eu.sunfishproject.icsp.benchmark.client;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public interface TimerClient  {


    public void setup() throws Exception;
    public void run();
    public void destroy();

}
