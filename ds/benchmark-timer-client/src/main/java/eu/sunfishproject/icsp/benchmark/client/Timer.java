package eu.sunfishproject.icsp.benchmark.client;

/**
 * @author <a href="mailto:dominik.ziegler@a-sit.at">Dominik Ziegler</a>
 */
public class Timer {


    public static void main(String [] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Usage: supply either \"direct\" or \"sunfish\" and iteration count");
            System.exit(1);
        }

        String argument = args[0];
        int iterations = Integer.parseInt(args[1]);

        TimerClient client = null;
        if(argument.equals("direct")) {
            client = new DirectClient();

        } else {
             client = new SunfishClient();
        }

        client.setup();

        for(int i=0; i<iterations; i++) {
            long startTime = System.currentTimeMillis();
            client.run();
            long endTime = System.currentTimeMillis();
            System.out.println(endTime - startTime);
            //System.out.println("Execution: " + (endTime - startTime));
        }



        client.destroy();

    }


}
