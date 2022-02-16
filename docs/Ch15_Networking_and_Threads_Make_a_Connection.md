# 联网与线程：构造连接

**Networking and threads: Make a Connection**

**要与外部世界连通**。Java程序是能够触及另一台机器上程序的。而且不难触及。`java.net`库中的众多类，负责全部低级别网络通信细节。采用 Java 的众多益处之一，就是透过网络来发送和接收数据，就跟普通 `I/O` 操作一样，些许不同之处在于，位于 `I/O` 操作链末端有着不同的连接性流。在获得了一个 `BufferedReader`后，就可以 *读取* 数据了。而 `BufferedReader`是不会在乎数据是从文件还是从以太网线上来的。本章将使用套接字来连接到外界（And the `BufferedReader` couldn't care less if the data came out of a file or flew down an ethernet cable. In this chapter we'll connect to the outside world with sockets）。这里会构造 *客户端* 套接字。也会构造 *服务器* 套接字。这里会构造 *客户端* 与 *服务器*。同时还会让二者互相对话。在本章完成之前，就会有一个功能完整、多线程的聊天客户端。对了，这里提到了 *多线程*，那么就 *即将* 学到怎样在与 Bob 对话的同时，还要听 Suzy 讲话的诀窍。

## 实时 `BeatBox` 聊天室应用

**Real-time `BeatBox` Chat**

![具备网络通信与线程特性之后的 `BeatBox` 应用](images/Ch15_01.png)


*图 1 - 具备网络通信与线程特性之后的 `BeatBox` 应用*

现在是在计算机游戏上工作了。玩家和队友一起，为游戏的各个部分制作声音素材。运用一个 *聊天室* 版的 `BeatBox` 应用，团队就可以协同工作 -- 可把一个节拍编排与一条消息一起发出，然后`BeatBox`聊天室中的所有人就会收到这条消息以及与消息一起发出的节拍编排。因此就不光要 *读取* 其他参与者的消息，还要可通过直接点击接收消息区中的某条消息，来加载并 *演奏出* 一个节拍编排。

在本章将了解到，要构造一个这样的聊天客户端需要些什么。甚至还会了解到一点有关构造聊天 *服务器* 的知识。为后面代码厨房的目的，这里将保留完整的 `BeatBox` 聊天室，不过在本章中 *将* 编写一个 `LudicrouslySimpleChatClient` 应用，以及一个用于发送和接收文本消息的非常简单的聊天服务器。

![`SimpleChatClient` 应用](images/Ch15_02.png)

*图 2 - `SimpleChatClient` 应用*

## 聊天程序概览

**Chat Program Overview**

![`SimpleChat`应用概览](images/Ch15_03.png)

*图 3 - `SimpleChat`应用概览*

### 工作原理

**How it Works**:

1) **客户端连接到服务器**

**Client connects to the server**

![`SimpleChat`应用原理（一）](images/Ch15_04.png)

*图 4 - `SimpleChat`应用原理（一）- 客户端连接到服务器*


2) **服务器构造一个连接，并将该客户端加入到参与者清单**

**The server makes a connection and adds the client to the list of participants**

![`SimpleChat`应用原理（二）](images/Ch15_05.png)

*图 5 - `SimpleChat`应用原理（二） - 服务器构造一个连接，并将该客户端加入到参与者清单*


3) **另一客户端进行连接**

**Another client connects**


![`SimpleChat`应用原理（三）](images/Ch15_06.png)

*图 6 - `SimpleChat`应用原理（三） - 另一客户端进行连接*


4) **客户端 A 发送一条消息给聊天服务**

![`SimpleChat`应用原理（四） - 客户端 A 发送一条消息给聊天服务](images/Ch15_07.png)

*图 7 - `SimpleChat`应用原理（四） - 客户端 A 发送一条消息给聊天服务*

5) **服务器将该条消息，发布给所有参与者（包括原本的发送者）**

![`SimpleChat`应用原理（五） - 该条消息被发布到全体参与者](images/Ch15_08.png)

*图 8 - `SimpleChat`应用原理（五） - 该条消息被发布到全体参与者*

## 建立连接、发送数据，与数据接收

**Connecting, Sending, and Receiving**

为了让 `SimpleChat` 客户端运作，必须掌握以下三件事：

1) 如何建立客户端与服务器之间最开始的 **连接**（How to establish the initial **connection** between the client and server）；
2) 如何把消息 **发送给** 服务器（How to **send** messages *to* the server）；
3) 如何从服务器 **接收** 消息（How to **receive** messages *from* the server）。

