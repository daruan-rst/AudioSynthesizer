package com.audio.synth.utilities;

import java.awt.*;

public class ParameterHandling {

    public static final Robot PARAMETER_ROBOT;

    static{
        try{
            PARAMETER_ROBOT = new Robot();
        }catch(AWTException e){
            throw new ExceptionInInitializerError("Cannot Construct robot instance");
        }

    }
}
