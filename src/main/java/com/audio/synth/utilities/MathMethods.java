package com.audio.synth.utilities;
import static java.lang.Math.*;




public class MathMethods {

    public static double frequencyToAngular(double freq){
        return 2 * Math.PI*freq;
    }

    public static double getKeyFrequency(int keyNum){
        return pow(root(2,12),(keyNum-49)) * 440;
    }

    public static double root(double num, double root){
        return pow(E, log(num)/root);
    }

}


