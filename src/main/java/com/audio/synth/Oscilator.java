package com.audio.synth;

import javax.swing.*;

import com.audio.synth.utilities.MathMethods;
import com.audio.synth.utilities.WindowDesign;

import java.awt.event.ItemEvent;
import java.util.Random;


public class Oscilator extends SynthControlContainer {

    private Waveform waveform = Waveform.SINE;

    private final Random random = new Random();

    private static final double FREQ = 440;

    private int wavePos;

    public Oscilator(Synthesizer synth){
        super(synth);
        JComboBox<Waveform> comboBox = new JComboBox<>(new Waveform[]{Waveform.SINE, Waveform.SQUARE, Waveform.SAW, Waveform.TRIANGLE, Waveform.NOISE});
        comboBox.setSelectedItem(Waveform.SINE);
        comboBox.setBounds(10,10,75,25);
        comboBox.addItemListener(l ->
        {
        if(l.getStateChange() == ItemEvent.SELECTED){
            waveform = (Waveform) l.getItem();
        }

        });
        add(comboBox);
        setSize(279, 100);
        setBorder(WindowDesign.LINE_BORDER);
        setLayout(null);

    }

    public double nextSample(){
        double tDivP = (wavePos++/(double)AudioInfo.SAMPLE_RATE) / (1/FREQ);
        switch (waveform){
            case SINE:
                return Math.sin(MathMethods.frequencyToAngular(FREQ) * (wavePos-1) / AudioInfo.SAMPLE_RATE);
            case SQUARE:
                return Math.signum(Math.sin(MathMethods.frequencyToAngular(FREQ) * (wavePos-1) / AudioInfo.SAMPLE_RATE));
            case SAW:
                return 2d* (tDivP - Math.floor(0.5 + tDivP));
            case TRIANGLE:
                return 2d* Math.abs((tDivP - Math.floor(0.5 + tDivP))) - 1;
            case NOISE:
                return random.nextDouble();
            default:
                throw new RuntimeException("Oscilator set to unknown wave form");
        }

    }
}
