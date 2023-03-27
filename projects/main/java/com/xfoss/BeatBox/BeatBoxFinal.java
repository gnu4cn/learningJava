package com.xfoss.BeatBox;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.event.*;

public class BeatBoxFinal extends JFrame{
    JPanel mainPanel;
    JList incomingList;
    JTextField userMessageBox;
    ArrayList<JCheckBox> checkboxList;
    int nextNum;
    Vector<String> listVector = new Vector<String>();
    String userName;
    ObjectOutputStream out;
    ObjectInputStream in;
    HashMap<String, boolean[]> otherSeqsMap = new HashMap<String, boolean[]>();

    Sequencer s;
    Sequence seq;
    Sequence mySeq = null;
    Track t;
    JLabel tempoLabel = null;

    String [] instrumentNames = {
        "贝斯鼓（低音鼓）", 
        "闭镲（闭合击镲）",
        "空心钹（开音踩钹）", 
        "小鼓（军鼓）", 
        "双面钹（强音钹）", 
        "拍手（拍掌声）",
        "高音鼓（高音桶鼓）", 
        "高音圆鼓（高音小鼓）", 
        "沙锤（沙铃）", 
        "口哨", 
        "低音手鼓",
        "牛铃（牛颈铃）", 
        "颤音叉", 
        "中低音桶鼓", 
        "高音撞铃",
        "开音高音手鼓"};

    int [] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    public static void main (String[] args) {
        // args[0] 即是用户 ID/显示名字
        //
        // 这里增加了一个作为显示名字的命令行参数。
        // 比如：%java -jar build/libs/com.xfoss.learningJava.0.0.1.jar theFlash
        new BeatBoxFinal(args[0]);
    }

    public BeatBoxFinal (String name) {
        super("赛博 BeatBox");

        userName = name;
        // 没什么新的东西......设置网络通信、I/O及构造（并启动）
        // 那个 reader 线程。
        try {
            Socket sock = new Socket("192.168.153.134", 14242);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            Thread remote = new Thread(new RemoteReader());
            remote.start();
        } catch (Exception ex) {
            System.out.println("无法连接 -- 你只能自己一个人玩了。");
            ex.printStackTrace();
        }

        setUpMidi();
        setUpGUI();
    }

    // 下面这些是 GUI 代码，没什么新东西
    private void setUpGUI () {
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

        JButton btnSerializeIt = new JButton("保存（序列化）💾");
        btnSerializeIt.addActionListener(new SavePatternListener());
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

        JButton sendItBtn = new JButton("发出🚀");
        sendItBtn.addActionListener(new SendListener());
        btnBox.add(sendItBtn);

        userMessageBox = new JTextField();
        btnBox.add(userMessageBox);

        // JList 是个之前不曾使用过的GUI部件。这正是传入消息得以显示
        // 的地方。与一般聊天那种只是查看传入消息所不同，在这个app中
        // 是可以从传入消息清单中选择一条消息，来加载并演奏出所附带
        // 的节拍编排。
        incomingList = new JList();
        incomingList.addListSelectionListener(new IncomingListSelectionListener());
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane theList = new JScrollPane(incomingList);
        btnBox.add(theList);

        incomingList.setListData(listVector);

        // 这以后就没什么新东西了。
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

        setBounds(50, 50, 640, 480);
        pack();
        setVisible(true);
    }

