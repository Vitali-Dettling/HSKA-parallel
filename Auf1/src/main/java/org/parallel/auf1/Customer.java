package org.parallel.auf1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vitali on 4/5/16.
 */
public class Customer implements Runnable {


    private static final Logger logger = LogManager.getLogger(Customer.class);

    private int baskets;
    private Automate automate;

    public Customer(){
        final int MIN_TIME = 2;
        final int MAX_TIME = 5;

        this.baskets = Util.getRandomInt(MIN_TIME, MAX_TIME);
    }

    public synchronized void run(){
        checkAutomate();
        automate.setAvailability(false);
        customersDuty();
        automate.setAvailability(true);
    }

    private void customersDuty(){
        final int SEC = 1000;
        final int MIN_TIME = 3;
        final int MAX_TIME = 6;

        logger.info("Customer {} has {} baskets to empty and is using automate {}.", this.hashCode(), baskets, automate.hashCode());
        for(int i = 0 ; i < this.baskets ; i++){
            try {
                int toEmptyBasket = Util.getRandomInt(MIN_TIME, MAX_TIME);
                logger.info("Customer {} is emptying his or her basket for {} seconds.", this.hashCode(), toEmptyBasket);
                Thread.sleep(toEmptyBasket * SEC);
            } catch (InterruptedException e) {
                logger.error("Customer {} has an problem with the machine." , this.hashCode());
            }
        }
        notify();
    }


    private void checkAutomate(){
        automate = Automate.anAvailableAutomates();
        if(automate == null){
            try {
                logger.info("Customer {} waits patiently.", this.hashCode());
                wait();
                checkAutomate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
