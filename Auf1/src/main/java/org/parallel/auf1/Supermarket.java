package org.parallel.auf1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Vitali Dettling on 4/4/16.
 */
public class Supermarket{

    private static final Logger logger = LogManager.getLogger(Supermarket.class);

     public static void main(String [ ] args) throws InterruptedException {

        final int MAX_CUSTOMERS = 25;
        final int TIME_NEXT_CUSTOMER = 8000;

        long start = System.nanoTime();

        for(int i = 0 ; i < MAX_CUSTOMERS ; i++){
            logger.info("A new Customer has entered.");
            new Thread(new Customer()).start();
            Thread.sleep(TIME_NEXT_CUSTOMER);
        }

        double elapsedTime = (System.nanoTime() - start) / 1000000000.0;
        logger.info("--------------------------- Final time: {} s. --------------------------- ", elapsedTime);

    }
}
