package org.parallel.auf3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vitali on 4/5/16.
 */
public class Automate {

    protected static final int AVAILABLE_AUTOMATES = 3;

    private static Map<Object, Boolean> available = new HashMap<>();
    private static Map<Object, Automate> instance = new HashMap<>();
    private static final boolean OCCUPIED = false;
    private static final boolean FREE = true;

    public static Automate nextAutomate(){
        //Derivation from the singleton pattern
        if(Automate.instance.size() < AVAILABLE_AUTOMATES){
            Automate newAutomate = new Automate();
            available.put(newAutomate.hashCode(), FREE);
            instance.put(newAutomate.hashCode(), newAutomate);
        }

        for(Map.Entry<Object, Boolean> singleEntry : available.entrySet()){
            if(singleEntry.getValue()) {
                available.put(singleEntry.getKey(), OCCUPIED);
                return instance.get(singleEntry.getKey());
            }
        }
        return null;
    }

    public void freeAutomate(Automate automate){
        available.put(automate.hashCode(), FREE);
    }

}
