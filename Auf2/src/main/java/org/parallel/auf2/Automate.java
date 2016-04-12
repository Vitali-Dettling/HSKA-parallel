package org.parallel.auf2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitali on 4/5/16.
 */
public class Automate {

    private static final int AVAILABLE_AUTOMATES = 3;

    private static List<Automate> instance = new ArrayList<>();
    private static boolean available;

    public static Automate nextAutomate(){
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