这些事项要能运行起来，就必须要有大量底层的操作要实现（There's a lot of low-level stuff that has to happen for these things to work）。不过值得庆幸的是，Java 的网络通信包 API （`java.net`）让这一切，都变成了小菜一碟。因此相比于网络通信与 `I/O` 代码，看到的更多的是 GUI 代码。

而且这还不是全部。

`SimpleChat`客户端中潜在的另一个到目前为止尚未遇到的问题，就是在同一时间进行两件事情。连接的建立是一次性的操作（要么成功要么失败）。然而在那之后，聊天室参与者就要 *发送传出消息*，并 **同时** *接收* 来自其他参与者的 *传入消息*。嗯...这就要花点心思，不过很快就会接触到这个问题。

1) **连接（Connect）**

经由建立起一个 **套接字（Socket）** 连接，客户端连接到服务器。

![`SimpleChat`客户端连接到服务器](images/Ch15_09.png)

*图 9 - `SimpleChat`客户端连接到服务器*

2) **发送（Send）**

客户端将消息 **发送** 给服务器。

![`SimpleChat`客户端将消息发送给服务器](images/Ch15_10.png)


*图 10 - `SimpleChat`客户端将消息发送给服务器*


3) **接收（Receive）**

客户端从服务器 **获取到** 消息。


![`SimpleChat`客户端从服务器获取到消息](images/Ch15_11.png)


*图 11 - `SimpleChat`客户端从服务器获取到消息*

## 构造网络套接字连接

**Make a network Socket connection**

要连接到另一台机器，就需要一个套接字连接。套接字（`java.net.Socket`）是一个表示两台机器之间网络连接的对象（A Socket(`java.net.Socket`) is an object that represents a network connection between two machines）。何为连接？即两台机器之间的一种 *关系（relationship）*，其中 *两个软件相互有了解（two pieces of software know about each other）*。更为重要的是，这两个软件知道怎样互相 *通信（communication）*。换句话说，他们知道如何将一系列的 *二进制位* 发送给对方。

好在这里不会关注那些底层细节，这是由于这些低级别细节，是在 “网络通信栈” 中的更底层处处理的（We don't care about the low-level details, thankfully, because they're handled at a much lower place in the 'networking stack'）。若对于“网络通信栈”一无所知，那么也无需担心他。那只是一种关于信息（一系列二进制位）从运行在某种OS上的JVM中的Java程序，到物理实体硬件（比如以太网线），到另一机器后又从物理硬件到Java程序中，所必须经历的多个层的说法。必须 *有人* 来处理这些全部繁重工作（If you don't know what the 'networking stack' is, don't worry about it. It's just a way of looking at the layers that information (bits) must travel throught to get from a Java program running in a JVM on some OS, to physical hardware(ethernet cables, for example), and back again on some other machine. *Somebody* has to take care of all the dirty details）。不过那不是咱们Java程序员要做的。处理这些底层网络通信的，正是那些特定于OS的软件，以及Java的网络通信API。真正要担心的，是那些高级别的东西 -- 构造出那个 *甚为* 高级别的对象 -- 从而就变得惊人的简单了。准备好了吗？

> 要构造一个 `Socket` 连接，就需要知道有关服务器的 **两件事**：服务器是谁，以及运行在哪个端口上。
>
> 换句话说，
>
> 即 **IP 地址和 TCP 端口编号**。


```java
// '196.164.1.103' 是服务器的 IP 地址
//
// '5000' 是 TCP 端口号
Socket chatSocket = new Socket("196.164.1.103", 5000);
```

![Java网络通信套接字的构造原理](images/Ch15_12.png)

*图 12 - Java网络通信套接字的构造原理*

**`Socket` 连接是指两台机器有着对方的信息，包括网络位置（即 IP 地址）以及 TCP 端口（A `Socket` connection means the two machines have information about each other, including network location(IP address) and TCP port）**。

**所谓 TCP 端口，无非是一个编号，一个 16 位的、区分服务器上特定程序的数字（A TCP port is just a number. A 16-bit number that identifies a specific program on the server）。**

互联网 Web（HTTP） 服务器运行在端口 `80`。那就是一项标准。在获取到一台远程登录（Telnet）服务器时，那么就是运行在端口 `23`上。至于 FTP？则为 `20`。POP3 邮件服务器？是 `110`。SMTP？为`25`。时间服务器位于 `37`。只需把端口编号当作唯一识别符就可以了。这些端口编号，表示的都是到运行在服务器上特定软件的逻辑连接（Think of port numbers as unique identifiers. They represents a logical connection to a particular piece of software running on the server）。就是这样子。对于这些端口编号，在物理机器外壳上无论如何也是找不到的。但要记住一点，服务器上有总共 `65536`个端口编号（`0 ~ 65535`）。那么显然这些端口编号不是表示物理设备上可以插入接头的地方。这些编号都只是表示某个应用的一个数字而已。

