# 获取GUI，一个甚为形象的故事

> 我听说你前任只会做命令行的饭（I heard your ex-wife could only cook command-line meals）？
>
> 喔！这可真是棒极了。呈现真是至关重要啊（Wow! This looks great. I guess presentation really is everything）。

**就承认了吧，你得构造 GUIs 的 App**。在构建的 app 会被其他人用到时，那么就需要图形界面。在构建自己要用的App时，也会想要个图形界面。就算余生都将编写服务器端代码，客户端界面全是web页面，也会早晚会编写一些工具，那个时候也会用到图形界面。当然，命令行的 apps 是复古的，但却并不是一种良好的形式。命令行App功能很弱、不够灵活，还不友好。后续会用两章篇幅，来学习图形用户界面，并随之而了解包括 **事件处理（Event Handling）** 及 **内联类（Inner Classes）** 等 Java 语言的关键特性。这一章中，会将按钮放置在屏幕上，并在点击这个按钮时，让按钮完成一些事情。然后会在屏幕上进行绘制，显示一张 `jpeg` 的图片出来，甚至还会制作一些动画。


## 全都是从一个视窗开始

**It all starts with a `windows`**


> **如果再让我看到一个命令行的 app, 那么你就滚蛋吧**。
>
> **If I see one more commanl-line app, you're fired**.

`JFrame` 是表示屏幕上视窗的对象。在那里就可以放上诸如按钮、复选框、文本字段等的用户界面元素。真的还可以有个用起来不错、带一些菜单项的菜单栏。不管在哪个平台，视窗都会有那些小小的视窗图标，用于视窗的最小化、最大化及关闭。

根据所在平台的不同，`JFrame`就会有不同的样子。下面是在 Mac OS X 上某个 `JFrame`的样子：

![带有一个菜单栏与两个“小部件”：一个按钮及一个单选按钮的 `JFrame`](images/Ch12_01.png)


*图 1 - 带有一个菜单栏与两个“小部件”：一个按钮及一个单选按钮的 `JFrame`*

> A `JFrame` with a menu bar and two 'widgets' (a button and a radio button)

### 把小部件放进视窗中

在有了一个 `JFrame` 之后，通过把物件（小部件，“widgets”）添加到这个 `JFrame`，这样就把这些物件放进了这个`JFrame`所表示的视窗中了。可以添加到`JFrame`的 `Swing` 组件相当多；在 `javax.swing`包中可以找到他们。最常用部件包括 `JButton`、`JRadioButton`、`JCheckBox`、`JLabel`、`JList`、`JScrollPane`、`JSlider`、`JTextArea`、`JTextField`，以及`JTable`等。其中大部分使用起来都很简单，但有些（比如 `JTable`）用起来就有点复杂。


### 构造一个GUI不难：

1) 构造一个帧（一个 `JFrame`）:


```java
JFrame frame = new JFrame();
```

2) 构造一个小部件（widget, 如按钮、文本字段等等）：

```java
JButton button = new JButton("点我");
```

3) 把这个小部件添加到帧

```java
// 并没有 直接 把物件添加到帧。可把帧当作是视窗周围的
// 窗框（the trim around the window），而把物件放在窗玻璃上的
frame.getContentPane().add(button);
```

4) 显示这个帧（给予其一个尺寸并令其可见）

```java
frame.setSize(300, 300);
frame.setVisible(true);
```

### 第一个GUI：在帧上的一个按钮


```java
// 不要忘了导入这个 swing 包（这里的 ‘x’ 表示 'extension'）
import javax.swing.*;

public class SimpleGui1 {
    public static void main (String [] args) {
        // 构造一个帧与按钮（可把希望在按钮上显示的文本，传递
        // 给按钮的构造器）
        JFrame frame = new JFrame();
        JButton button = new JButton("点击我");

        // 这行语句使得程序可在关闭视窗时，尽快退出（若没有
        // 这行语句，那么程序就会一直在屏幕上）
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 将上面构造的按钮，添加到帧的窗格
        frame.getContentPane().add(button);

        // 给予该帧一个尺寸，是以像素为单位的
        frame.setSize(300, 300);

        // 最后，让该帧可见！！！（若忘记这一步，那么在运行
        // 这段代码时什么也看不到。）
        frame.setVisible(true);
    }
}
```

