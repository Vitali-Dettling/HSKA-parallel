package org.parallel.auf1;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by vitali on 4/5/16.
 */
public class Util {


    public static int getRandomInt(int start, int end){
        return ThreadLocalRandom.current().nextInt(start, ++end);
    }
}
