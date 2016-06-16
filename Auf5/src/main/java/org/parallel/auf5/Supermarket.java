package org.parallel.auf5;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vitali Dettling on 4/4/16.
 */
public class Supermarket{

    private final static int MAX_CUSTOMERS = 25;
    //Why is the first automate disappearing, when it is set to 3?
    private final static int MAX_AUTOMATES = 4;
    private final static int SECONDS = 1000;
    private final static int TIME_NEXT_CUSTOMER = 7 * SECONDS;
    private final static int TIME_NEXT_GOLD_CUSTOMER = 10 * SECONDS;
    private final static int MAX_TIME_PROGRAM_TERMINATES = 4;
    private final static float IN_SECONDS = 1000000000.0f;

    private static final Logger logger = LogManager.getLogger(Supermarket.class);
    private static ScheduledExecutorService producer = Executors.newScheduledThreadPool(MAX_CUSTOMERS);
    private static ScheduledExecutorService consumer = Executors.newScheduledThreadPool(MAX_AUTOMATES);
    private static List<Callable<Automate>> preparedCalledAutomates;
    private static boolean supermarketClosed = false;

     public static void main(String [] args) throws InterruptedException {

         long start = System.nanoTime();

         preparedCalledAutomates = new ArrayList();
         for (int i = 0 ; i < MAX_AUTOMATES ; i++) {
             preparedCalledAutomates.add(new Automate());
         }
         runAutomates();
         runCustomer();

         // This will make the executor accept no new threads
         // and finish all existing threads in the queue
             producer.shutdown();
             consumer.shutdown();
        // Wait until all threads are finish
            producer.awaitTermination(MAX_TIME_PROGRAM_TERMINATES, TimeUnit.MINUTES);
            consumer.awaitTermination(MAX_TIME_PROGRAM_TERMINATES, TimeUnit.MINUTES);

        double elapsedTime = (System.nanoTime() - start) / IN_SECONDS;
        logger.info("--------------------------- Final time: {} s. --------------------------- ", elapsedTime);
    }

    public static void runAutomates(){

        Runnable consumerAutomate = () -> {
            try {
                consumer.invokeAll(preparedCalledAutomates);
            } catch (InterruptedException e) {
                logger.error("Automate is broken.");
            }
        };
        consumer.submit(consumerAutomate);
    }


    public static void runCustomer(){

        final boolean goldCustomerBool = true;

        Runnable normalCustomer = () -> {
            try {
                for (int i = 0 ; i < MAX_CUSTOMERS ; i++) {
                    FIFOQueue.addCustomer(new Customer(!goldCustomerBool));
                    Thread.sleep(TIME_NEXT_CUSTOMER);
                }
                supermarketClosed = true;
            } catch (InterruptedException e) {
                logger.error("Customer thread pool error.");
            }
        };

        Runnable goldCustomer = () -> {
            try {
                while(!supermarketClosed) {
                    Thread.sleep(TIME_NEXT_GOLD_CUSTOMER);
                    FIFOQueue.addCustomer(new Customer(goldCustomerBool));
                }
            } catch (InterruptedException e) {
                logger.error("Customer thread pool error.");
            }
        };

        producer.submit(normalCustomer);
        producer.submit(goldCustomer);
    }
}