### 不过在点击这个按钮时，什么也没发生......

准确来说这样讲有点背离事实。在按下按钮时，按钮会展示“被按下”或“按进去”的样子（具体看起来怎样，跟平台的外观和感受有关，但不论在何种平台，在按钮被按下时，都会发生一些改变）。

然而真正的问题在于，“怎样才能让按钮在用户按下时，去执行某些特定操作（How do I get the button to do something specific when the user clicks it）?”。


**这里就需要两个东西**：

1) 在用户点击时，要调用到的 **方法** （即作为按钮点击结果的、所想要的东西）。

2) 一种 **获悉** 何时触发这个方法的机制。也就是说，一种获悉用户在什么时候点击了按钮的方式！

![GUI 编程关注点：小部件上的事件](images/Ch12_02.png)

*图 2 - GUI 编程关注点：小部件上的事件*


## 答疑

- **在Windows 上运行 Java GUI程序时，按钮看起来会像个 Windows 的按钮吗**？

> 想要就可以。可从少数几种“外观及体验” -- 对应核心库中控制用户界面式样的几个类中加以选择。多数情况下，可以在至少两种外观之间加以选择：一种是被称作 ***`Metal`*** 的Java 标准的、同时也是平台的原生外观与体验；而这本书中使用的 Mac OS X 截屏，有的是 OS X 的 ***`Aqua`*** 主题，有的则是 ***`Metal`*** 外观与体验。


- **可以构造一个总是 Aqua 主题的程序吗？即便是运行在Windows下也要一直是Aqua主题的**。

> 注：Aqua 是苹果公司 MacOS 的主题外观。参见：[Aqua(GUI)](https://zh.wikipedia.org/wiki/Aqua_(GUI))

> 不可以。并不是在每个平台上全部外观与体验都是可行的。为了保持安全，那么就应该把外观和体验都设置为 ***`Metal`***，这样不管 app 运行在哪里，都能准确知道可以得到什么外观与体验，否则就干脆不指定外观与体验，而接受默认的样子。

- **听说 `Swing` 慢如蜗牛，没有人用这玩意儿**。

> 以前或许是这样，现在可不是这样了。在性能差的机器上，或许会感受到 `Swing` 的痛点。但只要是一台不那么老旧的机器，在使用 Java 版本1.3及以后的版本的情况下，甚至感受不到 `Swing` GUI 与原生GUI的有什么不同。时至今日，`Swing`在所有类别的app中都有重度使用。 


## 获取用户事件

**Getting a user event**

设想打算在用户按下按钮时，按钮上的文字由 *点击我* 改编为 *我已被点击*。首先可以编写一个修改按钮文本的方法（快速参考 API 文档就会发现修改按钮文本的方法`setText()`）:

```java
public void changeIt () {
    button.setText("我已被点击");
}
```

然而接下来呢？怎么知道这个方法应该什么时候运行呢？*怎么知道按钮在什么时候被点击的呢*？

在 Java 中，用户事件的获取与处理的过程，叫做 *事件处理（event-handling）*。尽管Java中有许多不同的事件类型，但绝大部分都涉及到 GUI 下的用户操作。在用户点击了某个按钮时，那就是一个事件。一个讲说“用户希望该按钮相对应的动作发生”的事件。假如按钮是个“慢节奏”按钮，那么用户就希望出现一个慢节奏的操作（An event that says "The user wants the action of this button to happen." If it's a "Slow Tempo" button, the user wants the slow-tempo action to occur）。又假如是个聊天客户端中的 “发送” 按钮，那么用户就希望出现一个“发送我的消息”的动作。因此最直接的事件就是，在用户点击按钮时，去表明他们是想要某个动作发生。

对于这些按钮，通常无需关心诸如 `button-is-being-pressed`及`button-is-being-released`这样的中间事件。想对按钮说的，实际上是“我不介意用户如何操作按钮，以及用户把鼠标悬停在按钮上多长时间，也不关心用户在按下按钮前改变了多少次注意和移开了多少次鼠标光标等等，***只要告诉我什么时候用户拿定主意就行***！也就是说，除了在用户点击按钮，表示他希望这个该死的按钮去干按钮要干的事情外，都不要说什么！”

**首先，按钮需要知道外界关心什么**

**First, the button needs to know what we care**.

![Java中GUI事件处理](images/Ch12_03.png)


*图 3 - Java中GUI事件处理*


**其次，按钮（GUI组件）需要某种在按钮点击事件发生时，呼回我们的方式（Second, the button needs a way to call us back when a button-clicked event occurs）**。

### 脑力大挑战

1) 到底怎样去通知某个按钮对象，你对其事件的关注？表明你是一个攸关的（事件）收听方（How could you tell a button object that you care about its events? That you're a concerned listener）？

