package org.parallel.auf2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vitali Dettling on 4/4/16.
 */
public class Supermarket{

    private final static int MAX_CUSTOMERS = 25;
    private final static int TIME_NEXT_CUSTOMER = 8;
    private final static int MAX_CUSTOMER_DUTY_TIME = 4;
    private final static float IN_SECONDS = 1000000000.0f;

    private static final Logger logger = LogManager.getLogger(Supermarket.class);
    private static ExecutorService executor = Executors.newFixedThreadPool(MAX_CUSTOMERS);
    private static final CountDownLatch waitTillNextCustomer = new CountDownLatch(MAX_CUSTOMERS);

     public static void main(String [] args) throws InterruptedException {

        long start = System.nanoTime();

         Monitor monitor = new Monitor();
         for(int i = 0 ; i < MAX_CUSTOMERS ; i++){
             logger.info("A new Customer has entered.");
             executor.execute(new Customer(monitor));
             waitTillNextCustomer.await(TIME_NEXT_CUSTOMER, TimeUnit.SECONDS);
         }

        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        executor.awaitTermination(MAX_CUSTOMER_DUTY_TIME, TimeUnit.MINUTES);

        double elapsedTime = (System.nanoTime() - start) / IN_SECONDS;
        logger.info("--------------------------- Final time: {} s. --------------------------- ", elapsedTime);

    }


}
