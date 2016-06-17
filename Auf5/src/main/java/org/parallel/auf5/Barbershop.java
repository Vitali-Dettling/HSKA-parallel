package org.parallel.auf5;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by vitali on 6/16/16.
 */
public class Barbershop {

    private final static int MAX_BARBER = 3;
    private final static int MUTEX_SEMAPHORE = 1;
    private final static int MAX_CUSTOMER = 30;

    private static final Logger logger = LogManager.getLogger(Barbershop.class);

    private static ArrayBlockingQueue sofa = new ArrayBlockingQueue(4);
    private static ArrayBlockingQueue standingRoom = new ArrayBlockingQueue(16);

    private static ScheduledExecutorService barberPool = Executors.newScheduledThreadPool(MAX_BARBER);
    private static ScheduledExecutorService customerPool = Executors.newScheduledThreadPool(MAX_CUSTOMER);

    private static Semaphore mutex = new Semaphore(MUTEX_SEMAPHORE);
    private static Semaphore chairs = new Semaphore(3);
    private static Semaphore receipt = new Semaphore(0);
    private static Semaphore cash = new Semaphore(0);
    private static Semaphore hairCut = new Semaphore(0);
    private static Semaphore cuttingBarber = new Semaphore(1);

    private static Barbershop shop = new Barbershop();
    private static int numberCustomers = 1;

    public boolean enterShop(int customerID) {

        try {
            mutex.acquire();

                if(numberCustomers >= 20){
                    mutex.release();
                    logger.info("Shop is full ({}), thus a customer could not enter it.", numberCustomers);
                    return false;
                }
            logger.info("Customer {} has entered the shop.", customerID);
            numberCustomers++;
        } catch (InterruptedException e) {
            logger.error("Customer has a problem entering the shop.");
        }finally{
            mutex.release();
        }

        return true;
    }

    public void sitOnSofa(Customer customer) {

        if(sofa.size() < 4){
            sofa.add(customer);
            logger.info("Another customer sits on sofa ({}).", sofa.size());
        }else {
            standingRoom.add(customer);
            logger.info("Sofa is full, thus the customer has to stay ({}).", standingRoom.size());
        }
    }

    public void sitInBarberChair(){

        Runnable standingCustomerTakesASeat = () -> {
            if(!standingRoom.isEmpty()){
                try {
                    if(!standingRoom.isEmpty()) {
                        Customer customer = (Customer) standingRoom.take();
                        sofa.add(customer);
                        logger.info("Standing customer takes a sit on the sofa. Customer ID: ({}).", customer.hashCode());
                    }
                } catch (InterruptedException e) {
                    logger.error("Customer cannot sit down on the sofa.");
                }
            }
        };
        standingCustomerTakesASeat.run();
    }

    public void cutHair(int customerID, int barberID){

        final int cuttingHairSec = 5000;

        try {
            chairs.acquire();
                logger.info("Barber {} (still available chairs: {}) cuts hair of customer {}.", barberID, chairs.availablePermits(), customerID);
                Thread.sleep(cuttingHairSec);
            chairs.release();
        } catch (InterruptedException e) {
            logger.error("Barber cannot cut hair.");
        }
    }

    public void pay(int customerID){

        try {
            logger.info("Customer ({}) has payed.", customerID);
            cash.release();
            receipt.acquire();

            mutex.acquire();
            numberCustomers--;
            logger.info("Customer ({}) has left the shop.", customerID);
            mutex.release();
        } catch (InterruptedException e) {
            logger.info("Payment did not work.");
        }
    }

    public void acceptPayment(){

        try {
            hairCut.release();
            cash.acquire();
            logger.info("One barber has accepted payment from customer.");
            receipt.release();
            cuttingBarber.release();
        } catch (InterruptedException e) {
            logger.info("Barber could not accept the payment.");
        }
    }




    public static void main(String [] args) throws InterruptedException {

        runBarbers();
        runCustomers();

        Thread.sleep(1000);

        customerPool.shutdown();
        barberPool.shutdown();

        customerPool.awaitTermination(Util.MAX_TIME_PROGRAM_TERMINATES, TimeUnit.MINUTES);
        barberPool.awaitTermination(Util.MAX_TIME_PROGRAM_TERMINATES, TimeUnit.MINUTES);
    }

    public static void runCustomers(){

        ArrayList preparedCustomerPool = new ArrayList();
        for (int i = 0 ; i < MAX_CUSTOMER ; i++) {
            preparedCustomerPool.add(new Customer(shop, hairCut));
        }

        Runnable customerThreadTask = () -> {
            try {
                customerPool.invokeAll(preparedCustomerPool);
            } catch (InterruptedException e) {
                logger.error("Customer cannot find the shop.");
            }
        };
        customerPool.submit(customerThreadTask);
        logger.info("Customers are coming.");
    }

    public static void runBarbers() throws InterruptedException {

        ArrayList preparedBarberPool = new ArrayList();
        for (int i = 0 ; i < MAX_BARBER ; i++) {
            preparedBarberPool.add(new Barber(shop, sofa, cuttingBarber));
        }

        Runnable barberTreadTask = () -> {
            try {
                barberPool.invokeAll(preparedBarberPool);
            } catch (InterruptedException e) {
                logger.error("Barber is still sleeping.");
            }
        };
        barberPool.submit(barberTreadTask);
        logger.info("Barbers are sleeping in chair.");
    }
}
