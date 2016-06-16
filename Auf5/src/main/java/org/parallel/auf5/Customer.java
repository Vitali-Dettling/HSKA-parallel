package org.parallel.auf5;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vitali on 4/5/16.
 */
public class Customer implements Comparable<Customer> {

    private static final Logger logger = LogManager.getLogger(Customer.class);

    private int baskets;
    private boolean goldCustomer;

    public Customer(boolean isGoldCustomer){
        final int MIN_BASKETS = 2;
        final int MAX_BASKETS = 5;
        this.goldCustomer = isGoldCustomer;

        this.baskets = Util.getRandomInt(MIN_BASKETS, MAX_BASKETS);
    }

    public boolean isGoldCustomer(){
        return this.goldCustomer;
    }

    public void customersDuty(Automate automate){
        final int SEC = 1000;
        final int MIN_EMPTY_TIME = 3;
        final int MAX_EMPTY_TIME = 6;

        for(int i = 0 ; i < this.baskets ; i++){
            try {
                if (automate == null) {throw  new Exception();}
                logger.info("Customer {} is gold ({}) has {} baskets to empty and is using automate {}.", this.hashCode(), this.isGoldCustomer(), baskets, automate.hashCode());

                int toEmptyBasket = Util.getRandomInt(MIN_EMPTY_TIME, MAX_EMPTY_TIME);
                logger.info("Customer {} is emptying his or her basket for {} seconds.", this.hashCode(), toEmptyBasket);
                Thread.sleep(toEmptyBasket * SEC);
            } catch (Exception e) {
                logger.error("Customer {} has a problem with the machine." , this.hashCode());
            }
        }
    }

    @Override
    public int compareTo(Customer referenceCustomer) {

        //Compares the right order in the FIFOQueue
        if(referenceCustomer.isGoldCustomer()){
            return 1;
        }
        return -1;
    }
}
