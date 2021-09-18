package com.audio.synth;

import javax.swing.*;

import com.audio.synth.utilities.MathMethods;
import com.audio.synth.utilities.ParameterHandling;
import com.audio.synth.utilities.WindowDesign;
import lombok.Getter;


import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Oscilator extends SynthControlContainer {

    private static final int TONE_OFFSET_LIMIT = 2000;

    private Waveform waveform = Waveform.SINE;

    private final Random random = new Random();

    private double keyFrequency;

    @Getter
    private int toneOffset;

    @Getter
    private double frequency;

    public void setFrequency(double frequency){
        keyFrequency = this.frequency + frequency;
        //apply tone offset
    }

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
        JLabel toneParameter =  new JLabel("x0.00");
        toneParameter.setBounds(165,65,50,25);
        toneParameter.setBorder(WindowDesign.LINE_BORDER);
        toneParameter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final Cursor BLANK_CURSOR = Toolkit.getDefaultToolkit()
                        .createCustomCursor(new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB),
                                new Point(0,0), "blank_cursor");
                setCursor(BLANK_CURSOR);
                mouseClickLocation = e.getLocationOnScreen();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setCursor(Cursor.getDefaultCursor());
            }
        });
        toneParameter.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseClickLocation.y != e.getY()){
                    boolean mouseMovingUp = mouseClickLocation.y - e.getYOnScreen() > 0;
                    if (mouseMovingUp && toneOffset < TONE_OFFSET_LIMIT){
                        ++toneOffset;
                    }else if(!mouseMovingUp && toneOffset > -TONE_OFFSET_LIMIT){
                        --toneOffset;
                    }
                    //apply tone offset
                    toneParameter.setText("x" + String.format("%.3f",(getToneOffset()/1000d)));
                }
                ParameterHandling.PARAMETER_ROBOT.mouseMove(mouseClickLocation.x, mouseClickLocation.y);
            }
        });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172,40,75,25);
        add(toneText);
        setSize(279, 100);
        setBorder(WindowDesign.LINE_BORDER);
        setLayout(null);

    }





    public double nextSample(){
        double tDivP = (wavePos++/(double)AudioInfo.SAMPLE_RATE) / (1/ frequency);
        switch (waveform){
            case SINE:
                return Math.sin(MathMethods.frequencyToAngular(frequency) * (wavePos-1) / AudioInfo.SAMPLE_RATE);
            case SQUARE:
                return Math.signum(Math.sin(MathMethods.frequencyToAngular(frequency) * (wavePos-1) / AudioInfo.SAMPLE_RATE));
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
