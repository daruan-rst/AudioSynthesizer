package com.audio.synth;

import com.audio.synth.utilities.MathMethods;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class Synthesizer {

    private boolean shouldGenerate;

    private static final HashMap<Character, Double> KEY_FREQUENCIES= new HashMap<>();

    private final JFrame frame = new JFrame("Synthesizer");


    private final Oscilator[] oscilators = new Oscilator[3];
    private final AudioThread audioThread = new AudioThread(()->
    {
        if(!shouldGenerate){
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for (int i= 0 ; i<AudioThread.BUFFER_SIZE ; i++){
            double d = 0;
            for (Oscilator o : oscilators){
                d+= o.nextSample()/ oscilators.length;
            }
            s[i] = (short)(Short.MAX_VALUE * d);
        }
        return s;
    }

    );

    @Getter
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if(!audioThread.isRunning()){
                for (Oscilator o: oscilators){
                    //set frequency of o based on the key event
                }
                shouldGenerate = true;
                audioThread.triggerPlayback();
            }
        }


        @Override
        public void keyReleased(KeyEvent e) {
            shouldGenerate = false;
        }
    };

    static{

        final int STARTING_KEY = 16;
        final int KEY_FREQUENCY_INCREMENT = 2;
        final char[] KEYS = "zxcvbnm,.asdfghjklqwertyuiop[]".toCharArray();
        for (int i = STARTING_KEY, key = 0 ; i<KEYS.length*KEY_FREQUENCY_INCREMENT +STARTING_KEY; i +=KEY_FREQUENCY_INCREMENT, key++ ){
            KEY_FREQUENCIES.put(KEYS[key], MathMethods.getKeyFrequency(i));
        }

    }


    Synthesizer(){
        int y = 0;
        for (int i = 0 ; i<oscilators.length ; i++){
            oscilators[i] = new Oscilator(this);
            oscilators[i].setLocation(5,y);
            frame.add(oscilators[i]);
            y += 105;
        }

        frame.addKeyListener(keyAdapter);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                audioThread.close();
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(613,357);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
