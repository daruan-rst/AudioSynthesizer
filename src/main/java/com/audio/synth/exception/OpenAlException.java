package com.audio.synth.exception;

import static org.lwjgl.openal.AL10.*;

public class OpenAlException extends RuntimeException{

    public OpenAlException(int errorCode){
        super("Invalid" + returnMessage(errorCode) + "OpenAl exception");
    }

    private static String returnMessage(int errorCode) {
        String message;
        switch (errorCode) {
            case AL_INVALID_NAME:
                message = "invalid name";
                break;
            case AL_INVALID_ENUM:
                message = "invalid enum";
                break;
            case AL_INVALID_VALUE:
                message = "invalid value";
                break;
            case AL_INVALID_OPERATION:
                message = "invalid operation";
                break;
            default:
                message = "unknown";
        }
        return message;
    }
        }



