package org.parallel.auf1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitali on 4/5/16.
 */
public class Automate {

    private static final Logger logger = LogManager.getLogger(Automate.class);

    private static final int AVAILABLE_AUTOMATES = 3;

    private static List<Automate> instance = new ArrayList<>();
    private static boolean available;

    public static Automate anAvailableAutomates(){

        //Derivation from the singleton pattern
        if(Automate.instance.size() <= AVAILABLE_AUTOMATES){
            Automate.instance.add(new Automate());
            Automate.available = true;
        }

        //Returns the first free automate
        return Automate.instance.parallelStream().filter(
                automate -> automate.available)
                .findAny().orElse(null);
    }

    public static void setAvailability(boolean isOccupied) {
        Automate.available = isOccupied;
    }

}
