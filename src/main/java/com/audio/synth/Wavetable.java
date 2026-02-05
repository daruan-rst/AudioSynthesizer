package com.audio.synth;

import com.audio.synth.utilities.MathMethods;
import lombok.Getter;

enum Wavetable {
    SINE, SQUARE, SAW, TRIANGLE;

    static final int SIZE = 8192;

    @Getter
    public final float[] samples = new float[SIZE];

    static {

        final double FUND_FREQ = 1d / ( SIZE / (double) AudioInfo.SAMPLE_RATE);
        for (int i = 0; i < SIZE; i++) {
            double t =  1 / (double) AudioInfo.SAMPLE_RATE;
            double tDivP = t / (1d / FUND_FREQ );
            SINE.samples[i] = (float) Math.sin(MathMethods.frequencyToAngular(FUND_FREQ) * t);
            SQUARE.samples[i] = (float) Math.signum(SINE.samples[i]);
            SAW.samples[i] = (float) (2d * (tDivP - Math.floor(0.5 + tDivP)));
            TRIANGLE.samples[i] = (float) (2d * Math.abs(SAW.samples[i]) - 1d);
        }
    }

}
