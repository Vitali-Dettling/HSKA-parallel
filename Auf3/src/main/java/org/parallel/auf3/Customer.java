package org.parallel.auf3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/**
 * Created by vitali on 4/5/16.
 */
public class Customer implements Callable {

    private static final Logger logger = LogManager.getLogger(Customer.class);

    private final Semaphore available;

    private int baskets;

    public Customer(Semaphore onlyOneSemaphore){
        final int MIN_BASKETS = 2;
        final int MAX_BASKETS = 5;

        available = onlyOneSemaphore;
        this.baskets = Util.getRandomInt(MIN_BASKETS, MAX_BASKETS);
    }

    private void customersDuty(Automate automate){
        final int SEC = 1000;
        final int MIN_EMPTY_TIME = 3;
        final int MAX_EMPTY_TIME = 6;

        for(int i = 0 ; i < this.baskets ; i++){
            try {
                if (automate == null) {throw  new Exception();}
                logger.info("Customer {} has {} baskets to empty and is using automate {}.", this.hashCode(), baskets, automate.hashCode());

                int toEmptyBasket = Util.getRandomInt(MIN_EMPTY_TIME, MAX_EMPTY_TIME);
                logger.info("Customer {} is emptying his or her basket for {} seconds.", this.hashCode(), toEmptyBasket);
                Thread.sleep(toEmptyBasket * SEC);
            } catch (Exception e) {
                logger.error("Customer {} has a problem with the machine." , this.hashCode());
            }
        }
    }


    @Override
    public Object call() throws Exception {

        logger.info("New Customer {} has entered.", this.hashCode());
        if(available.tryAcquire()){
            logger.info("Customer {} waits patiently.", this.hashCode());
        }
        available.acquire();
        Automate automate = Automate.nextAutomate();
        if(automate == null){call();}
        this.customersDuty(automate);
        logger.info("Customer {} is leaving. ", this.hashCode());
        automate.freeAutomate(automate);
        //Do not understand why release has to be with number of automates?
        //Otherwise the threads will be released individual which ends up in a linear execution of customers.
        //Example: available.release(); -> One thread after another are executed, why?
        available.release(Automate.AVAILABLE_AUTOMATES);
        return this;
    }


}
