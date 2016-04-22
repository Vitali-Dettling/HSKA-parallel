package org.parallel.auf3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Vitali Dettling on 4/4/16.
 */
public class Supermarket{

    private final static int MAX_CUSTOMERS = 25;
    private final static int SECONDS = 1000;
    private final static int TIME_NEXT_CUSTOMER = 7 * SECONDS;
    private final static int MAX_TIME_PROGRAM_TERMINATES = 4;
    private final static float IN_SECONDS = 1000000000.0f;

    private static final Logger logger = LogManager.getLogger(Supermarket.class);
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(MAX_CUSTOMERS);
    private static List<Callable<Customer>> preparedCalledCustomers;

     public static void main(String [] args) throws InterruptedException {

         long start = System.nanoTime();
         Semaphore available = new Semaphore(Automate.AVAILABLE_AUTOMATES);

         preparedCalledCustomers = new ArrayList();
         for (int i = 0 ; i < MAX_CUSTOMERS ; i++) {
            preparedCalledCustomers.add(new Customer(available));
         }
         runCallable();

        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
           executor.shutdown();
        // Wait until all threads are finish
           executor.awaitTermination(MAX_TIME_PROGRAM_TERMINATES, TimeUnit.MINUTES);

        double elapsedTime = (System.nanoTime() - start) / IN_SECONDS;
        logger.info("--------------------------- Final time: {} s. --------------------------- ", elapsedTime);

    }

    public static void runCallable() {

        preparedCalledCustomers.forEach(customer -> {
            try {
                executor.submit(customer);
                Thread.sleep(TIME_NEXT_CUSTOMER);
            } catch (Exception e) {
                logger.error("Customer thread pool error.");
            }
        });
    }






}
