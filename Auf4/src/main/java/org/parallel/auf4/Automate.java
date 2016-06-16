package org.parallel.auf4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * Created by vitali on 4/5/16.
 */
public class Automate implements Callable {

    private static final Logger logger = LogManager.getLogger(Automate.class);

    @Override
    public Object call() throws Exception {

        Customer nextCustomer = FIFOQueue.getNextInLine();
        logger.info("New Customer {} has entered and is gold ({}). ", nextCustomer.hashCode(), nextCustomer.isGoldCustomer()); //and it has {} queued threads.", this.hashCode(), available.hasQueuedThreads());

        nextCustomer.customersDuty(this);
        logger.info("Customer {} is leaving and s/he was gold ({}). ", nextCustomer.hashCode(), nextCustomer.isGoldCustomer());

        if(!FIFOQueue.empty()) {
            call();//Next round
        }
        return null;
    }
}
