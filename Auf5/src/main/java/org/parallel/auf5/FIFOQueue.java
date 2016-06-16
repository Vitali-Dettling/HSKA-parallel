package org.parallel.auf5;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by vitali on 5/6/16.
 */
public class FIFOQueue {

    private final static int SECONDS = 1000;
    private final static int LATE_CUSTOMER = 7 * SECONDS;

    private static final Logger logger = LogManager.getLogger(FIFOQueue.class);

    private static PriorityBlockingQueue<Customer> queue;

    //What is the difference?
    //Compare to: private static PriorityBlockingQueue<Customer> queue = new PriorityBlockingQueue<>()
    static{
        queue = new PriorityBlockingQueue<>();
    }

    public static void addCustomer(Customer customer){
        queue.add(customer);
    }

    public static boolean empty() throws InterruptedException {

        Thread.sleep(LATE_CUSTOMER);
        logger.info("Customer still queueing {}", queue.size());
        return queue.isEmpty();
    }

    public static Customer getNextInLine(){

        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




}