但若没有这些端口编号，那么服务器就没有办法知道客户端要连接到哪个应用了。而由于各个应用可能有着他自己独特的协议，那么可以设想一下若没有这些标识符，会有多大麻烦。比如在 Web 浏览器落在了 POP3 的邮件服务器，而不是 HTTP服务器上时，会发生什么呢？此时邮件服务器就不知道怎么去解析 HTTP 请求了！同时就算邮件服务器知道怎样区解析 HTTP请求，POP3 服务器也对如何去对HTTP请求进行服务一无所知。

![关于网络通信中的端口编号](images/Ch15_13.png)

*图 13 - 关于网络通信中的端口编号*

**从 `0` 到 `1023` 的 TCP 端口编号，被保留给那些知名服务。在自己的服务器程序中请勿使用他们<sup>*</sup>！**

**这里所编写的聊天室服务器使用了端口 `5000`。所选用的就是 `1024` 与 `65535` 之间的一个数字**。

> <sup>*</sup> 当然也 *可以* 使用 `0` ~ `1023` 中的某个端口编号，不过你工作地方的系统管理员可能会杀了你。

## 答疑

- **该怎么知道要与其会话的服务器程序的端口编号呢（How do you know the port number of the server program you want to talk to）**？

> 这取决于那个服务器程序是否是那些知名服务（well-known services）之一。在要连接某个知名服务，比如上面提到的那些（HTTP、SMTP、FTP等待）时，那么在互联网上就能找到他们的端口编号（Google 一下 “Well-Known TCP Port”）。或者问一下你隔壁的系统管理员同事。
>
> 但在服务器程序并非这些知名服务时，那么就需要问一下部署该项服务的那个人了。通常某人编写了一项网络服务，且他/她又希望有人来为这个网络服务编写客户端时，那么他就会把此项服务的 IP 地址、端口号以及协议公布出来。比如在给一个名为 `GO` 的游戏服务器编写客户端时，就可以访问某个 `GO` 服务器站点，从而找到有关如何为那个特定`GO`游戏服务器编写客户端的信息。


- **在单个端口上可以运行多个的程序吗？也就是说，同一台服务器上的两个应用，可以有着同样的端口号吗**？

> 不行！在尝试将某个程序绑定到一个已在使用中的端口时，就会收到一个 `BindException` 异常。而将某个程序 *绑定* 到某个端口，就意味着启动某个服务器应用，并告诉这个应用在特定端口上运行。老调重弹，在本章后面的服务器部分，会了解到更多有关此方面的内容。

![理解IP地址与端口号](images/Ch15_14.png)


*图 14 - 理解IP地址与端口号*


### 脑力锻炼

好，现在有了一个 `Socket` 连接。客户端与服务器都知道了对方的 IP 地址和 TCP 端口编号。那么现在又该怎么办呢？该怎样透过那个连接进行通讯呢？也就是说，怎样将一系列的二进制位，从一个地方移动到另一个地方呢？请设想一下有哪些类型的消息，要有这个客户端来发送与接收。

![思考题：聊天室客户端与服务器之间如何进行会话](images/Ch15_15.png)


*图 15 - 思考题：聊天室客户端与服务器之间如何进行会话*


## 使用 `BufferedReader` 从套接字读取数据

**To read data from a `Socket`, use a `BufferedReader`**

要透过`Socket`连接来进行通信，就要用到流。常规的那些老式 `I/O` 流，就如同上一章中用到的那些。Java中最酷特性之一，就是大部分的 `I/O` 工作，都不会关心高级别链式流具体连接到的何种连接性流（One of the coolest features in Java is that most of your `I/O` work won't care what your high-level chain stream is actually connected to）。也就是说，可就像之前写入文件时那样使用 `BufferedReader`，不同之处在于这里所采用的连接性流，是连接到一个 *套接字（`Socket`）*，而不再是之前的 *文件（`File`）*！

![Java网络通信中的输入与输出流，都是套接字连接](images/Ch15_16.png)


*图 16 - Java网络通信中的输入与输出流，都是套接字连接*


1) **构造一个到服务器的 `Socket` 连接**

