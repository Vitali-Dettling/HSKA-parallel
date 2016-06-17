package org.parallel.auf5;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

/**
 * Created by vitali on 4/5/16.
 */
public class Customer implements Callable {

    private Barbershop shop;
    private Semaphore hairCut;

    public Customer(Barbershop shop, Semaphore hairCut){
        this.shop = shop;
        this.hairCut = hairCut;
    }

    @Override
    public Object call() throws InterruptedException {

        while(true) {

            if (this.shop.enterShop(this.hashCode())) {
                this.shop.sitOnSofa(this);
                    this.hairCut.acquire();
                    this.shop.pay(this.hashCode());
            }
            Thread.sleep(Util.getRandomInt(10, 20) * Util.seconds);
        }
    }
}
