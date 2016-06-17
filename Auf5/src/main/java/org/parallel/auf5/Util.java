package org.parallel.auf5;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by vitali on 4/5/16.
 */
public class Util {

    public final static int seconds = 1000;

    public final static int MAX_TIME_PROGRAM_TERMINATES = 4;

    public static int getRandomInt(int start, int end){
        return ThreadLocalRandom.current().nextInt(start, ++end);
    }
}
