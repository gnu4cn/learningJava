# 获取GUI，一个甚为形象的故事

## Notes

- `listener interface` -- 是收听者（listener）和事件源（event source, 窗口/GUI部件，window/widget）之间的桥梁

Swing的GUI部件，就是事件源。在Java语境下，事件源（event source）是一个可将用户操作（鼠标点击、键盘击键、关闭窗口等），转变成事件的对象。就如同 Java中所有东西一样，事件也是由对象表示的，是某些事件类的一个对象。在查看了 Swing API的 `java.awt.event` 包的代码之后，就会发现那里有着一大批的事件类（他们很容易分辨，因为他们的名称中都有`event`）。你会找到 `MouseEvent`、`KeyEvent`、`WindowEvent`、`ActionEvent`以及其他一些事件类。

某个事件 _源_ （_`source`_，比如某个按钮）在用户执行某些有关操作（比如 _点击_  该按钮）时，将创建出一个 _事件对象_ （_event object_）。你所编写的大部分代码（以及本书<`Head First Java`>中的全部代码），都是 _接收_ （_recieve_） 事件，而不会去 _创建_ （_create_） 事件。也就是说，你的大多数时间，都是作为一个事件收听者（_listener_），而不是作为事件源（_source_）。
