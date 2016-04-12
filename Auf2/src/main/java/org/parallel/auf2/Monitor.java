package org.parallel.auf2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by vitali on 4/11/16.
 */
public class Monitor {

    private static final Logger logger = LogManager.getLogger(Monitor.class);

    private Condition freeThread;
    private Lock lock;

    private Condition unlock;

    public Monitor(){
        lock = new ReentrantLock();
        freeThread  = lock.newCondition();
        unlock = lock.newCondition();
    }

    public Automate checkAutomate(Object customerID){

        Automate automate = null;
        lock.lock();
        try {
            automate = Automate.nextAutomate();
            if (automate == null) {
                try {
                    logger.info("Customer {} waits patiently.", customerID);
                    freeThread.await();
                } catch (InterruptedException e) {
                    logger.error("Automate is not monitored correctly.");
                }
                automate = checkAutomate(customerID);
            }
        }finally {
            lock.unlock();
        }
        return automate;
    }


    public void customerLeases(){
        lock.lock();
        try {
            freeThread.signal();
        }finally{
            lock.unlock();
        }
    }
}