    // 这就是那个线程作业 -- 从服务器读入数据。在该代码中，“数据”
    // 将始终是两个序列化的对象：字符串的消息以及节拍编排（一个那些
    // 勾选框状态值的 ArrayList）
    //
    // 在消息传入进来时，这里就会读取（解序列化）那两个对象（消息与
    // 那些布尔值的勾选框状态的 ArrayList 对象），并把他添加到那个 JList
    // 组件。添加到 JList 是通过两步完成的：设置了一个该清单数据的矢量值（
    // Vector, 矢量类型就是一种老式的 ArrayList），并在随后告诉那个 JList 去
    // 使用那个矢量值，作为在那个清单中显式内容的源。
    //
    // When a message comes in, we read(deserialize) the two objects(the
    // message and the ArrayList of Boolean checkbox state values) and 
    // add it to the JList component. Adding to a JList is a two-step
    // thing: you keep a Vector of the lists data(Vector is an old-fashioned
    // ArrayList), and then tell the JList to use that Vector as it's source for
    // what to display in the list.
    public class RemoteReader implements Runnable {
        boolean[] checkboxState = null;
        String nameToShow = null;
        Object obj = null;
        public void run() {
            try {
                while((obj=in.readObject()) != null) {
                    System.out.format("已从服务器获取到一个对象\n%s\n", obj.getClass());
                    String nameToShow = (String) obj;
                    checkboxState = (boolean[]) in.readObject();
                    otherSeqsMap.put(nameToShow, checkboxState);
                    listVector.add(nameToShow);
                    incomingList.setListData(listVector);
                }
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public class PlayMineListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            if(mySeq != null) {
                // 恢复到自己原先的编排
                seq = mySeq;
            }
        }
    }

    // 全部有关 MIDI 的代码，都跟之前的版本一模一样。
    //
    // 获取音序器，构造一个音序，还构造了一个音轨
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

        // 通过遍历这些勾选框而获取到他们的状态，并将这些状态
        // 映射到某种乐器（还构造了该乐器的 MidiEvent），从而
        // 构造出一个音轨。此操作相当复杂，不过也就只是在之前的
        // 章节中那样，因此请参考之前的代码厨房，以再度获得完整
        // 的说明。
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

    // 以下是一些 GUI 的事件收听器。这些与先前章中的版本一致。
    //
    // 这几个GUI事件收听器是新加入的。
    //
    // 这也是个新的东西......这是一个新的 ListSelectionListener 事件收听器，在
    // 用户在消息清单上做出了一个选择时，该事件就会通知我们。在用户选中了一条
    // 消息时，这里就会立即加载与该消息相关联的节拍编排（在一个名为 otherSeqsMap 的
    // HashMap中），并开始演奏这个节拍编排。由于在获取 ListSelectionEvent 事件
    // 时存在一些古怪的事情，因此这里有多个 if 条件测试。
    class IncomingListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent ev){
            if(!ev.getValueIsAdjusting()){
                String selected = (String) incomingList.getSelectedValue();
                if(selected != null) {
                    // 现在去到乐器图谱，并修改其音序
                    boolean[] selectedState = (boolean[]) otherSeqsMap.get(selected);
                    changeSequence(selectedState);
                    s.stop();
                    buildTrackAndStart();
                }
            }
        }
    }

    // 在用户选择了传入消息清单中的某条消息时，便会调用此方法。这里会
    // 立即将编排修改为用户选中的那个编排。
    public void changeSequence(boolean[] state){
        for (int i = 0; i < 256; i++) {
            JCheckBox check = (JCheckBox) checkboxList.get(i);
            if (state[i]) check.setSelected(true);
            else check.setSelected(false);
        }
    }

    class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            buildTrackAndStart();
        }
    }

    // 这是个新的事件收听器......这里与发送一条字符串消息不同，是
    // 将两个对象（字符串消息与节拍编排对象）进行序列化，并将这两
    // 个对象写到那个套接字输出流（到服务器的）。
    class SendListener implements ActionListener {
        public void actionPerformed(ActionEvent ev){
            // 构造一个只保存那些勾选框状态的 ArrayList
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                JCheckBox check = (JCheckBox) checkboxList.get(i);
                if(check.isSelected()) checkboxState[i] = true;
            }

            try {
                out.writeObject(String.format("%s%d: %s", userName, nextNum, userMessageBox.getText()));
                out.writeObject(checkboxState);
            } catch(Exception ex) {
                System.out.println("抱歉兄弟。无法将其发送到服务器。");
                ex.printStackTrace();
            }
            userMessageBox.setText("");
        }
    }

    class SavePatternListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            boolean[] checkboxesState = new boolean[256];

            for (int i = 0; i < 256; i++){
                JCheckBox check = (JCheckBox) checkboxList.get(i);

                if (check.isSelected()) checkboxesState[i] = true;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(BeatBoxFinal.this);
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
            fileChooser.showOpenDialog(BeatBoxFinal.this);

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
