# 获取GUI，一个甚为形象的故事

## 笔记

- `listener interface` -- 是收听者（listener）和事件源（event source, 窗口/GUI部件，window/widget）之间的桥梁
- 往GUI上放东西有三种方式：把小部件放在帧上，在小部件上绘制2D图形，以及在小部件上放置图片。

## `event listener`、`event source` 与 `listener interface`

将事件处理器绑定到事件源的事件上。

Swing的GUI部件，就是事件源。在Java语境下，事件源（event source）是一个可将用户操作（鼠标点击、键盘击键、关闭窗口等），转变成事件的对象。就如同 Java中所有东西一样，事件也是由对象表示的，是某些事件类的一个对象。在查看了 Swing API的 `java.awt.event` 包的代码之后，就会发现那里有着一大批的事件类（他们很容易分辨，因为他们的名称中都有`event`）。你会找到 `MouseEvent`、`KeyEvent`、`WindowEvent`、`ActionEvent`以及其他一些事件类。

某个事件 _源_ （_`source`_，比如某个按钮）在用户执行某些有关操作（比如 _点击_  该按钮）时，将创建出一个 _事件对象_ （_event object_）。你所编写的大部分代码（以及本书<`Head First Java`>中的全部代码），都是 _接收_ （_recieve_） 事件，而不会去 _创建_ （_create_） 事件。也就是说，你的大多数时间，都是作为一个事件收听者（_listener_），而不是作为事件源（_source_）。

所有的 _事件类型_，都有其对应的 _收听者接口_ （Every event type has a matching listener interface）。在希望得到 `MouseEvent` 时，就需要部署一个 `MouseListener` 接口。想要一个 `WindowEvent`？那就部署一个 `WindowListener` 接口。道理就是这样的。同时要记住 __接口规则__ （`interface rules`） -- 在部署某个你所 __声明__ 的接口时，就要去实现他（比如 `class Dog implements Pet`），也就是说，你必须给接口中的所有方法， __编写实现方法__ （to implement an interface you __declare__ that you implement it <class Dod implement Pet>, which means you must __write implementation methods__ for every method in the interface）。

某些接口有着不止一个的方法，因为事件本身可能有着差异。比如在实现 `MouseListener`时，就会得到 `mousePressed`、`mouseReleased`、`mouseMoved`等事件类型，尽管这些鼠标事件都带有一个`MouseEvent`，但在在事件收听接口中，所有这些鼠标事件都有各自的方法。在部署了 `MouseListener` 接口时，那么在用户按下鼠标时，就会调用到 `mousePressed`方法。因此对于鼠标事件，就只有一个事件 _对象_ （_object_），`MouseEvent`，但却有着不同的事件 _方法_ （_methods_），以表示不同的鼠标事件 __类型__ （_types_）。
