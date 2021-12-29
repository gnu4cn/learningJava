# 例外处理：风险行为

**Exception Handling: Risky Behavior**

> 那肯定有风险，但在有什么事情出错的时候，可以处理风险。

**发生了状况。文件不在那里。服务器宕机了**。不过你是多么优秀的程序员，都没办法掌控所有事情。总会有些事情会出错。甚至错得离谱。在编写有风险的方法时，就需要代码来处理可能发生的不好情况。那么怎么知道某个方法具有风险呢？同时又要把 *处理* **例外** 情形的代码放在哪里呢？到目前为止，都还没有真正面对过任何风险。在运行时确实有出错的情况，但这些问题主要是自己编写代码的缺陷。也就是代码漏洞（bugs）。这些在开发时（development time, compilation time and runtime）都可以修复。然而这一章所说的问题处理代码，指的不是之前的那种，而是在运行时无法确保可以工作的代码。比如那些期望文件就在某个目录、服务器是在运行，或者线程确实保持睡眠等等的代码。同时也必须完成对异常处理的掌握。因为就要构建一个 “MIDI 音乐播放器”了。

## 构造一个“Music Machine”的应用

在接下来的三章，会构建几个有着些许不同的声音应用，包括一个 “BeatBox Drum Machine”。实际上，在本教程完结时，就会写出一个可以把鼓节拍循环，像聊天室应用一样中聊天一样，发送给其他玩家的多玩家版本。虽然可以从GUI章节拷贝写好的程序，不过也可以马上开始编写这个应用的各个部分。

虽然不是每个IT部门都需要这样的一个 “BeatBox” 服务器，这里编写这个应用的目的是更好地掌握 Java 语言。构建的目的是在学习 Java 时可以带来些乐趣而已。

**完工后的 “BeatBox” 应用看起来像下面这样**：

![完成后的“BeatBox”应用](images/Ch11_01.png)

*图 1 - 完成后的“BeatBox”应用*

玩法是在16个“节拍”的各种乐器的复选框中打勾。比如，在节拍 1（16拍中的）时，贝斯鼓和沙锤就会演奏，节拍 2 上就什么也不会演奏，而在节拍 3 上则是沙锤和踩镲......明白了吧。点击“开始（Start）”按钮，程序就会循环播放编排的这些模式，直到点击 “停止（Stop）”停下来为止。在任何时候都可以通过将这些模式发送给 `BeatBox`服务器，对自己编排的模式加以“捕获（capture）”（这样其他玩家就可以收听到）。通过点击聊天框中的消息，还可以收听到和消息一起发出来的收到的曲目。

## 先从简单的开始

**We'll start with the basics**

显然在完成这整个的程序前，需要学习一些东西，包括怎样去构建一个 `Swing` GUI，怎样通过网络通信与另一台机器连接，还需要一个小型的 `I/O`，从而可以一些东西发送给其他机器。

当然还有 `JavaSound` 这个 API了。本章就要从这个API开始。现在先不要想 GUI的事情，忘掉网络通信和`I/O`，而是要专心获取到一些由 MIDI 产生、可以从电脑中传出来的声音。即使对 MIDI，也就是一种读取或构造音乐的技术不了解，也不用担心。

所有需要掌握的内容，这里都有讲到。几乎可以嗅到一个唱片生意的味道。

### 关于 `JavaSound` API

`JavaSound`是自 1.3 版本开始加入到 Java 中的一套类与接口。这些类与接口并非特别的附加组件；他们是标准 J2SE 类库的一部分。`JavaSound`又被分为了两个部分：`MIDI`和`Sampled`。这里只使用到 `MIDI`。`MIDI`表示“乐器数字接口（Musical Instrument Digital Interface）”，同时也是各种不同种类的电音设备得以通信的标准协议（a standard protocol for getting different kinds of electronic sound equipment to communicate）。但对于这里的 `BeatBox` app，可以把 `MIDI` 想作 *某种类型的乐谱*，可将一些装置，比如某种高科技的“演奏钢琴”，放进这个表格。也就是说，MIDI数据本身并没有包含任何的 *声音*，他保存的是由MIDI所读取的、某种乐器的、可以回放的 *指令*（MIDI data doesn't actually include any *sound*, but it does include the *instructions* that a MIDI-reading instrument can play back）。或以另一个比方说，可把某个MIDI文件当作一个HTML文档，那么乐器就会对这个MIDI文件进行渲染（也就是演奏这个MIDI文件），如同Web浏览器渲染HTML文档一样。

MIDI数据说的是要做 *什么*（奏出一个中音`C`调，还有多大力度，以及这个音有多长等等），但MIDI数据并不会描述会听到的 *声音*。MIDI是不知道怎样去发出长笛、钢琴或者 Jimi Hendrix 吉他声的。对于这些真实的声音，是需要某种可以读取和演奏MIDI文件的乐器（即MIDI设备）的。不过MIDI设备通常更像 *一整个乐队或乐团* 的很多种乐器了。这些乐器可以是实体装置，比如摇滚乐手使用的电子键盘合成器，也可以是存在于电脑种的、完全由软件构建的乐器。

对于这里的 `BeatBox`应用，只会使用到Java自带的、内建的、纯软件的乐器。这些乐器叫做 *合成器（synthesizer）* （有人也称他们为 *软件合成器（software synth）*），因为他们可以 *创建出* 声音来。就是能听到的声音。

![关于MIDI文件](images/Ch11_02.png)

*图 2 - 关于MIDI文件*


### 首先需要一个 `Sequencer`

在可以播放出一个声音出来之前，首先就需要一个 `Sequence`对象。音序器是一个可以接收全部MIDI数据并将其发送到相应乐器的对象（The sequencer is the object that takes all the MIDI data and sends it to the right instruments）。正是这个东东，负责把音乐 *播放* 出来。音序器可以完成很多不同的事情，这里只是将其作为一个回访设备使用而已。就跟立体声音响上的CD播放器一样，只不过还有一些其他附加功能特性。类 `Sequencer`是在 `javax.sound.midi`包中（这个包是自版本1.3开始的Java的标准库）。因此首先就要确保可以构造（或获取到）一个`Sequencer`对象。

```java
package com.xfoss.BeatBox;

// 这里导入了 javax.sound.midi 包
import javax.sound.midi.*;

public class MusicTest1 {
    public void play () {
        // 这里需要一个 Sequencer 对象。他是要用到的MIDI设备/乐器
        // 的主要部分。他就是把所有MIDI信息编排为一首“乐曲”的那个
        // 东东。但这里不会由我们自己去构造一个全新的 Sequencer 
        // 对象 -- 必须请求 MidiSystem 给出一个来（与之前的用到的 
        // Calendar 类中的静态方法 getInstance() 类似）
        Sequencer seq = MidiSystem.getSequencer();

        System.out.println("我们就得到了一个‘音序器（Sequencer）’");
    }

    public static void main(String [] args) {
        MusicTest1 mt = new MusicTest1 ();
        mt.play();
    }
}
```

然而在编译编译这段代码时，就会抛出错误：

```console
[ERROR] .../src/main/java/com/xfoss/BeatBox/MusicTest1.java:[7,48] unreported exception javax.sound.midi.MidiUnavailableException; must be caught or declared to be thrown
```

**显然有地方出错了**！

这段代码不会被编译！编译器说有一个必须被捕获或声明的“未报告的例外（异常）”（The compiler says there's an "unreported exception" that must be caught or declared）。

