package com.xfoss.BeatBox;

import javax.sound.midi.*;

public class MusicTest1 {
    public void play () {
        try {
            Sequencer seq = MidiSystem.getSequencer();
            System.out.println("我们就得到了一个‘音序器（Sequencer）’");
        } catch (MidiUnavailableException ex) {
            System.out.println("Bummer");
        }
    }

    public static void main(String [] args) {
        MusicTest1 mt = new MusicTest1 ();
        mt.play();
    }
}
