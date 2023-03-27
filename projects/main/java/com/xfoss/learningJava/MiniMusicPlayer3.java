package com.xfoss.learningJava;

import javax.sound.midi.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class MiniMusicPlayer3 {
    static JFrame f = new JFrame("Java GUI示例: 内部类与监听非 GUI 事件");
    static DrawingPanel p;
    static int fWidth = 640;
    static int fHeight = 480;

    public static void main (String[] args) {
        MiniMusicPlayer3 mini = new MiniMusicPlayer3();
        mini.go();
    }

    public void setUpGui () {
        p = new DrawingPanel();
        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(30, 30, fWidth, fHeight);
        f.setVisible(true);
    }

    public void go () {
        setUpGui();

        try {
            Sequencer s = MidiSystem.getSequencer();
            s.open();

            s.addControllerEventListener(p, new int[] {127});

            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track t = seq.createTrack();

            int r = 0;
            for (int i = 0; i < 360; i+=4) {
                r = (int) (Math.random() * 50 + 1);

                t.add(makeEvent(144, 1, r, 100, i));
                t.add(makeEvent(176, 1, 127, 0, i));
                t.add(makeEvent(128, 1, r, 100, i + 2));
            }

            s.setSequence(seq);
            s.setTempoInBPM(220);
            s.start();
        } catch (Exception ex){ex.printStackTrace();}
    }

    public MidiEvent makeEvent (int comd, int chan, int one, int two, int tick) {
        MidiEvent ev = null;
        
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            ev = new MidiEvent(a, tick);
        } catch (Exception e) {}

        return ev;
    }

    class DrawingPanel extends JPanel implements ControllerEventListener {
        boolean msg = false;

        public void controlChange(ShortMessage ev) {
            msg = true;
            repaint();
        }

        public void paintComponent(Graphics g) {
            if (msg) {
                g.setColor(Color.white);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());

                Graphics2D g2 = (Graphics2D) g;

                int r = (int) (Math.random() * 250);
                int gr = (int) (Math.random() * 250);
                int b = (int) (Math.random() * 250);

                g.setColor(new Color(r, gr, b));
                 
                int x = (int) (Math.random() * 40 + 10);
                int y = (int) (Math.random() * 40 + 10);
               
                int ht = (int)(Math.random() * fHeight / 2 + 10);
                int width = (int) (Math.random() * fWidth / 2 + 10);
                
                g.fillRect(x, y, width, ht);
                msg = false;
            }
        }
    }
}
