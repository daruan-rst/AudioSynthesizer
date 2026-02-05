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


public class Oscilator extends SynthControlContainer {

    private static final int TONE_OFFSET_LIMIT = 2000;
    private Wavetable wavetable = Wavetable.SINE;
    private int wavetableStepSize;
    private int wavetablIndex;
    private double keyFrequency;

    @Getter
    private int toneOffset;


    public void setKeyFrequency(double frequency){
        keyFrequency = frequency;
        applyToneOffset();
    }

    private int wavePos;

    public Oscilator(Synthesizer synth){
        super(synth);
        JComboBox<Wavetable> comboBox = new JComboBox<>(Wavetable.values());
        comboBox.setSelectedItem(Wavetable.SINE);
        comboBox.setBounds(10,10,75,25);
        comboBox.addItemListener(l ->
        {
        if(l.getStateChange() == ItemEvent.SELECTED){
            wavetable = (Wavetable) l.getItem();
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
                    applyToneOffset();
                    toneParameter.setText("x" + String.format("%.3f",(getToneOffset()/1000d)));
                }
                ParameterHandling.PARAMETER_ROBOT.mouseMove(mouseClickLocation.x, mouseClickLocation.y);
                System.out.println(toneOffset);
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


    public double nextSample() {

        double sample = wavetable.getSamples()[wavetablIndex];
        wavetablIndex = (wavetablIndex + wavetableStepSize) % Wavetable.SIZE;
        return sample;

    }

    private void applyToneOffset(){
        wavetableStepSize = (int) (Wavetable.SIZE * (keyFrequency * Math.pow(2, getToneOffset()))/ AudioInfo.SAMPLE_RATE);

    }
}
