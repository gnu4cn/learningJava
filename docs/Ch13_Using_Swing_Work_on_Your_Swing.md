# 运用 `Swing`: 在 `Swing` 上工作

***Using `Swing`: Work on Your `Swing`*

![运用 `Swing`: 在 `Swing` 上做事：题图](images/Ch13_01.png)

*图 1 - 运用 `Swing`: 在 `Swing` 上做事：题图*

**`Swing` 很简单**。除非真的关心物件最终在屏幕上所处位置（**`Swing` is easy**. Unless you actually *care* where things end up on the screen）。`Swing` 代码 *看起来* 不难，但在对其进行编译、运行，最后看到其效果时，就会想，“嘿，那东西可不应该在那儿呀。” 正是那些让 `Swing` 代码写起来容易的因素，使得`Swing`难于掌控 -- 那就是**布局管理器（Layout Manager）**。布局管理器的那些对象，控制着 Java GUI 中众多小部件的大小与位置。布局管理器替我们干了许许多多的事情，然而结果却并不会总是让我们满意。在想要两同样大小的按钮时，得到的两个按钮并不一样大。在希望文本字段的长度为三英寸时，会得到九英寸长的文本字段。要不就只有一英寸长。希望这个文本字段在标签部件旁边，得到的却是在便签部件下边。但只需稍加努力，就可以让布局管理器服从咱们的意愿。在这一章中，就要在 `Swing` 上下功夫，且除了布局管理器外，还会学到更多的 GUI 小部件。这里会构造处他们，让后把他们显示出来（在选定的区域），进而在程序中运用他们。

## 关于`Swing` 的那些组件

**`Swing` components**

对于先前一直讲的小部件（*widget*），其实更准确的叫法应该是***组件（Component）***。就是那些放到GUI中的 *物件（things）*。也即是 *用户所见到并与之交互的那些东西*。文本字段、按钮、滚动清单、单选按钮等等，这些全都是组件。事实上他们都扩展了 `javax.swing.JComponent`类。

> **小部件从技术上讲就是 `Swing` 组件。几乎所有可吸附在 GUI 中的东西，都扩展自 `javax.swing.JComponent` 类**。

### 组件可以嵌套

**Components can be nested**

在 `Swing` 中，差不多 *所有* 组件都具备驻留其他组件的能力（In `Swing`, virtually *all* components are capable of holding other components）。也就是说，*几近能够把任意组件都吸附到其他任意组件中*。然而在大多数时候，都只会把 *用户界面* 的那些组件，比如按钮与清单，添加到一些 *背景* 组件，比如视窗框及面板等中去。不过将比如面板放到按钮中，也是 *有可能* 的，这只是有些奇怪，同时也不会带来任何可用性上的增强。

除开 `JFrame` 这个例外，其他的 *交互性（interactive）* 组件与 *背景（background）*  组件之间的区别，就是有意为之的了。而比如说对于 `JPanel`，则常常被用作目的为组织其他组件的背景，但即便是 `JPanel`，仍然可以是交互性的。就跟其他组件一样，也可以对`JPanel`的那些事件，比如鼠标点击和键盘按键等进行注册。

**构造GUI的四个步骤（复习）**：

1) 构造一个视窗（一个 `JFrame`）

```java
JFrame frame = new JFrame("新视窗");
```

2) 构造一个组件（按钮、文本字段等等）

```java
JButton button = new JButton("点我");
```

3) 将这个组件添加到这个视窗框

```java
frame.getContentPane().add(BorderLayout.EAST, button);
```

4) 把他显示出来（给到视窗框一个大小，并令其可见）

```java
frame.setSize(300, 300);
frame.setVisible(true);
```