2) 假设没有路径可以告知按钮那个独特方法名字（即`changeIt()`）的情况下，按钮又是在事件发生后怎样呼回的？那么还可以运用什么其他的东西来告知按钮，在事件发生时可以调用我们的特定方法吗？【提示：想想 `Pet` 类】（How will the button call you back? Assume that there's no way for you to tell the button the name of your unique method(`changeIt()`). So what else can we use to reassure the button that we have a specific method it can call when the event happens? [hint: think `Pet`]）


** 在关心那个按钮的事件时，就去 *实现一个`interface`*（`JButton`类的），以表明 “正在收听你的事件” **

> 注：可以看着是注册一个 GUI 组件的事件收听者。

**收听者 `interface`** 是 **收听者（`listener`）** （你）与 **事件源（event source）** （按钮） 之间的桥梁。

## 笔记

* `listener interface` -- 是收听者（listener）和事件源（event source, 窗口/GUI部件，window/widget）之间的桥梁
* 往GUI上放东西有三种方式：把小部件放在帧上，在小部件上绘制2D图形，以及在小部件上放置图片。

## `event listener`、`event source` 与 `listener interface`

将事件处理器绑定到事件源的事件上。

Swing的GUI部件，就是事件源。在Java语境下，事件源（event source）是一个可将用户操作（鼠标点击、键盘击键、关闭窗口等），转变成事件的对象。就如同 Java中所有东西一样，事件也是由对象表示的，是某些事件类的一个对象。在查看了 Swing API的 `java.awt.event` 包的代码之后，就会发现那里有着一大批的事件类（他们很容易分辨，因为他们的名称中都有`event`）。你会找到 `MouseEvent`、`KeyEvent`、`WindowEvent`、`ActionEvent`以及其他一些事件类。

某个事件 _源_ （_`source`_，比如某个按钮）在用户执行某些有关操作（比如 _点击_  该按钮）时，将创建出一个 _事件对象_ （_event object_）。你所编写的大部分代码（以及本书<`Head First Java`>中的全部代码），都是 _接收_ （_recieve_） 事件，而不会去 _创建_ （_create_） 事件。也就是说，你的大多数时间，都是作为一个事件收听者（_listener_），而不是作为事件源（_source_）。

所有的 _事件类型_，都有其对应的 _收听者接口_ （Every event type has a matching listener interface）。在希望得到 `MouseEvent` 时，就需要部署一个 `MouseListener` 接口。想要一个 `WindowEvent`？那就部署一个 `WindowListener` 接口。道理就是这样的。同时要记住 __接口规则__ （`interface rules`） -- 在部署某个你所 __声明__ 的接口时，就要去实现他（比如 `class Dog implements Pet`），也就是说，你必须给接口中的所有方法， __编写实现方法__ （to implement an interface you __declare__ that you implement it <class Dod implement Pet>, which means you must __write implementation methods__ for every method in the interface）。

某些接口有着不止一个的方法，因为事件本身可能有着差异。比如在实现 `MouseListener`时，就会得到 `mousePressed`、`mouseReleased`、`mouseMoved`等事件类型，尽管这些鼠标事件都带有一个`MouseEvent`，但在在事件收听接口中，所有这些鼠标事件都有各自的方法。在部署了 `MouseListener` 接口时，那么在用户按下鼠标时，就会调用到 `mousePressed`方法。因此对于鼠标事件，就只有一个事件 _对象_ （_object_），`MouseEvent`，但却有着不同的事件 _方法_ （_methods_），以表示不同的鼠标事件 __类型__ （_types_）。
