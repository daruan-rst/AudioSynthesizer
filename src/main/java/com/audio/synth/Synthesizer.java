package com.audio.synth;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Synthesizer {

    private boolean shouldGenerate;

    private final JFrame frame = new JFrame("Synthesizer");

    private int wavePos;
    private final AudioThread audioThread = new AudioThread(()->
    {
        if(!shouldGenerate){
            return null;
        }
        short[] s = new short[AudioThread.BUFFER_SIZE];
        for (int i= 0 ; i<AudioThread.BUFFER_SIZE ; i++){
            s[i] = (short)(Short.MAX_VALUE * Math.sin(2*Math.PI * 440 / AudioInfo.SAMPLE_RATE * wavePos));
        }
        return s;
    }

    );


    Synthesizer(){
        Oscilator o = new Oscilator();
        o.setLocation(5,0);
        frame.add(o);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!audioThread.isRunning()){
                    shouldGenerate = true;
                    audioThread.triggerPlayback();
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                shouldGenerate = false;
            }
        });
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