**Make a `Socket` connection to the server**

```java
// '127.0.0.1' 是 “localhost”，也就是此代码
// 运行所在的机器的 IP 地址。在单台、独立机器上对客户端
// 和服务器进行测试时，可使用这个 IP 地址。
//
// 而这个端口号，之前就讲过，5000是这里聊天服务器
// 的端口号。
Socket chatSocket = new Socket("127.0.0.1", 5000);
```

2) **构造一个链接到这个套接字底层（连接性）输入流的 `InputStreamReader`**

**Make an `InputStreamReader` chained to the `Socket`'s low-level (connection) input stream**

```java
// InputStreamReader 是底层字节流（就比如这里的从套接字获取
// 到的那个），与高级别字符流（如同后面那个作为链式流顶部的
// `BufferedReader`）之间的“桥梁”
// 
// 这里只须从该套接字请求一个输入流即可！所获取到的，就是
// 一个低级别的连接性流，不过这里只要将其链接到某个对文本
// 更加友好的东西上（All we have to do is ASK the socket for
// an input stream! It's a low-level connection stream, but we're
// just gonna chain it to something more text-friendly）。
InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream());
```

3) **构造一个 `BufferedReader`然后读取就是了**！

**Make a `BufferedReader` and read**!

```java
// 把这个 BufferedReader 链接到 InputStreamReader （他又
// 是链接到那个从套接字获得的底层连接性流的）。
BufferedReader reader = new BufferedReader(stream);
String message = reader.readLine();
```

![从网络通信套接字读取数据的流链条](images/Ch15_17.png)


*图 17 - 从网络通信套接字读取数据的流链条*


## 使用 `PrintWriter`往套接字写数据

**To write data to a `Socket`, use a `PrintWriter`**

在最后一章用到的并不是`PrintWriter`，那里用了 `BufferedWriter`。虽然这里有选择，不过在一次写一个字符串时，`PrintWriter`就是标准选择。同时也会认识到，`PrintWriter`中的两个关键方法，`print()`与`println()`，就如同先前 `System.out`中的两个一样。

1) **构造一个到服务器的 `Socket` 连接**

**Make a `Socket` connection to the server**

```java
// 此部分与先前从套接字读取数据时一样 -- 要写到
// 服务器，仍必须连接到服务器。
Socket chatSocket = new Socket("127.0.0.1", 5000);
```


2) **构造一个链接到套接字底层（连接性）输出流的 `PrintWriter`**

**Make a `PrintWriter` chained to the `Socket`'s low-level(connection) output stream**

```java
// InputStreamReader 扮演了字符数据与从套接字低级别输出流获取到
// 的字节之间的桥梁。通过将 PrintWriter 链接到套接字的输出流，就可以
// 将字符串写到套接字连接了（PrintWriter acts as its own bridge
// between character data and the bytes it gets from the Socket's
// low-level output stream. By chaining a PrintWriter to the Socket's
// output stream, we can write Strings to the Socket connection）。
//
// 这个套接字给到一个低级别连接性流，同时这里通过将这个连接性流
// 交给 PrintWriter 的构造器，而把这个连接性流链接到
// 新构造的 PrintWriter。
PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
```


3) **写（`print`）下一些内容**

**Write(`print`) something**

```java
// println() 会在他发送的东西末尾加一个另起一行（'/n'）
writer.println("要发送的消息");
// print() 不会添加那个另起一行（'/n'）
writer.print("另一条消息");
```

![往套接字写入数据的流链条](images/Ch15_18.png)


*图 18 - 往套接字写入数据的流链条*


## `DailyAdviceClient` 程序

**The `DailyAdviceClient`**

在开始构建 `SimpleChat` app前，先来做一个较小的东西。`AdviceGuy`是一个提供实用、励志的一些提示的服务器程序，从而让我们度过漫长编写代码的日子。

这里要构建的是一个 `AdviceGuy`程序的客户端，他在每次连接服务器时，从服务器拉取一条消息。

你还等什么呢，若没有这个 app，没人知道你会失去些什么机会。

1） **连接**

**Connect**

客户端连接到服务器，并从他获取到一个输入流（Client connects to the server and gets an input stream from it）。

![`AdviceGuy` app 建立连接](images/Ch15_19.png)

*图 19 - `AdviceGuy` app 建立连接*


2) **读取**

**Read**

客户端从服务器读取一条消息（Client reads a message from the server）。

![`AdviceGuy` app 读取消息](images/Ch15_20.png)


*图 20 - `AdviceGuy` app 读取消息*
