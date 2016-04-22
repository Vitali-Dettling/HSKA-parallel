package org.parallel.auf3;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by vitali on 4/5/16.
 */
public class Util {


    public static int getRandomInt(int start, int end){
        return ThreadLocalRandom.current().nextInt(start, ++end);
    }
}
