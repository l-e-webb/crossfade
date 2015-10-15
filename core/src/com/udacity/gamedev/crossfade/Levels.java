package com.udacity.gamedev.crossfade;

/**
 * Created by louiswebb on 10/12/15.
 */
public class Levels {

    private static byte OFF = (byte) 0;
    private static byte ON = (byte) 1;
    public static byte[][][] LEVELS;

    public Levels() {
        LEVELS = new byte[][][]{
                {
                        {OFF, OFF, OFF, OFF, OFF},
                        {OFF, OFF, OFF, OFF, OFF},
                        {OFF, OFF, OFF, OFF, OFF},
                        {OFF, OFF, OFF, OFF, OFF},
                        {OFF, OFF, OFF, OFF, OFF}
                }
        };
    }
}
