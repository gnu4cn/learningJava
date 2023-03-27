package com.xfoss.BeatBox;

import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class BeatBox extends JFrame{
    JPanel mainPanel;
    ArrayList<JCheckBox> checkboxList;
    Sequencer s;
    Sequence seq;
    Track t;
    JLabel tempoLabel = null;

    String [] instrumentNames = {"贝斯鼓（低音鼓）", "闭镲（闭合击镲）",
        "空心钹（开音踩钹）", "小鼓（军鼓）", "双面钹（强音钹）", "拍手（拍掌声）",
        "高音鼓（高音桶鼓）", "高音圆鼓（高音小鼓）", "沙锤（沙铃）", "口哨", "低音手鼓",
        "牛铃（牛颈铃）", "颤音叉", "中低音桶鼓", "高音撞铃",
        "开音高音手鼓"};

    int [] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    public static void main (String[] args) {
        new BeatBox();
    }

    public BeatBox () {
        super("赛博 BeatBox");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL icoURI = getClass().getResource("/images/ico.png");
        ImageIcon ico = new ImageIcon(icoURI);
        setIconImage(ico.getImage());

        BorderLayout l = new BorderLayout();
        JPanel bg = new JPanel(l);
        bg.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        checkboxList = new ArrayList<JCheckBox> ();
        Box btnBox = new Box(BoxLayout.Y_AXIS);

        JButton btnS = new JButton("开始▶");
        btnS.addActionListener(new StartListener());
        btnBox.add(btnS);

        JButton btnStop = new JButton("停止◾");
        btnStop.addActionListener(new StopListener());
        btnBox.add(btnStop);

        JButton btnReset = new JButton("重置🔄");
        btnReset.addActionListener(new ResetListener());
        btnBox.add(btnReset);

        JButton btnSerializeIt = new JButton("将其序列化💾");
        btnSerializeIt.addActionListener(new SendPatternListener());
        btnBox.add(btnSerializeIt);

        JButton btnRestore = new JButton("恢复🔙");
        btnRestore.addActionListener(new ReadInPatternListener());
        btnBox.add(btnRestore);

        btnBox.add(Box.createHorizontalStrut(1));
        btnBox.add(new JSeparator(SwingConstants.HORIZONTAL));

        JButton btnUpTempo = new JButton("加速>>");
        btnUpTempo.addActionListener(new UpTempoListener());
        btnBox.add(btnUpTempo);

        JButton btnDownTempo = new JButton("减慢<<");
        btnDownTempo.addActionListener(new DownTempoListener());
        btnBox.add(btnDownTempo);

        tempoLabel = new JLabel(String.format("速度因子：%.2f", 1.00f)); 
        btnBox.add(tempoLabel);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }

        bg.add(BorderLayout.EAST, btnBox);
        bg.add(BorderLayout.WEST, nameBox);

        getContentPane().add(bg);

        GridLayout g = new GridLayout(16, 16);
        g.setVgap(1);
        g.setHgap(2);
        mainPanel = new JPanel(g);
        bg.add(BorderLayout.CENTER, mainPanel);

        for (int i = 0; i < 256; i++) {
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkboxList.add(c);
            mainPanel.add(c);
        }

        setUpMidi();

        setBounds(50, 50, 640, 480);
        pack();
        setVisible(true);
    }

    public void setUpMidi () {
        try {
            s = MidiSystem.getSequencer();
            s.open();
            seq = new Sequence(Sequence.PPQ, 4);
            t = seq.createTrack();
            s.setTempoInBPM(120);
        } catch (Exception e) {e.printStackTrace();}
    }

    public void buildTrackAndStart () {
        int [] trackList = null;

        seq.deleteTrack(t);
        t = seq.createTrack();

        for (int i = 0; i < 16; i++){
            trackList = new int[16];

            int key = instruments[i];

            for (int j = 0; j < 16; j++) {
                JCheckBox jc = checkboxList.get(j + 16*i);
                if (jc.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }

            makeTracks(trackList);
            t.add(makeEvent(176, 1, 127, 0, 16));
        }

        t.add(makeEvent(192, 9, 1, 0, 15));
        try {
            s.setSequence(seq);
            s.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            s.start();
            s.setTempoInBPM(120);
        } catch(Exception e) {e.printStackTrace();}
    }

    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            buildTrackAndStart();
        }
    }

    class SendPatternListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            boolean[] checkboxesState = new boolean[256];

            for (int i = 0; i < 256; i++){
                JCheckBox check = (JCheckBox) checkboxList.get(i);

                if (check.isSelected()) checkboxesState[i] = true;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(BeatBox.this);
            File fileChoice = fileChooser.getSelectedFile();

            if (fileChoice != null) {
                try {
                    FileOutputStream fileStream = new FileOutputStream(fileChoice);
                    ObjectOutputStream os = new ObjectOutputStream(fileStream);
                    os.writeObject(checkboxesState);
                    os.close();
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class ReadInPatternListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            boolean[] checkboxesState = null;

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(BeatBox.this);

            File fileSelected = fileChooser.getSelectedFile();

            if (fileSelected != null) {
                try {
                    FileInputStream fileIn = new FileInputStream(fileSelected);
                    ObjectInputStream is = new ObjectInputStream(fileIn);
                    checkboxesState = (boolean[]) is.readObject();
                    is.close();
                } catch (Exception ex) {
                    System.out.println("打开编曲出错");
                    ex.printStackTrace();
                }
            }

            // 这里有可能未选择文件，而导致checkboxesState 为 null
            if (checkboxesState != null) {
                for (int i = 0; i < 256; i++) {
                    JCheckBox check = (JCheckBox) checkboxList.get(i);
                    if(checkboxesState[i]) check.setSelected(true);
                    else check.setSelected(false);
                }

                s.stop();
                buildTrackAndStart();
            }
        }
    }

    class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            s.stop();
        }
    }

    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            for(int i = 0; i < 256; i++) {
                checkboxList.get(i).setSelected(false);
            }
        }
    }

    class UpTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            s.setTempoFactor(s.getTempoFactor() + 0.03f);
            tempoLabel.setText(String.format("速度因子：%.2f", s.getTempoFactor()));
        }
    }

    class DownTempoListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            s.setTempoFactor(s.getTempoFactor() - 0.03f);
            tempoLabel.setText(String.format("速度因子：%.2f", s.getTempoFactor()));
        }
    }

    public void makeTracks(int [] list) {
        for(int i = 0; i < 16; i++) {
            int k = list[i];

            if(k != 0) {
                t.add(makeEvent(144, 9, k, 100, i));
                t.add(makeEvent(128, 8, k, 100, i+1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
        MidiEvent ev = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            ev = new MidiEvent(a, tick);
        } catch (Exception e) {e.printStackTrace();}
        return ev;
    }
}
