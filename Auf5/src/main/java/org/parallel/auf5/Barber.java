package org.parallel.auf5;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/**
 * Created by vitali on 6/16/16.
 */
public class Barber implements Callable {


    private static final Logger logger = LogManager.getLogger(Barber.class);

    private ArrayBlockingQueue sofa;
    private Barbershop shop;
    private Semaphore cuttingBarber;

    public Barber(Barbershop shop, ArrayBlockingQueue sofa, Semaphore cuttingBarber){

        this.sofa = sofa;
        this.shop = shop;
        this.cuttingBarber = cuttingBarber;
    }


    @Override
    public Object call() throws Exception {

        while(true) {

            Customer customer = (Customer) this.sofa.take();

            this.shop.sitInBarberChair();
            this.shop.cutHair(customer.hashCode(), this.hashCode());
                this.cuttingBarber.acquire();
                this.shop.acceptPayment();

            logger.info("Barber is tired and sleeps for a while.");
            Thread.sleep(Util.getRandomInt(0, 2) * Util.seconds);
        }
    }
}
