package com.xfoss.learningJava;

import javax.sound.midi.*;

public class MiniMusicPlayer2 implements ControllerEventListener {
    public static void main (String[] args) {
        MiniMusicPlayer2 mini = new MiniMusicPlayer2();
        mini.go();
    }

    public void go () {
        try {
            Sequencer s = MidiSystem.getSequencer();
            s.open();

            int [] eventsIWant = {127};
            s.addControllerEventListener(this, eventsIWant);

            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track t = seq.createTrack();

            for (int i = 5; i < 60; i+=4) {
                t.add(makeEvent(144, 1, i, 100, i));
                t.add(makeEvent(176, 1, 127, 0, i));
                t.add(makeEvent(128, 1, i, 100, i + 2));
            }

            s.setSequence(seq);
            s.setTempoInBPM(220);
            s.start();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public void controlChange (ShortMessage ev) {System.out.println("la");}

    public MidiEvent makeEvent (int comd, int chan, int one, int two, int tick) {
        MidiEvent ev = null;
        
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            ev = new MidiEvent(a, tick);
        } catch (Exception e) {}

        return ev;
    }
}
