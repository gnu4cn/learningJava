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


### `DailyAdviceClient` 的代码

**`DailyAdviceClient` code**

这个程序构造一个 `Socket`，并构造一个 `BufferedReader`（有着其他流的辅助），进而从服务器应用（即运行在端口`4242`的那个）读取单行文本。

```java
package com.xfoss.AdviceGuy;

import java.io.*;
// 类 Socket 是在 java.net 包中
import java.net.*;

public class DailyAdviceClient {
    public DailyAdviceClient () {
        // 这里有很多会出错的代码
        try {
            // 构造一个到位于此代码运行所在的同一主机
            // （即 'localhost'）、端口 4242 上程序的套接字连接
            Socket s = new Socket("127.0.0.1", 4242);

            // 这里 InputStreamReader 构造函数的第二个参数，指定了字符串
            // 编码，表示 InputStreamReader 的构造函数是过载的。
            // 若不加入这个参数，当服务器和客户端运行在不同平台时
            // 会出现乱码。
            InputStreamReader streamReader = new InputStreamReader(s.getInputStream(), "UTF-8");
            // 把一个 BufferedReader 链接到一个 InputStreamReader
            // 这个 InputStreamReader 又是链接到来自套接字的输入流
            BufferedReader reader = new BufferedReader(streamReader);

            // 这个 readLine() 与之前用到的、链接到文件的
            // BufferedReader 时的那个一模一样。也就是说在
            // 调用 BufferedReader 的某个方法时，读取器（the reader）
            // 是不知道或不关心字符是从何处而来的。
            String advice = reader.readLine();
            System.out.format("今日宜：%s\n", advice);

            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DailyAdviceClient();
    }
}
```


### 编写一个简单的服务器

**Writing a simple server**

那么编写一个服务器应用需要用到哪些东西呢？只需要一对套接字就行。是的，一对就是 *两个*。一个 `ServerSocket`，等待客户端的那些请求（在客户端构造新的 `Socket()` 时），还要一个普通的老式 `Socket` 套接字，用于与客户端进行通信。

**服务器工作原理（How it works）**:

1) 服务器应用在某个特定端口上，构造一个 `ServerSocket`


    ```java
    ServerSocket serverSock = new ServerSocket(4242);
    ```

    这行语句就启动了服务器应用收听那些目的为端口`4242`、进入的客户端请求（This starts the server application listening for client requests coming in for port `4242`）。

    ![服务器应用构造一个 `ServerSocket`](images/Ch15_21.png)


    *图 21 - 服务器应用构造一个 `ServerSocket`*


2) 客户端构造一个到服务器应用的 `Socket` 连接


    **Client makes a `Socket` connection to the server application**

    ```java
    Socket sock = new Socket("190.165.1.103", 4242);
    ```

    客户端了解 IP 地址以及端口号（是由将改服务器app配置在那个端口上的人公布或给到编写客户端的人）

    ![客户端构造一个连接到服务器应用的 `Socket`](images/Ch15_22.png)


    *图 22 - 客户端构造一个连接到服务器应用的 `Socket`*


3) 服务器构造一个新的、与此客户端通信的 `Socket`


    **Server makes a new `Socket` to communicate with this client**

    ```java
    Socket sock = serverSock.accept();
    ```

    在等待某个客户端 `Socket` 连接期间，这个`accept()`方法会阻塞（即闲在那里）。在有客户端最终尝试进行连接时，该方法就会返回一个知道怎样与客户端进行通信（即知道 *客户端* 的 IP地址和端口号），的普通老式套接字（在某个 *不同* 的端口上）。这个 `Socket`是在不同于 `ServerSocket` 的端口上的，因此 `ServerSocket` 才可以回去继续等待其他客户端的连接（The `accept()` method blocks(just sits there) while it's waiting for a client `Socket` connection. When a client finally tries to connect, the method returns a plain old `Socket` (on a *different* port) that knows how to communicate with the client(i.e., knows the *client*'s IP address and port number). The `Socket` is on a different port than the `ServerSocket`, so that the `ServerSocket` can go back to waiting for other clients）。


    ![服务器程序构造一个新的、与客户端通信的 `Socket`](images/Ch15_23.png)

    *图 23 - 服务器程序构造一个新的、与客户端通信的 `Socket`*


### `DailyAdviceServer` 程序代码

**`DailyAdviceServer` code**

```java
package com.xfoss.AdviceGuy;

// 要记得这些导入
import java.io.*;
import java.net.*;

public class DailyAdviceServer {
    // 日常劝解来自这个数组
    // （请记住，这些字符串是由编码编辑器包装起来的单词。绝不要在字符串
    // 中间敲入回车（remember, these Strings were word-wrapped by 
    // the code editor. Never hit return in the middle of a String）！）
    String[] adviceList = {
        "少食多餐", 
        "买些紧身牛仔裤。他们不会让你看起来显胖。", 
        "一个字：不合适",
        "就今天而言，要诚实，告诉你的老板你的真实想法。",
        "对于这个发型，你应该三思而后行"
        };

    public DailyAdviceServer () {
        try {
            // 这里的 ServerSocket 对象，令到此服务器应用在该
            // 代码运行所在机器的端口 4242 上，“收听” 那些
            // 客户端请求。
            ServerSocket serverSock = new ServerSocket(4242);

            // 服务器进入到一个永久循环，等待（并服务）那些
            // 客户端请求。
            while (true) {
                // 这个 accept() 方法将会阻塞（即处于空闲状态），直到
                // 有请求进来，并在有请求进来时返回一个用于与
                // 客户端通信的 `Socket`（在某个匿名端口）
                //
                // the accept method blocks (just sits there) until
                // a request comes in, and then the method returns 
                // a Socket(on some anonymous port) for communicating
                // with the client
                Socket sock = serverSock.accept();

                // 此时就要是由这个到客户端的套接字连接，来构造一个
                // PrintWriter，并把一个字符串的劝解消息，发送给他（println()）
                // 而由于完成了这个客户端的请求，所以随后就要关闭这个套接字。
                //
                // now we use the Socket connection to the client to make a 
                // PrintWriter and send it (println()) a String advice message
                // Then we close the Socket because we're done with this client.
                //
                // PrintWriter writer = new PrintWriter(sock.getOutputStream());
                // 这里进行了修改，加入了字符串编码，解决服务器与客户端运行于
                // 不同 OS 下时乱码问题。
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                System.out.println(advice);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getAdvice() {
        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }

    public static void main (String[] args) {new DailyAdviceServer();}
}
```

### 脑力锻炼

**服务器怎样知道他与客户端如何通信**？

客户端知悉服务器的 IP 地址与端口编号，然而服务器又是怎样能构造一个跟客户端的套接字连接（进而构造输入和输出流）呢？

请思考一下服务器怎样/于何时/何处获悉到客户端的知识的（Think about how/when/where the server gets knowledge about the client）。


## 答疑

- **上面的算命服务器代码，有个非常严重的问题 -- 看起来他只能一次处理一个客户端**！

> 是的，说的没错。在没有完成当前客户端请求，进而开始那个无限循环（正是在这个无限循环里，服务器的 `accept()` 调用会处于空闲，等待客户端请求进入，在请求进入时，服务器构造一个跟新客户端的套接字，完成请求处理后就又开始另一个迭代和等待）的下一次迭代之前，这个服务器是无法接受另一客户端请求的（Yes, that's right. It can't accept a request from a client until it has finished with the current client and started the next iteration of the infinite loop(where it sits at the `accept()` call until a request comes in, at which time it makes a Socket with the new client and starts the process over again)）。


- **我还是换个说法吧：怎样才能构造一个可以同时并发地处理多个客户端的服务器？？？好比说现在这个服务器，*绝对不能* 当作一个聊天服务器**。

> 呃，那并不难，真的。使用一些单独线程，然后把各个新客户端套接字交给这些线程即可。这里正要掌握怎么实现线程和并发特性呢（Ah, that's simple, really. Use separate threads, and give each new client `Socket` to a new thread. We're just about to learn how to do that）！

## 重点

- 客户端和服务器应用透过套接字连接进行通信（Client and server applications communicate over a `Socket` connection）；
- 套接字表示两个应用之间的连接，两个应用可运行在同一台机器，亦可运行在不同机器上；
- 客户端必须知道服务器应用的IP地址（或域名）与TCP端口好；
- TCP端口是个指派给特定服务器应用的16位无符号数。TCP端口号实现不同客户端连接到同一台机器，而与运行在那台机器上的不同应用进行通信；
- 从 `0` 到 `1023` 的端口号，保留用于那些 “知名服务（well-known services）”，包括 HTTP、FTP、SMTP等待；
- 客户端通过构造服务器套接字，连接到服务器；

```java
Socket s = new Socket("127.0.0.1", 4200);
```

- 一旦连接成功，客户端就可以从那个套接字获取到输入与输出流。从套接字获取到的输入输出流，都是底层的 “连接性” 流；

```java
sock.getInputStream();
sock.getOutputStream();
```

- 要从服务器读取文本数据，就要创建 `BufferedReader`，将其链接到一个 `InputStreamReader`，而 `InputStreamReader`又链接到来自套接字的输入流；
- `InputStreamReader`是一个取得字节序列，然后转换为文本（字符）数据的 “桥接性” 流。主要用作高级别 `BufferedReader` 与底层套接字的输入流的中间链（`InputStreamReader` is a 'bridge' stream that takes in bytes and converts them to text(character) data. It's used primarily to act as the middle chain between the high-level `BufferedReader` and the low-level `Socket` input stream）；
- 要向服务器写数据，就要创建直接链接到套接字输出流的 `PrintWriter`对象。调用其 `print()` 或 `println()` 方法，来将字符串发送给服务器；
- 服务器会用到在特定端口号上等待客户端请求的 `ServerSocket`；
- 在 `ServerSocket` 收到请求时，`ServerSocket`就会通过构造跟客户端的套接字连接，而 “受理” 这个请求。

## 编写 `ChatClient`

**Writing a `ChatClient`**

这里将通过两阶段，来编写 `ChatClient`。首先要构造一个把消息发送给服务器，但尚不会读取其他聊天室参与者发出消息的仅发送版本（一个对完整聊天室概念的激动人心又神秘的曲解，First we'll make a send-only version that sends messages to the server but doesn't get to read any of the messages from other paritcipants(an exciting and mysterious twist to the whole chat room concept)）。

随后就会实现完善的聊天方式，而构造一个兼具聊条消息发送 *与* 接收的 `ChatClient`。

### 第一版：仅发送

**Version One: send-only**

![`LudicrouslySimpleChatClient`聊天室客户端第一版](images/Ch15_24.png)

*图 24 - `LudicrouslySimpleChatClient`聊天室客户端第一版*

**代码大纲**

```java
public class SimpleChatClientA extends JFrame {
    JTextField outgoing;
    PrintWriter writer;
    Socket sock;

    public SimleChatClientA () {
        // SimpleChatClientA 类构造函数
        // 构造 GUI 及在发送按钮上注册事件收听者
        // 调用下面的 setUpNetworking() 方法
    }

    private void setUpNetworking () {
        // 构造一个 Socket 对象，随后构造一个 PrintWriter 对象
        // 将 PrintWriter 指派给实例变量 writer
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            // 从文本字段获取文本，并
            // 使用 writer 实例变量（即一个 PrintWriter 对象），把
            // 获取到的文本发送给服务器
        }
    }
}
```

```java
package com.xfoss.SimpleChat;

// 各种流（java.io）、套接字（java.net）及GUI等得各种导入
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClientA extends JFrame {

    JTextField outgoing;
    PrintWriter writer;
    Socket sock;

    public SimpleChatClientA () {
        // 这里只是构建 GUI，没有什么新东西，且不涉及到网络通信
        // 或 I/O 操作
        super("搞笑的简单聊天客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        outgoing = new JTextField(20);

        JButton sendButton = new JButton("发送");
        sendButton.addActionListener(new SendButtonListener());

        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        getContentPane().add(BorderLayout.CENTER, mainPanel);

        setUpNetworking();

        setSize(640, 480);
        setVisible(true);

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent winEvt) {
                if(writer != null) writer.close();
                System.exit(0);
            }
        });
    }

    // 此时完成具体写入。请记住，这里的 writer 对象，链接的是来自
    // Socket 对象的输出流，因此不能何时执行 println() 方法，都会
    // 透过网络抵达服务器！
    //
    // Now we actually do the writing. Remember, the writer is chained
    // to the output stream from the Socket, so whenever we do a println()
    // it goes over the network to the server!
    public class SendButtonListener implements ActionListener {
        public void actionPerformed (ActionEvent ev) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception ex) {ex.printStackTrace();}

            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    private void setUpNetworking () {
        try {
            // 由于用的是 localhost ，因此可以在一台机器上测试客户端
            // 和服务器。
            // 
            // 这里就是构造 Socket 与 PrintWriter （这个 setUpNetworking() 
            // 方法，是在刚刚显示出该 app 的 GUI 界面后，从类 SimpleChatClientA 
            // 构造器调用）。
            sock = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            System.out.println("网络通信已建立");
        } catch (IOException ex) {ex.printStackTrace();}
    }
}
```

> **若现在就想尝试运行，那么就要输入本章末尾处的已编写好的服务器代码。现在一个终端启动服务器，接着用另一个终端启动这个客户端即可**。


### 版本二：发送与接收

**Version Two: send and receive**

![具备发送和接收功能的 `SimpleChatClient` app](images/Ch15_25.png)


*图 25 - 具备发送和接收功能的 `SimpleChatClient` app*

**大问题：怎样从服务器获取消息**？

应该不难；建立起网络通讯时，就要同时构造一个输入流（应该是个`BufferedReader`）。随后使用 `readLine()`读取消息。


**更大的问题：何时从服务器获取消息**？

**Bigger Question: WHEN do you get messages from the server**?

请思考一下这个问题。有哪些选项？

1) **选项一：每隔20秒轮询一次服务器（Option One: Poll the server every 20 seconds）**


    **优点（Pros）**: 是的，这是可行的。

    **弊端（Cons）**：服务器怎么知道客户端已经收到的消息与尚未收到的消息？服务器就不得不把消息存储起来，而不是在每次收到消息后只要分发并忘却。并且为什么间隔是20秒？这样的延迟对可用性有影响，而如果降低这个延迟值，又会带来对服务器不必要冲击。这个选项效率底下（The server would have to store the messages, rather than just doing a distribute-and-forget each time it gets one. And why 20 seconds? A delay like this affects usability, but as you reduce the delay, you risk hitting your server needlessly. Inefficient）。

2) **选项二：在用户每次发送消息时从服务器读入一些东西（Option Two: Read something in from the server each time the user sends a message）**。


    **优点（Pros）**: 可行，很容易。

    **弊端（Cons）**：笨办法。为什么要选择这样的任意时间去查收那些消息？如果用户一直潜水不发送任何消息会怎样呢？


3) **选项三：在服务器发出消息后立即读取消息（Option Three: Read messages as soon as they're sent from the server）**。


    **优点（Pros）**: 最为高效，最佳可用性。

    **弊端（Cons）**: 怎样在同一时间完成两件事情？应该把代码放在何处？这就会涉及到某处的一个一直等待着从服务器读取的循环。然而这个循环应该放在哪里呢？在启动了 GUI后，就只有在GUI部件发出事件后，程序才有动作了，其他情况下程序是不会有任何动作的（How do you do two things at the same time? Where would you put this code? You'd need a loop somewhere that was always waiting to read from the server. But where would that go? Once you launch the GUI, nothing happens until an event is fired by a GUI component）。

> **在 Java 中，真的可以一边走路一边嚼口香糖**。


**你肯定明白从现在开始就要采行选项三了吧**。

这里要的是持续运行对来自服务消息进行检查，而又 *不会对用户与 GUI 交互能力造成干扰* 的东西（We want something to run continuously, checking for messages from the server, but *without interrupting the user's ability to interact with the GUI*）！那么在用户开心地输入消息，或者在众多传入消息中愉悦地滚动翻阅的同时，所需要的就是 *这美好场景背后*，有代码去持续读取来自服务器的新输入。

这就意味着这里最终需要一个新线程。一个新的、独立的栈（That means we finally need a new thread. A new, separate stack）。

这里要让在前面那个仅发送版（版本一）中完成的所有工作，还以同样方式运作，与此同时伴随一个新的 **进程（process）** 来读取来自服务器的信息，并将读取到的信息显示在传入文本区。

好吧，也不尽然是这样子的。除非计算机上有多个处理器，否则各个新 Java 线程也并非真正是运行在操作系统上的单独进程。不过这些新 Java 线程 *给人的感觉*，就跟独立进程一样（Well, not quite. Unless you have multiple processors on your computer, each new Java thread is not actually a separate process running on the OS. But it almost *feels* as though it is）。

## Java中的多线程技术

**Multithreading in Java**

Java在其语言结构中，就内置了多线程技术。同时构造一个新执行线程十分简单（Java has multiple threading built right into the fabric of the language. And it's a snap to make a new thread of execution）：

```java
Thread t = new Thread();
t.start();
```

这就完了。通过创建新的 `Thread` *对象*，就启动了一个单独的、有着自己独有调用栈的 *执行线程*（That's it. By creating a new `Thread` *object*, you've launched a separate *thread of execution*, with its very own call stack）。

**然而这里有个问题（Except for one problem）**。

上面的线程实际上什么也不会 *做*，因此可以说这个线程一出生就几乎“死了”。在线程死去时，他的新栈也会消失。故事结束（That thread doesn't actually *do* anything, so the thread "dies" virtually the instant it's born. When a thread dies, its new stack disappears again. End of story）。

因此这里缺失了一项关键组件 -- 线程的 *作业*。也就是说，这里需要打算让独立线程去运行的那些代码（So we're missing one key component -- the thread's *job*. In other words, we need the code that you want to have run by separate thread）。

Java 中的多线程技术，意味着这里必须同时要了解 *线程* 及由该线程所 *运行* 的 *作业*（Multiple threading in Java means we have to look at both the *thread* and the *job* that's *run* by the thread）。与此同时这里还会了解 `java.lang` 包中的 `Thread` 类。（请记住，`java.lang`无需手动导入、已被隐式导入，同时这个包中有着Java这门语言的一些最有基础的类，包括`String`及`System`等。）

### Java有着多线程特性，却只有一个`Thread`类

**Java has multiple threads but only on `Thread` class**

对于 *线程（thread）*，既可以讲作带小写字母 ‘t’ 的 `thread`，同时也可讲作带大写字母 ‘T’ 的 `Thread`。在讲到 `thread`，时，讲的是一条独立的执行线。也就是一个单独调用栈。而在讲到 `Thread`时，请想想 Java 的命名约定。Java 里以大写字母开头的都是些什么？都是些类和接口。那么在此情形下，`Thread`就是 `java.lang` 包中的一个类。而`Thread`对象表示的，就是一条 *执行线*；在每次想要启动一条新的执行线时，都会创建一个 `Thread` 类的实例。

> **一个线程就是一条单独 “执行线”**。也即是一个单独的调用栈。
>
> **而`Thread` 则是表示某个线程的 Java 类**。
>
> **要构造一个线程，就要构造一个 `Thread` 对象**。

![关于线程](images/Ch15_26.png)

*图 26 - 关于线程*

线程是一条独立的执行线（A thread(lower-case 't') is a separate thread of execution）。意思是一个单独调用栈。每个Java应用都会启动一个主线程 -- 那个把 `main()` 方法放在栈底部的线程。JVM负责主线程的启动（以及一些JVM选定的其他线程，包括垃圾回收线程等）。作为Java程序员，可编写代码来启动自己的其他线程。

![类 `Thread`](images/Ch15_27.png)

*图 27 - 类`Thread`*

`Thread` （带大写字母 `T`）是表示执行线程的一个类。有着用于启动线程、将一个线程与另一线程结合以及将线程置于睡眠状态等方法。（类`Thread`有着其他一些方法；这三个不过是这里需要用到关键方法）。

### 具有多个调用栈指的是什么？

**What does it mean to have more than one call stack**?

有了多个调用栈，就能得到同时发生多个事情的 *样子*。实际只有真正多处理器系统，才能真正地一次完成多个事情，而在Java多线程特性下，就可以 *出现* 同时完成多个事情。也就是说，代码执行可极为迅速地在多个调用栈之间前后移动，以至于这所有栈是在同时执行。请记住，Java只是运行在所在操作系统上的一个进程。因此首先，Java *本身* 就必须是操作系统上的 “当前正在执行的进程”。然而一旦Java得到他的执行轮次，JVM会利用这个执行轮次来 *运行什么* 呢？哪些字节码会执行呢？当然是位于当前正在执行栈顶部的那些！而就在 100 毫秒期间，当前执行代码就可能切换到一个 *不同* 栈的 *不同* 方法（With more than one call stack, you get the *appearance* of having multiple things happen at the same time. In reality, only a true multiprocessor system can actually do more than one thing at a time, but with Java threads, it can *appear* that you're doing several things simultaneously. In other words, execution can move back and forth between stacks so rapidly that you feel as though all stacks are executing at the same time. Remember, Java is just a process running os your underlying OS. So first, Java *itself* has to be 'the currently executing process' on the OS. But once Java gets its turn to execute, exactly *what* does the JVM *run*? Which bytecodes execute? Whatever is on the top of the currently-running stack! And in `100` milliseconds, the currently executing code might switch to a *different* method on a *different* stack）。

线程必须完成的事情中有一项，就是保持对线程栈上当前正在执行语句（所在方法）的跟踪（One of the things a thread must do is keep track of which statement(of which method) is currently executing on the thread's stack）。

线程工作原理看起来差不多是这样的：

1) **JVM 调用 `main()` 方法**


    ```java
    public static void main(String[] args) {
        ...
    }
    ```

    ![主进程](images/Ch15_28.png)

    *图 28 - 主进程*

2) `main()` 启动一个新线程。在新线程开始运行的同时，主线程被临时冻结（`main()` starts a new thread. The main thread is temporarily frozen while the new thread starts running）。


    ```java
    // 很快就会学到这里的 Runnable 对象 r 是什么意思。
    Runnable r = new MyThreadJob();
    Thread t = new Thread(t);
    t.start();
    Dog d = new Dog();
    ```

    ![主线程启动一个新的线程](images/Ch15_29.png)

    *图 29 - 主线程启动一个新的线程*


3) JVM 在新线程（用户线程A）与原本的主线程直接不断切换，直到两个线程执行完毕。


    ![进入多线程执行阶段，在全部线程栈运行完毕之前，JVM于这些栈之间不断切换](images/Ch15_30.png)


    *图 30 - 进入多线程执行阶段，在全部线程栈运行完毕之前，JVM于这些栈之间不断切换*


### 怎样启动一个新线程

**How to launch a new thread**

1) **构造一个 `Runnable` 对象（即该新线程的作业）**


    ```java
    Runnable treadJob = new MyRunnable();
    ```

    ![构造一个`Runnable` 对象](images/Ch15_31.png)

    *图 31 - 构造一个`Runnable` 对象*

    `Runnable` 是一个接口，后面马上就会学到。将会编写一个对 `Runnable` 接口进行实现的类，而正是在这个类中，去定义某个线程要执行的任务工作。也就是那些要在线程的新调用栈上运行的那个方法（`Runnable` is an interface you'll learn about on the next page. You'll write a class that implements the `Runnable` interface, and that class is where you'll define the work that a thread will perform. In other words, the method that will be run from the thread's new call stack）。


2) **构造一个`Thread` 对象（即工具人线程）并把一个`Runnable`对象（即作业）交给他（Make a `Thread` object(the worker) and give tit a `Runnable`(the job)）**


    ```java
    Thread myThread = new Thread(threadJob);
    ```

    ![以这个`Runnable`对象做参数，构造一个`Thread`对象](images/Ch15_32.png)

    *图 32 - 以这个`Runnable`对象做参数，构造一个`Thread`对象*

    将上面构造的新`Runnable`对象传递给 `Thread` 类构造器。这就告诉了新的`Thread`对象，要将哪个方法放在新执行栈 的底部 -- 当然是那个 `Runnable` 对象的 `run()` 方法。


3) **启动该线程（Start the `Thread`）**


    ```java
    myThread.start();
    ```

    在调用该`Thread`对象的`start()`方法前，什么也不会发生。对此方法的调用，正是由一个`Thread`实例，向一条新执行线程转变的时间点。在新线程启动起来时，新线程就取得那个`Runnable`对象的`run()`方法，并将其放在新线程栈的底部（Nothing happens until you call the `Thread`'s `start()` method. That's when you go from having just a `Thread` instance to having a new thread of execution. When the new thread starts up, it takes the `Runnable` object's `run()` method and puts it on the bottom of the new thread's stack）。

    ![启动线程](images/Ch15_33.png)

    *图 33 - 启动线程*


### 每个`Thread`对象都需要一项要执行的作业。即一个要放到新线程栈上的方法。

**Every `Thread` needs a job t do. A method to puton the new thread stack**.

> **`Runnable`对象之于`Thread`对象，就好比某项作业任务之于一名工人。`Runnable`对象就是线程要运行的作业（`Runnable` is to a `Thread` what a job is to a worker. A `Runnable` is the job a thread is supposed to run）。**
>
> **`Runnable`对象保存着那个将要放到新线程执行栈底部的方法：`run()`（A `Runnable` holds the method that goes on the bottom of the new thread's stack: `run()`）**。

`Thread`对象需要作业。在现场启动起来时，线程就会运行一项作业。而那项作业，实际上就是去到新线程执行栈上的第一个方法，且作业务必总是一个看起来像这样的方法：

```java
// 接口 Runnable 只定义了一个方法，那就是 public void run()
// （请记住，由于他是个接口，因此这个方法是 public 的，与
// 这里的是否像这样输入代码无关。）
//
// The Runnable interface defines only one method,
// public void run(). (Remember, it's an interface so the 
// method is public regardless of whether you type it in that
// way.)
public void run () {
    // 将被新线程运行的代码
}
```

线程怎样知道该放那个方法在执行栈的底部呢？因为`Runnable`接口定义了合约。因为`Runnable`是个接口。线程作业可被定义在任何的实现了`Runnable`接口的类里头。线程仅关心传递给`Threa`类构造器的，十一个实现了`Runnable`接口的类的对象（How does the thread know which method to put at the bottom of the stack? Because `Runnable` defines a contract. Because `Runnable` is an interface. A thread's job can be defined in any class that implements the `Runnable` interface. The thread cares only that you pass the `Thread` cnstructor an object of a class that implements `Runnable`）。

在将`Runnable`类型对象传递给`Thread`构造器时，真的就只是给予了那个`Thread`对象，一种抵达`run()`方法的方式。实在给予那个`Thread`对象其要执行的作业（When you pass a `Runnable` to a `Thread` constructor, you're really just giving the `Thread` a way to get to a `run()` method. You're giving the `Thread` its job to do）。

### 要构造线程的作业，就要实现`Runnable`接口

**To mke a job for your thread, implement the `Runnable` interface**

```java
// Runnable 是在 java.lang 包中，因此无需导入。
public class MyRunnable implements Runnable {
    private String incomingMessage;

    public MyRunnable (String message) {
        incomingMessage = message;
    }

    // Runnable只有一个要实现的方法：
    // public void run() （不带参数）
    // 这就是放置线程将要运行作业方法的地方。这也是那个
    // 位于新执行栈底部的方法。
    public void run () {
        go();
    }

    public void go () {
        doMore ();
    }

    public void doMore () {
        System.out.format("这是执行栈的顶部, 收到主线程传入的消息：%s\n", incomingMessage);
    }
}

class ThreadTester {
    public static void main (String[] args) {
        Runnable threadJob = new MyRunnable("你好，用户线程！");
        // 将新构造的Runnable实例，传入到这个
        // 新 Thread 构造器。这就告诉线程，把什么样的
        // 方法放在新执行栈的底部。也就是新执行栈将运行的
        // 第一个方法。
        Thread myThread = new Thread(threadJob);

        // 在没有调用这个 Thread 实例上的 start() 方法前，不会
        // 得到新的执行线程。在启动线程前，线程还不成其为线程。
        // 在启动 Thread 实例之前，他就跟其他的对象一样，
        // 只是个 Tread 的实例，而不会有任何真实的 “线程特征”（
        // Before that, it's just a Thread instance, like any 
        // other object, but it won't have any real 'threadness'）。
        myThread.start();

        System.out.println("回到主线程");
    }
}
```

> **注**: 这段代码演示了：1、构造新线程；2、在构造新线程时，主线程往用户线程传递数据；3、Java 中主类可以不是显式 `public` 的类，或者说一个 `.java`文件中只能有一个`public`类的情况下，其他非显式`public`的类，也可作为Java的主类，作为`jar`包的执行进入点。

![示例代码的执行栈图解](images/Ch15_34.png)

*图 34 - 示例代码的执行栈图解*

上面代码的输出，如下所示：

```console
$java -jar learningJava/build/libs/com.xfoss.learningJava-0.0.1.jar
回到主线程
线程栈的顶部，收到主线程的消息：你好，用户线程！
```

### 脑力锻炼

在运行 `ThreadTester` 类时，为什么会有上面这样的输出？

### 新线程的三种状态

**The three state of a new thread**

```java
Threed t = new Thread(r);
```

![线程的三种状态](images/Ch15_35.png)

*图 35 - 线程的三种状态*

**新构建（NEW）状态**

```java
Thread t = new Thread(r);
```

线程已被创建但未被启动。也就是说，已经有了一个`Thread`对象，但还没有 *执行线*（A `Thread` instance has been created but not started. In other words, there is a `Thread` *object*, but no *thread of execution*）。


**可运行（RUNNABLE）状态**

```java
t.start();
```

在启动这个线程时，他便进入到可运行状态。这意味着该线程准备好了运行，只是在等待着他被选作执行的时机。这个时候，就有了此线程的调用栈了（When you start the thread, it moves into the runnable state. This means the thread is ready to run and just waiting for its Big Change to be selected for execution. At this point, there is a new call stack for this thread）。


**运行（RUNNING）状态**

这是所有线程渴望的状态！成为被选中的那个线程。那个当前运行线程。而只有JVM的线程调度器，才能做出决定。有时可人为对该决定 *施加影响*，但无法强制某个线程从可运行状态转换成运行状态。在运行状态，线程（且 **只有** 这个线程）有着活动的调用栈，同时该调用栈顶部的那个方法在执行（This is the state all thread lust after! To be The Chosen One. The Currently Running Thread. Only the JVM thread scheduler can make that decision. You can sometimes *influence* that decision, but you cannot force a thread to move from runnable to running. In the running state, a thread (and ONLY this thread) has an active call stack, and the method on the top of the stack is executing）。

**然而关于线程状态，还有更多的东西。一旦线程成为可运行状态，他就可以在可运行状态（runnable）、运行状态（running），以及一些其他状态：*临时非可运行状态（temporarily not runnable）*（也叫做“阻塞状态（blocked）”） 之间交互变化**。


### 典型的可运行/运行状态循环

**Typical runnable/running loop**

典型情况下，JVM线程调度器选出某个线程来运行，并在随后将其踢回，而让另一线程有时机运行，如此线程是在可运行状态与运行状态直接来回切换的（Typically, a thread moves back and forth between runnable and running, as the JVM thread scheduler selects a thread to run and then kicks it bak out so another thread gets a chance）。

![典型状况下线程从可运行状态与运行状态之间变换](images/Ch15_36.png)


*图 36 - 典型状况下线程从可运行状态与运行状态之间变换*


### 线程可被置为暂时非可运行状态

**A thread can be made temporarily not-runnable**

![线程的阻塞状态](images/Ch15_37.png)

*图 37 - 线程的阻塞状态*

因为各种不同原因，线程调度器可将运行状态线程，移入到阻塞状态。比如线程可能正在执行从套接字输入流读取数据的代码，但套接字输入流上现在并没有要读取的数据。那么调度器就会将该线程从运行状态移除，知道要读取的数据可用为止。或者线程正在执行的代码，告诉线程要将他自己置入睡眠状态（`sleep()`）。抑或可能由于在尝试调用某个对象上的方法，而那个对象却被“锁了起来”，因此该线程就处于等待状态。在这种情况下，直到所调用对象的锁，被持有这个锁的线程释放之前，等待中的线程，是无法继续执行的（The thread schedular can move a running thread into a blocked state, for a variety of reasons. For example, the thread might be executing code to read from a `Socket` input stream, but there isn't any data to read. The schedular will move the thread out of the running state until something becomes available. Or the executing code might have told the thread to put itself to sleep(`sleep()`). Or the thread might be waiting because it tried to call a method on an object, and that object was 'locked'. In that case, the thread can't continue until the object's lock is freed by the thread that has it）。

所有上述的情形（以及其他情形），都会导致线程成为临时非可运行状态（All of those conditions(and more) cause a thread to become temporarily not-runnable）。


## 进程调度器

**The Thread Schedular**

线程调度器就谁将从可运行状态迁往运行状态，与何时（及在何种条件下）某个线程离开运行状态等事情，做出一切决定。调度器决定谁会运行，与运行多长时间，以及在调度器决定把某些线程从当前运行状态踢出去时，这些线程前往何处（The thread schedular makes all the decisions about who moves from runnable to running, and about when(and under what circumstances) a thread leaves the running state. The schedular decides who runs, and for how long, and where the threads go when the scheduler decides to kick them out of the currently-running state）。

编程者无法对调度器施加控制。调度器上并无调用方法的API。最重要的时，并无调度的任何保证！（虽然有那么几个 *近乎* 保证的东西，但就算这些也是毫无头绪的。）（You can't control the scheduler. There is no API for calling methods on the scheduler. Most importantly, there are no guarantees about scheduling! (There are a few *almost*-guarantees, but even those are a little fuzzy.)）

在处理线程调度器时，底线就是：***不用把程序的正确运行，建立在调度器以某种特定方式运作的基础上***！对于不同JVM，此调度器的实现是各异的，即使在同一台机器上运行同样的程序，都能给出不同的运行结果。新手Java程序员最糟糕的问题，就是在单台机器上测试他们的多线程程序，并错误地假定线程调度器会一致以那种方式运作，而不考虑程序会在何处运行（The bottom line is this: ***do not base your program's correctness on the scheduler working in a particular way***! The scheduler implementations are different for different JVM's, and even running the same program on the same machine can give you different results. One of the worst mistakes new Java programmers make is to test their multi-threaded program on a single machine, and assume the thread scheduler will always work that way, regardless of where the program runs）。

而这到底对“一次编写各处运行”意味着什么呢？这意味着要编写独立于平台Java代码，那么所编写的多线程程序，就必须在不管线程调度器 *怎样* 行事，都要能工作。那就是说不能对，比如，调度器确保所有线程都能在运行状态上得到良好的、绝对公平的执行机会，有所依赖。尽管如今几乎不会有这样的情况发生：程序运行在一个有着这样说的调度器JVM上，“好的，五号线程，现在你可以执行了，在我仍管事的时候，你可以一直执行，直到你的 `run()` 方法执行完毕而结束为止”（So what does this mean for write-one-run-anywhere? It means that to write platform-independent Java code, your multi-threaded program must work no matter *how* the thread scheduler behaves. That means that you can't be dependent on, for example, the scheduler making sure all the threads take nice, perfectly fair and equal turns at the running state. Although highly unlikely today, your program might end up running on a JVM with a scheduler that says: "Ok thread five, you're up, and as far as I'm concerned, you can stay here until you're done, when your `run()` method completes"）。

![工作中的线程调度器](images/Ch15_38.png)

*图 38 - 工作中的线程调度器*

> **线程调度器就谁运行谁不运行做出所有决定。他通常会让线程们有着良好的队列。然而对此并无保证。线程调度器可能会让一个线程心满意足地运行的同时，而让其他线程饱受“饥饿”之苦（The thread scheduler makes all the decisions about who runs and who doesn't. He usually makes the threads take turns, nicely. But there's no guarantee about that. He might let one thread run to its heart's content while the other threads 'starve'）**。

这些东西的秘诀，几乎都在于 *睡眠*。对的，就是 *睡眠*。将某个线程置于睡眠中，即使几个毫秒的时间，就可以强制当前正在运行的线程离开运行状态，因此而给到另一线程运行的机会。`Thread`的静态方法 `sleep()`，有着 *一项* 明确的保证：在休眠时间超时之前，休眠中的线程，是不会成为当前运行线程的。比如，在告诉了线程休眠两秒（`2000`毫秒）后，那么在两秒钟时间过去之前，那个线程就绝不可能再度成为运行线程（The secret to almost everything is *sleep*. That's right, *sleep*. Putting a thread to sleep, even for a few milliseconds, forces the currently-running thread to leave the running state, thus giving another thread a chance to run. The `Thread`'s `sleep()` method does come with *one* guarantee: a sleeping thread will *not* become the currently-running thread before the length of its sleep time has expired. For example, if you tell your thread to sleep for two seconds (`2000` milliseconds), that thread can never become the running thread again until sometime *after* the two seconds have passed）。

### 关于调度器不可预测的示例

> 此示例即上面的 `MyRunable` 程序，经测试，在 `jdk11` 下输出实际上是恒定的。故在此省略。


### 为何会有不同的输出？

**How did we end up with different results**?

**有时程序像这样运行（Sometimes it runs like this）**:

![示例程序线程执行顺序之一](images/Ch15_39.png)


*图 39 - 线程执行顺序之一*

**而有时他会这样运行**：

![示例程序线程执行顺序之二](images/Ch15_40.png)

## 答疑

- **之前曾看到过一些没有使用单个 `Runnable` 实现，而是只构造了一个`Thread`子类并重写了`Thread`得 `run()` 方法的示例。那样的话，在构造新线程时，就相当于调用了 `Thread` 的无参数构造器（I've seen examples that don't use a separate `Runnable` implementation, but instead just make a subclass of `Thread` and override the `Thread`'s `run()` method. That way, you call the `Thread`'s no-arg constructor when you make the new thread）**；

```java
Thread t = new Thread(); // 没有 Runnable
```

> 是的，这是另一种构造自己的线程的方式，不过请从面向对象角度来思考这个问题。子类化操作的目的何在？请记住这里讲的是两个不同的事情 -- 类 `Thread` 与线程的 *作业（job）*。从面向对象视角来看，这二者有着完全不同的性质，且属于不同的类。在打算对类 `Thread` 进行子类化/扩展时，唯一目的就是要构造一个新的且更具体的`Thread`。也就是说，在将 `Thread` 当作工作者（worker）时，除非需要更具体的 *工作者（worker）* 行为，那么就请不要对 `Thread` 进行扩展。而在仅需要某个新 *作业* 由工作线程去运行时，就要对 `Runnable` 接口，在一个单独的、特定于*作业（job）* （而非特定于 *工作线程*） 的类中进行实现（Yes, that *is* another way of making your own thread, but think about it from an OO perspective. What's the purpose of subclassing? Remember that we're talking about two different things here -- the `Thread` and the thread's *job*. From an OO view, those two are very separate activities, and belong in separate classes. The only time you want to subclass/extend the `Thread` class, is if you are making a new and more specific type of `Thread`. In other words, if you think of the `Thread` as the worker, don't extend the `Thread` class unless you need more specific *worker* behaviors. But if all you need is a new *job* to be run by a `Thread`/worker, then implement `Runnable` in a separate, *job*-specific (not *worker*-specific) class）。
>
> 这是个设计问题，而非性能或语言问题。对`Thread`进行子类化，以及重写 `run()` 方法，是完全合法的，但这样做只有在极少的情况下，才是不错的主意（This is a design issue and not a performance or language issue. It's perfectly legal to subclass `Thread` and override the `run()` method, but it's rarely a good idea）。

- **可以重用某个`Thread`对象吗？就是可以交给他一项新的作业，然后通过再次调用 `start()` 方法来重启他吗**？

> 不行。一旦线程的 `run()`方法执行完毕，那么这个线程就再也不能被重启了。事实上，线程在那个时间点就过渡到一种还不曾讲到的状态 -- ***死亡（dead）*** 状态。在死亡状态，线程已经完成他的 `run()` 方法，且绝不可能被重启。那个`Thread`对象可能仍在内存堆上，作为一个存活的对象，可调用其另外的方法（只有一些适当的方法），然而这个`Thread`对象已经永远失去了他的 “线程性质”。也就是说，已经没有了单独的调用栈，同事这个 `Thread` 对象也不在是一个 *线程（thread）*了。这个时候他就仅仅是个对象，与其他对象别无二致（No. Once a thread's `run()` method has completed, the thread can never be restarted. In fact, at that point the thread moves into a state we haven't talked about -- ***dead***. In the dead state, the thread has finished its `run()` method and can never be restarted. The `Thread` object might still be on the heap, as a living object that you can call other methods(if appropriate), but the `Thread` object has permanently lost its 'threadness'. In other words, there is no longer a separate call stack, and the `Thread` object is no longer a *thread*. It's just an object, at that point, like all other objects）。
>
> 然而，是有一些构造线程池的设计模式的，在这样的设计模式下，就可以持续使用`Thread`对象来完成不同作业。但不是通过调用死亡线程上的 `restaring()` 方法来完成的（But, there are design patterns for making a pool of threads that you can keep using to perform different jobs. But you don't do it by `restarting()` a dead thread）。


## 重点知识

- Java中带小写字母 `t` 的 `thread`，指的是一条单独的执行线（A thread with a lower-case 't' is a separate thread of execution）；
- Java 中所有线程都有自己的调用栈（Every thread in Java has its own call stack）；
- 带大写字母`T`的 `Thread`，指的是类 `java.lang.Thread`。而`Thread`对象，则表示一条执行线；
- `Thread`对象需要一项去完成的作业。而`Thread`对象的作业，是某个实现了`Runnable`接口的东西；
- `Runnable`接口只有一个方法，那就是`run()`方法。这个方法会去往新调用栈的底部。也就是说，这是在新线程中第一个运行的方法；
- 要启动新线程，就需要将一个`Runnable`类型的对象，传递给类`Thread`的构造器；
- 在已经实例化出一个 `Thread`对象，而尚未调用该对象的 `start()` 方法时，线程是在一种 新建（NEW） 状态中的；
- 在启动了某个线程（通过调用这个 `Thread` 对象的 `start()` 方法）时，就创建出了一个新栈，这个新栈的底部，就是 `Runnable` 类型对象的 `run()` 方法。这个线程现在处于 可运行（RUNNABLE） 状态，等待着被 JVM 选去运行；
- 在 JVM 的线程调度器将某个线程选中去作为当前运行线程时，这个线程就被成为 运行中的 线程。在单个处理器的机器上，某个时刻只能有一个当前运行线程；
- 有的时候线程可从 运行（RUNNING）状态，被移动到 阻塞（临时非可运行，BLOCKING，temporarily non-runnable）状态。线程可能由于等待来自某个流的数据，或由于已进入睡眠，抑或由于等待某个对象的锁，而进入阻塞状态；
- 线程调度是不保证以某种特定方式去运作的，因此就无法确定线程们会良好地排队。通过将线程间歇地置入睡眠状态，可对调度排队施加影响（Thread scheduling is not guaranteted to work in any particular way, so you cannot be certain that threads will take turns nicely. You can help influence turn-taking by putting your threads to sleep periodically）。

### 将线程置为睡眠状态

**Putting a thread to sleep**

帮助线程获取运行机会的最佳方式之一，就是将这些线程定期置于睡眠状态。需要做的仅是调用静态的`sleep()`方法，传递给他以毫秒计算的睡眠时长（One of the best ways to help your threads take turns is to put them to sleep periodically. All you need to do is call the static `sleep()` method, passing it the sleep duration, in milliseconds）。

比如：

```java
Thread.sleep(2000);
```

就好将某个线程从运行状态敲出去，然后让其保持在可运行状态之外两秒钟。这样在至少两秒钟时间过去之前，这个线程是无法再度成为运行线程的（will knock a thread out of the running state, and keep it out of the runnable state for two seconds. The thread *can't* become the running thread again until after at least two seconds have passed）。

有点遗憾的是，这个 `sleep()` 方法会抛出一个 `InterruptedException` 异常，这是一个受检查的异常，因此所有对 `sleep()` 的调用，都必须封装在 `try/catch` 代码块（或被声明出来）。因此一个真正的 `sleep()` 调用，看起来是这样的：

```java
try {
    Thread.sleep(2000);
} catch (InterruptedException ex) {ex.printStackTrace();}
```

线程几乎是绝不会被睡眠中断的；这个异常是在 API 中，为了支持线程通信的机制，而这种机制在现实中几乎不会被用到。不过仍必须遵从异常处理或声明的原则，因此就需要习惯将这个 `sleep()` 方法，封装在 `try/catch` 中（Your thread will probably *never* be interrupted from sleep; the exception is in the API to support a thread communication mechanism that almost nobody uses in the Real World. But, you still have to obey the handle or declare law, so you need to get used to wrapping your `sleep()` calls in a `try/catch`）。

> **在要确保其他线程获得运行时机时，就把某个线程置于睡眠状态（Put your thread to sleep if you want to be sure that other threads get a chance to run）**。
>
> **在线程苏醒时，他回到的总是可运行状态，并等待着线程调度器将其选去再度运行（When the thread wakes up, it always goes back to the runnable state and waits for the thread scheduler to choose it to run again）**。

既然已经清楚在指定的时长 *之前* 线程不会苏醒，那么在 “计时器” 超时后的某个时刻，线程就可能会醒过来吗？答案是可能会也可能不会。这并不重要，真的，因为在线程苏醒时，***总是会回到可运行状态***！线程在指定时间不会自动苏醒并成为当前运行线程。在线程苏醒时，线程仍然是受线程调度器支配的。现在对于那些对分时要求不怎么苛刻的应用，以及那些仅有少数几个线程的应用，其中的线程差不多再苏醒时就会准时（也就是在设定的2000毫秒过后）继续其运行状态。但仍然不能把我们的程序押注在这种可能性上（Now you know that your thread won't wake up *before* the specified duration, but is it possible that it will wake up some time *after* the 'timer' has expired? Yes and no. It doesn't matter, really, because when the thread wakes up, ***it always goes back to the runnable state***! The thread won't automatically wake up at the designated time and become the currently-running thread. When a thread wakes up, the thread is once again at the mercy of the thread scheduler. Now, for applications that don't require perfect timing, and that have only a few threads, it might appear as though the thread wakes up and resumes running right on schedule(say, after the `2000` milliseconds). But don't be your program on it）。

### 运用睡眠来令到程序更可预测

**Using sleep to make our program more predictable**

还记得先前那个在每次运行时都会给出不同结果的示例吧（注：实际上那个示例不会）？请回顾并对代码和样本输出加以研究。有的时候主线程必须等待那个新线程执行完毕（并打印出“位于栈顶部”），而另一些时候新线程在他执行完毕之前会被送回到可运行状态，而允许主线程回来执行并打印出“回到主线程”。那么该怎样来修复这种不确定性呢？请停下来思考一下，然后回答这个问题：“在那个地方可以放入一个`sleep()`的调用，从而确保始终在打印出‘位于栈的顶部’之前，先打印出‘回到主线程’”（Remember our earlier example that kept giving us different results each time we ran it? Look back and study the code and the sample output. Sometimes main had to wait until the new thread finished(and printed "top o' the stack"), while other times the new thread would be sent back to runnable before tit was finished, allowing the main thread to come back in and print out "back in main". How can we fix this? Stop for a moment and answer this question: "Where can you put a `sleep()` call, to make sure the 'back in main' always prints before 'top o' the stack'"）？

在你得出答案前，这里会等待（当然答案不止一个）。

想到答案了吗？

```java
package com.xfoss.learningJava;

public class MyRunnable implements Runnable {
    private String incomingMessage;

    public MyRunnable (String message) {
        incomingMessage = message;
    }

    public void run () {
        go();
    }

    public void go () {
        doMore();
    }

    public void doMore () {
    
        // 在这里调用 sleep() 将强制新线程离开当前运行状态！
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {ex.printStackTrace();}

        // 主线程将再次成为当前运行线程，并打印出 “回到主线程”。随后
        // 在到这条语句打印出“线程栈的顶部......”之前，就会有个
        // 暂停（大概两秒钟时间）了
        System.out.format("线程栈的顶部，收到主线程的消息：%s\n", incomingMessage);
    }
}

class ThreadTester {
    public static void main (String[] args) {
        Runnable threadJob = new MyRunnable("你好，用户线程！");
        Thread myThread = new Thread(threadJob);

        myThread.start();

        System.out.println("回到主线程");
    }
}
```

## 构造并启动两个线程

**Making and starting two threads**

线程是有名称的。可给线程取一个自己选择的名字，或者可以接收线程的默认名称。然而关于线程名称的有趣一面在于，可使用他们来了解哪个线程是在运行中。下面的示例，启动了两个线程。两个线程的作业相同：运行一个循环，每次迭代时打印出当前运行线程的名称（Threads have names. You can give your threads a name of your choosing, or you can accept their default names. But the cool thing about names is that you can use them to tell which thread is running. The following example starts two threads. Each thread has the same job: run in a loop, printing the currently-running thread's name with each iteration）。

```java
package com.xfoss.learningJava;

public class RunThreads implements Runnable {
    private Thread alpha;
    private Thread beta;

    public RunThreads () {
        // 构造两个线程，他们有着同样的 Runnable 实例（
        // 即同样的作业 -- 接下来会讲到更多有关 “两个线程与一个
        // Runnable 作业”这种情况）。
        alpha = new Thread(RunThreads.this);
        beta = new Thread(RunThreads.this);

        // 给这两个线程取名字。
        alpha.setName("Alpha thread");
        beta.setName("Beta thread");
        
        // 启动这两个线程。
        alpha.start();
        beta.start();
    }

    public static void main (String[] args) {
        // 这里构造了一个 Runnable 类型的实例。
        new RunThreads();
    }

    public void run () {
        // 两个线程都会贯通行这个循环，每次迭代
        // 都会打印出线程的名称。
        for (int i = 0; i < 5; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.format("%s is running.\n", threadName);
        }
    }
}
```

经测试（重复运行 20 遍），上面的代码，运行的结果均为：

```console
$ java -jar build/libs/com.xfoss.learningJava-0.0.1.jar 
Alpha thread is running.
Alpha thread is running.
Alpha thread is running.
Alpha thread is running.
Alpha thread is running.
Beta thread is running.
Beta thread is running.
Beta thread is running.
Beta thread is running.
Beta thread is running.
```

将上面的 `RunThreads` 修改为下面这样：

```java
package com.xfoss.learningJava;

public class RunThreads implements Runnable {
    private Thread alpha;
    private Thread beta;

    public RunThreads () {
        // 构造两个线程，他们有着同样的 Runnable 实例（
        // 即同样的作业 -- 接下来会讲到更多有关 “两个线程与一个
        // Runnable 作业”这种情况）。
        alpha = new Thread(RunThreads.this);
        beta = new Thread(RunThreads.this);

        // 给这两个线程取名字。
        alpha.setName("Alpha thread");
        beta.setName("Beta thread");
        
        // 启动这两个线程。
        alpha.start();
        beta.start();
    }

    public static void main (String[] args) {
        // 这里构造了一个 Runnable 类型的实例。
        new RunThreads();
    }

    public void run () {
        for (int i = 0; i < 5; i++) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {ex.printStackTrace();}

            String threadName = Thread.currentThread().getName();
            System.out.format("%s is running.\n", threadName);
        }
    }
}
```


运行结果就变成：

```console
$ java -jar build/libs/com.xfoss.learningJava-0.0.1.jar 
Alpha thread is running.
Beta thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
```

```console
$ java -jar build/libs/com.xfoss.learningJava-0.0.1.jar 
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
```

```console
$ java -jar build/libs/com.xfoss.learningJava-0.0.1.jar 
Alpha thread is running.
Beta thread is running.
Beta thread is running.
Alpha thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
Alpha thread is running.
Beta thread is running.
```

可以看出，输出结果具有不确定性了。

### 将发生什么？

**What will happen**?

这两个线程会依次运行吗？会看到线程名称交替出现吗？线程切换有多频繁？循环的每次迭代都会切换吗？还是在五次循环迭代后会发生线程切换？

答案当然是已知的：*对这些我们一无所知*！这些都取决于调度器。且在指定OS上，以指定JVM，在指定CPU上，每次运行仍会得到极为不同的结果。

在 OS X 10.2（`Jaguar`）下运行此程序，在五次或五次循环迭代以内，线程 Alpha 就会运行完毕，随后线程 Beta 运行完毕。在不同的此程序执行时，这种结果将极为一致。虽然没有保证，但会极为一致。

但在执行到25次或更多的循环迭代时，情况就开始摇摆不定了。线程 Alpha 可能在还没有执行完 25 次循环迭代时，线程调度器就会将其送回到可执行状态，以让线程 Beta 有机会运行（But when you up the loop to 25 or more iterations, things start to wobble. The Alpha thread might not get to complete all 25 iterations before the scheduler sends it back to runnable to let the Beta thread have a chance）。


## 多个流程难道不精彩么？

**Aren't threads wonderful**?

**嗯，是的。（在有多个流程时）存在着一个目前为止尚未揭示的一面。多个线程会导致并发 “问题”（Um, yes. There IS a dark side. Threads can lead to concurrency 'issues'）**。

并发问题导致竞争情形。而竞争情形会导致数据不一致。数据不一致又导致恐惧......你知道接下来会是什么情况了（Concurrency issues lead to race conditions. Race conditions lead to data corruption. Data corruption leads to fear...you know the rest）。

然而这一切都归结于一种潜在的致命场景：两个以上的线程，对同一个对象的 *数据* 进行访问。也就是执行在两个不同线程栈上的一些方法，都在调用, 比如说，内存堆上同一对象的读取器或设置器（It all comes down to one potentially deadly scenario: two or more threads have access to a single object's *data*. In other words, methods executing on two different stacks are both calling, says, getters or setters on a single object on the heap）。

这整个就是“左右手互博”的混乱局面。两个线程丝毫不在意对方，欢快地执行着他们各自的那些方法，他们两都以为自己是那个真正的线程。然而他们不知道的是这里有那么一个重要的事情。在他们各自欢快运行之后，在某个线程不在运行中，而处于可运行状态（或阻塞状态）时，这个线程就基本上被线程调度器打入昏迷状态。而当这个线程再度成为当前运行线程是，他是不知道他曾停下来过的（It's a whole 'left-hand-doesn't-know-what-the-right-hand-is-doing thing. Two threads, without a care in the world, humming along executing their methods, each thread thinking that he is the One True Thread. The only one that matters. After all, when a thread is not running, and in runnable(or blocked) it's essentially knocked unconscious. When it becomes the currently-running thread again, it doesn't know that it ever stopped）。

### 陷入麻烦中的婚姻

**这两口子还有救吗**？

***接下来，请观看一节非常特别的 Dr. Steve 秀（Next, on a very special Dr. Steve Show）***

[转载自第 42 集（Transcript from episode #42）]

欢迎来到斯蒂夫博士秀场。

今天的故事，是完全围绕两口子闹掰的两大原因 -- 管钱和睡觉，而展开的（We've got a story today that's centered around the top two reasons why couple split up -- finances and sleep）。

今天的问题夫妻，是 Ryan 和 Monica, 他们睡一张床，把钱放在一起。如果找不到法子，他俩也就长久不下去了。问题是啥？就是那个很悠久的 “两人一个银行账户”的事情。

Monica 是这么跟我讲的：

“Ryan 和我商量好了，谁也不要透支这个银行账户。所以就有这样的手续，不管谁要取钱，都要事先检查一下账户余额。这本来就是个简单的事情。可是突然我们俩都透支了，还被扣了透支费！

我觉得这是不可能的发生的，我觉得我两的手续没有问题。然而下面的事情还是发生了：

Ryan有次需要取 $50，所以他检查了账户余额，看到账户里有 $100。这当然没有问题，他就即将取钱了。**但是这个时候他睡着了**！

就在他睡着的时候，我来了，现在我要取 $100。于是我检查了余额，发现有 $100（因为 Ryan 还在睡觉而没有取出他的 $50），那么我就想，取 $100 没有问题。因此就把那 $100 取出来了，到这里还是没毛病。可是在 Ryan 醒来后，继续了他的取钱，这个时候突然间我们就透支了！他甚至不知道他曾睡着过，所以他就在没有再次检查账户余额的情况下，直接完成了他取钱这个事。你一定得帮帮我们啊，斯蒂夫博士”！

有办法吗？他们就这样完了吗？这里没办法让 Ryan 不睡觉，不过可以确保在 Ryan 醒来之前，不让 Monica 去动银行账户吗？

在切进广告期间，请花点时间来思考这个问题。

### Ryan 与 Monica 问题的代码实现

**The Ryan and Monica problem, in code**

下面的示例，展示了在 *两个* 线程共用 *同一个* 对象时，所发生的事情（The following example show what can happen when *two* threads(Ryan and Monica) share a *single* object(the bank account)）。

该代码有两个类，`BankAccoutn` 与 `MonicaAndRyanJob`，类 `MonicaAndRyanJob` 实现了 `Runnable`，并表示 Ryan 与 Monica 俩都具备的行为 -- 检查账户余额并进行取款操作。然而显然，两个线程在检查余额和真正取钱 *之间*，会睡过去的。

类`MonicaAndRyanJob`有个类型为 `BankAccount`的实例变量，表示他们共用的账户。

![Ryan和Monica问题代码实现的设计](images/Ch15_41.png)

*图 41 - Ryan和Monica问题代码实现的设计*

此代码工作原理如下：

1) **构造一个 `RyanAndMonicaJob`的实例**


    类`RyanAndMonicaJob`是 `Runnable`（即要执行的作业）的，同时 Monica 和 Ryan 都进行同样的事（检查余额与取钱），因此这里只需要一个实例。

    ```java
    RyanAndMonicaJob theJob = new RyanAndMonicaJob();
    ```

2) **以这同样的 `Runnable` 实例（即上面那个 `RyanAndMonicaJob`实例），构造两个线程**


    ```java
    Thread one = new Thread(theJob);
    Thread two = new Thread(theJob);
    ```

3) **分别对这两个线程进行命名和启动**


    ```java
    one.setName("Ryan");
    two.setName("Monica");

    one.start();
    two.start();
    ```

4） **同时对两个线程执行他们的 `run()` 方法加以观察**（检查余额并取钱）


    一个线程代表 Ryan, 另一个表示 Monica。两个线程都持续对余额进行检查，并随后取出一笔钱，不过只有在不透支的情况下才支取！

    ```java
    if (accout.getBalance() >= amount) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {ex.printStackTrace();}
    }
    ```

**在方法 `run()` 里头，完成 Ryan 和 Monica 正要做的 -- 检查账户余额，并在有足够钱的情况下，取出一笔钱**。

**这样处理应该可以保护到免受透支的问题**。

**除非......Ryan 和 Monica 在检查完余额后、在完成账户支取前，总是会睡过去**。


### Ryan和Monica 示例

**The Ryan and Monica example**

```java
package com.xfoss.learningJava;

class BankAccount {
    // 账户以余额 100 块开始。
    private int balance = 100;

    public int getBalance () {
        return balance;
    }

    public void withdraw (int amount) {
        balance = balance - amount;
    }
}

public class RyanAndMonicaJob implements Runnable {
    // 只有一个 RyanAndMonicaJob 的实例。这就意味着只有一个
    // 银行账户的实例。两个线程都将对这个账户进行操作。
    private BankAccount account = new BankAccount ();

    // 这是 RyanAndMonicaJob 的构造函数。
    public RyanAndMonicaJob () {
        // 构造两个线程，把同样的这个 Runnable 作业交给这两个
        // 线程。这就意味着两个线程都将对这个Runnable类型类
        // 中的账户实例进行操作。
        Thread one = new Thread(RyanAndMonicaJob.this);
        Thread two = new Thread(RyanAndMonicaJob.this);

        one.setName("Ryan");
        two.setName("Monica");

        one.start();
        two.start();
    }

    public static void main (String[] args) {
        new RyanAndMonicaJob();
    }

    public void run () {
        // 在这个 run() 方法中，线程会遍历循环，并尝试在
        // 每次迭代中对银行账户进行支取。在每次支取后，会
        // 再次检查余额，看账户是否已经透支。
        for (int x = 0; x < 10; x++) {
            int random;

            while (true) {
                double mathRandom = Math.random();

                if ( mathRandom < 0.1) continue;
                else {
                    random = (int) (mathRandom * 10);
                    break;
                }
            }

            makeWithdrawal(random*10);
            if (account.getBalance() < 0) {
                System.out.format("账户已透支！余额为 %d\n", accout.getBalance());
                break;
            }
        }
    }

    private void makeWithdrawal (int amount) {

        String currentThread = Thread.currentThread().getName();

        System.out.format("%s 即将进行支取，数额为 %d, 此时余额为 %d\n", 
                currentThread, amount, account.getBalance());

        // 检查余额，在余额不足以进行支取时，只打印一条消息。而有
        // 足够支取的余额时，进入睡眠，随后在醒过来并完成支取，就
        // 跟上面 Ryan 做的那样。
        if (account.getBalance() >= amount){

            try {
                System.out.format("%s 即将睡过去\n", currentThread);

                Thread.sleep(500);
            } catch (InterruptedException ex) {ex.printStackTrace();}

            System.out.format("%s 醒过来了\n", currentThread);

            account.withdraw(amount);
            System.out.format("%s 完成了支取，支出数额 %d, 此时账户余额为 %d\n", 
                    currentThread, amount, account.getBalance());
        }
        else {
            // 程序中放了很多 System.out.format 语句，为的是可以看到在程序
            // 运行时都发生了些什么。
            System.out.format("抱歉，%s, 已经余额不足\n", currentThread);
        }
    }
}
```

该程序某次运行的输出为：

```console
$java -jar build/libs/com.xfoss.learningJava-0.0.1.jar                         ✔ 
Monica 即将进行支取，数额为 80, 此时余额为 100
Ryan 即将进行支取，数额为 30, 此时余额为 100
Ryan 即将睡过去
Monica 即将睡过去
Ryan 醒过来了
Monica 醒过来了
Monica 完成了支取，支出数额 80, 此时账户余额为 -10
Ryan 完成了支取，支出数额 30, 此时账户余额为 70
账户已透支！余额为 -10
账户已透支！余额为 -10
```

**方法 makeWithdrawal() 始终会在支取一边款项之前，对余额进行检查，然而还是透支了账户**。

**这是一种情形**：

Ryan 查看了余额，发现有足够的钱，然后睡了过去。

与此同时，Monica 来了，并查看了余额。她也发现账户里有足够的钱。她不知到Ryan要醒过来并完成支取。

Monica 睡了过去。

Ryan 醒了，并完成了支取。

Monica 醒了，并完成了支取。这里就有个大问题了！在Monica查看余额和取出一笔钱的期间，Ryan 醒过来了并从账户里取走了钱。

**Monica 之前对账户的检查，是无效的，这是由于Ryan虽然完成了对账户余额的检查，但Monica 检查的时间，还是在Ryan取钱的过程中（Monica's check of the account was not valid, because Ryan had already checked and was still in the middle of making a withdrawal）**。

那么在 Ryan 醒过来并完成他的支取事务之前，Monica就必须停止触及银行账户。相反在 Monica 醒过来之前，Ryan也必须停止触及银行账户。


### 他们需要一把账户访问的锁！

**They need a lock for account access**!

**该锁像这样运作**：

1） 存在一把与银行账户事务（检查余额并支取款项）关联的锁。锁只有一把钥匙，在没有人要访问银行账户的时候，锁与钥匙是在一起的。


    ![事务锁的初始状态](images/Ch15_42.png)

    *图 42 - 事务锁的初始状态*


2) 在 Ryan 打算访问银行账户时（去查看余额及支取款项），他就要锁上那把锁并把钥匙揣在自己兜里。现在，在Ryan交出钥匙前，就没有人可以对该账户进行访问了。


    ![给事务锁上锁](images/Ch15_43.png)


    *图 43 - 给事务锁上锁*


3) **在Ryan完成有关银行账户事务的操作之前，他都是把这把钥匙揣在兜里的**。此时他有着唯一的那把钥匙，因此在 Ryan 解锁账户并退还钥匙之前，Monica 就无法对银行账户（或支票簿）进行访问。

现在，就算Ryan在查看了余额后睡着了，他也得到了在他醒过来后余额与他检查时保持一致的保证，这正是由于他在睡着时，保留着事务锁的钥匙！

![事务锁的释放](images/Ch15_44.png)


*图 44 - 事务锁的释放*


### 这里就需要 `makeWithdrawal()`方法以类似原子的方式运行。⚛️

**We need the `makeWithdrawal()` method to run as one *atomic* thing**.⚛️

这里需要确保一旦某个线程进入了 `makeWithdrawal()`方法，就要允许这个线程，在其他线程可以进入这个方法之前，执行完这个方法（We need to make sure that once a thread enters the `makeWithdrawal()` method, *it must be allowed to finish the method* before any other thread can enter）。

也就是说，这里需要确保一旦某个线程已检查了账户余额，那么那个线程就会被保证，*在其他线程可以检查账户余额之前*，此线程能够醒过来并完成支取（In other words, we need to make sure that once a thread has checked the account balance, that thread has a guarantee that it can wake up and finish the withdrawal *before any other thread can check the account balance*）！

那么就要使用关键字 `synchronized`，来对方法加以修改，如此在某个时刻，就只有一个线程可以方法这个方法了（Use the `synchronized` keyword to modify a method so that only one thread at a time can access it）。

![关键字`synchronized`](images/Ch15_45.png)


*图 45 - 关键字`synchronized`*

**关键字`synchronized`表示，线程为了访问这些同步代码，是需要一把钥匙的（The `synchronized` keyword means that a thread needs a key in order to access the synchronized code）**。

**为了对数据（好比这里的银行账户）进行保护，就要将对数据进行操作的那些方法，进行同步化处理**。

这就是对银行账户施加保护的措施！不是把锁安在银行账户本身之上；而是给操作银行账户的事务加上锁。这样的话，某个线程就要去完成整个事务，就算线程执行到方法的一半睡了过去，也要有始有终（That's how you protect the bank account! You don't put a lock on the bank accout itself; you lock the method that does the banking transaction. That way, one thread gets to complete the whole transaction, start to finish, even if that thread falls asleep in the middle of the method）！

那么问题来了，既然没有给银行账户加锁，那到底锁了什么呢？是那个方法吗？还是那个 `Runnable` 对象？还是线程本身？

接下来会讨论整个问题。从代码上看，这是很简单的 -- 只需将 `synchronized` 修饰符，加到方法声明即可：

```java
private synchronized void makeWithdrawal (int amount) {

    String currentThread = Thread.currentThread().getName();

    System.out.format("---------\n%s 即将进行支取，支取数额为 %d\n", 
            currentThread, amount, account.getBalance());

    if (account.getBalance() >= amount){
        try {
            System.out.format("%s 即将睡过去\n", currentThread);

            Thread.sleep(500);
        } catch (InterruptedException ex) {ex.printStackTrace();}

        System.out.format("%s 醒过来了\n", currentThread);

        account.withdraw(amount);
        System.out.format("%s 完成了支取，支出数额 %d, 此时账户余额为 %d\n", 
                currentThread, amount, account.getBalance());
    }
    else {
        System.out.format("抱歉，%s, 已经余额不足, 余额为：%d\n", currentThread, account.getBalance());
    }
}
```

（致那些精通物理的本书读者朋友：是的，此处使用 “原子” 一词的惯例，并未反应整个亚原子的粒子事物。当在线程及事务语境下讲到“原子”时，就要做牛顿，而不是爱因斯坦。嘿，这可不是 写这本书的人 的约定。真要由“我们”来约定的话，就要用海森堡不确定原理，来描述任何与线程相关的东西了。）

### 运用对象锁

**Using an object's lock**

每个对象都有一把锁。多数时候，那把锁都是开着的，同时可设想对象旁边始终有那么一把钥匙在那里。这些对象锁，只会在有着同步方法时，才生效。在某个对象有着一个或多个同步方法时，*线程就只有在可以获取到这个对象的锁时，才能进入到同步方法*（Every object has a lock. Most of the time, the lock is unlocked, and you can imagine a virtual key sitting with it. Object locks come into play only when there are synchronized methods. When an object has one or more sychronized methods, *a thread can enter a synchronized method only if the thread can get the key to the object's lock*）！

这些锁不是基于每个 *方法* 的，他们是基于每个 *对象* 的。在某个对象有两个同步方法时，也不是简单表明不可以让两个线程同时进入同一方法。而是表示不能让两个线程，同时进入这两个同步方法的 *任意一个* （The locks are not per *method*, they are per *object*. If an object has two synchronized methods, it does not simply mean that you can't have two threads entering *any* of the synchronized methods）。

请设想一下这个情况。在有着多个可以潜在对某个对象的多个实例变量，进行操作的方法时，那么全部这些方法，都需要使用 `synchronized` 保护起来（Think about it. If you have multiple methods that can potentially act on an object's instance variables, all those methods need to be protected with `synchronized`）。

同步的目的，是要保护重要数据。但请记住，锁住的并非数据本身，而是将那些要 *存取* 数据的方法，进行了同步化改造（The goal of synchronization is to protect critical data. But remember, you don't lock the data itself, you synchronize the methods that *access* that data）。

![关于对象的锁(object's lock)](images/Ch15_46.png)


*图 46 - 关于对象的锁(object's lock)*

> **每个 Java 对象都有一把锁。一把锁也仅有一把钥匙**。
>
> **多数时候，对象锁都是开着的，也没有人在乎他开着**。
>
> **而在对象有了一些同步方法时，线程就只有在可以拿到对象锁的钥匙时，才能进入到这些同步方法之一中去执行了**。也就是说，只有在另一方法不再持有那把钥匙的时候。

那么在某个线程自底往上贯穿其调用栈（从线程作业的那个 `run()` 方法开始），而突然碰到一个同步化方法时，会发生什么呢？这个时候线程就会意识到，他需要在进入这个同步化方法前，获取那个对象的钥匙。他会查找那把钥匙（这都是由JVM处理的；Java中没有访问对象锁的API），并在钥匙可用时，线程就会抓取到钥匙而进入到那个同步方法。

而在这个时间点之前，线程就会纠结于那把锁，就好像线程的存亡，取决于这个锁一样。在线程执行完毕这个同步方法之前，他是不会放弃这把锁的。进而在此线程持有这把锁期间，就不会由其他线程，能够进入到 *任何* 该对象的那些同步方法，这是由于那个对象的唯一钥匙，已不再可用（From that point forward, the thread hangs on to that key like the thread's life depends on it. The thread won't give up the key until it completes the synchronized method. So while that thread is holding the key, no other threads can enter *any* of that object's synchronized methods, because the one key for that object won't be available）。

### 可怕的“更新丢失”问题

**The dreaded "Lost Update" problem**

这是另外一个经典的、来自于数据库领域的并发问题。这个问题与Ryan和Monica的故事密切相关，不过这里是要用这个示例，来说明几个其他的要点（Here's another classic concurrency problem, that comes from the database world. It's closely related to the Ryan and Monica story, but we'll use this example to illustrate a few more points）。

这里所说的更新丢失，是围绕这样一个过程展开的：

- 步骤一：获取账户中的余额

```java
int i = balance;
```


- 步骤二：给余额加 1

```java
// 或许这个不是个原子过程（Probably not an atomic process）
balance = i + 1;
```

即使这里是要更为常见的语法：`balance++;` 同样不能确保编译后的代码，将是一个 “原子化的过程（atomic process）”。事实上，这个过程根本就不会是个原子化的过程（In fact, it probably won't）。

在这个“更新丢失”问题中，有着两个线程，他们都尝试要对余额进行递增操作。请阅读下面的代码，然后就会看到真正的问题所在。

```java
package com.xfoss.learningJava;

class TestSync implements Runnable {
    private int balance;

    public void run () {
        String threadName = Thread.currentThread().getName();

        for (int i = 0; i < 10; i++){
            increment();
            System.out.format("%s 存了一块钱，现在余额为 %d\n", threadName, balance);
        }
    }

    private void increment () {
        int i = balance;
        balance = i + 1;
    }

    public TestSync () {
        Thread a = new Thread(TestSync.this);
        Thread b = new Thread(TestSync.this);

        a.setName("Ryan");
        b.setName("Monica");

        a.start();
        b.start();
    }

    public static void main (String[] args) {
        new TestSync();
    }
}
```


此程序运行结果：

```console
> java -jar .\Pictures\com.xfoss.learningJava-0.0.1.jar
Ryan 存了一块钱，现在余额为 1
Ryan 存了一块钱，现在余额为 3
Ryan 存了一块钱，现在余额为 4
Ryan 存了一块钱，现在余额为 5
Ryan 存了一块钱，现在余额为 6
Monica 存了一块钱，现在余额为 2
Monica 存了一块钱，现在余额为 8
Monica 存了一块钱，现在余额为 9
Monica 存了一块钱，现在余额为 10
Monica 存了一块钱，现在余额为 11
Monica 存了一块钱，现在余额为 12
Monica 存了一块钱，现在余额为 13
Monica 存了一块钱，现在余额为 14
Monica 存了一块钱，现在余额为 15
Monica 存了一块钱，现在余额为 16
Ryan 存了一块钱，现在余额为 7
Ryan 存了一块钱，现在余额为 17
Ryan 存了一块钱，现在余额为 18
Ryan 存了一块钱，现在余额为 19
Ryan 存了一块钱，现在余额为 20
```


修改为这样后:

```java
package com.xfoss.learningJava;

class TestSync implements Runnable {
    private int balance;

    public void run () {
        String threadName = Thread.currentThread().getName();

        // 每个线程循环10次，每次循环将余额递增
        for (int i = 0; i < 10; i++){
            increment();
            System.out.format("%s 存了一块钱，现在余额为 %d\n", threadName, balance);
        }
    }

    private void increment () {
        // 这里是重要的地方！对余额的递增，是通过加 1 到之前读取余额时余额
        // 的值上的（而不是加 1 到当前的那个余额值上）
        //
        // Here's the crucial part! We increment the balance by adding
        // 1 to whatever the value of balance was AT THE TIME WE READ
        // IT (rather than adding 1 to whatever the CURRENT value is)
        int i = balance;
        balance = i + 1;
    }
}

public class TestSyncTest {
    public static void main (String[] args) {
        TestSync job = new TestSync();

        Thread a = new Thread(job);
        Thread b = new Thread(job);

        a.setName("Ryan");
        b.setName("Monica");

        a.start();
        b.start();
    }
}
```

运行结果为：

```console
> java -jar .\Pictures\com.xfoss.learningJava-0.0.1.jar
Ryan 存了一块钱，现在余额为 1
Ryan 存了一块钱，现在余额为 3
Ryan 存了一块钱，现在余额为 4
Ryan 存了一块钱，现在余额为 5
Ryan 存了一块钱，现在余额为 6
Ryan 存了一块钱，现在余额为 7
Ryan 存了一块钱，现在余额为 8
Ryan 存了一块钱，现在余额为 9
Ryan 存了一块钱，现在余额为 10
Ryan 存了一块钱，现在余额为 11
Monica 存了一块钱，现在余额为 2
Monica 存了一块钱，现在余额为 12
Monica 存了一块钱，现在余额为 13
Monica 存了一块钱，现在余额为 14
Monica 存了一块钱，现在余额为 15
Monica 存了一块钱，现在余额为 16
Monica 存了一块钱，现在余额为 17
Monica 存了一块钱，现在余额为 18
Monica 存了一块钱，现在余额为 19
Monica 存了一块钱，现在余额为 20
```


> **注**：实际上，上面两种写法，是不是一样的效果呢？


### 下面来运行一下这个程序......

**Let's run this code...**

1) 线程 A 运行一会儿


    ```pseudocode
    将余额值放到变量 i 中；
    余额为 0，因此 i 现在就是 0；
    将余额的值设置为 i+1 的结果；
    现在余额为 1；
    将余额的值放到变量 i 中；
    余额为 1，因此 i 现在就是 1；
    将余额值设置为 i+1 的结果；
    现在余额为 2；
    ```


2) 线程 B 运行一会儿


    ```pseudocode
    将余额值放到变量 i 中；
    余额为 2，因此 i 现在就是 2；
    将余额的值设置为 i+1 的结果；
    现在余额为 3；
    将余额的值放到变量 i 中；
    余额为 1，因此 i 现在就是 3；

    [此时，在线程 B 将余额值设置为 4 之前，就从正在运行
    被送回到了可运行状态]
    ```


3) 线程 A 再度运行，在他之前离开的地方继续（Thread A runs again, picking up where it left off）


    ```pseudocode
    将余额值放到变量 i 中；
    余额为 3，因此 i 现在就是 3；
    将余额的值设置为 i+1 的结果；
    现在余额为 4；
    将余额的值放到变量 i 中；
    余额为 4，因此 i 现在就是 4；
    将余额值设置为 i+1 的结果；
    现在余额为 5；
    ```


4) 线程 B 再度运行，并精确地在他之前离开的地方继续（Thread B runs again, and pickes up exactly where it left off）!


    ```pseudocode
    将余额值设置为 i+1 的结果；
    // 哎呀！！
    //
    // 线程 A 将余额更新为了 5，但现在线程 B 回来继续运行，而
    // 践踏了线程 A 所做出的更新，就如同线程 A 的更新从来也
    // 没发生过一样。
    //
    // Thread A updated it to 5, but now B came back and stepped
    // on top of the update A made, as if A's update never happened.
    现在余额为 4；
    ```


> **这里就丢失了线程 A 所做出的那些更新！由于线程 B 先前完成了余额的 “读取”，且在线程 B 醒来后，就只是如同他不曾错过一些事情一样，继续运行着（We lost the last updates that Thread A made! Thread B had previously done a 'read' of the value of balance, and when B woke up, it just kept going as if it never missed a beat）**。

### 将方法 `increment()` 构造成原子化。使其成为同步方法！⚛️

**Make the `increment()` method atomic. Synchronize it**!⚛️

同步化 `increment()` 方法，就解决了 “更新丢失” 问题，这是因为同步化就让该方法中的两个步骤，成为了一个不可分割的单元了（Synchronizing the `increment()` method solves the "Lost Update" problem, because it keeps the two steps in the method as one unbreakable unit）。


```java
private synchronized void increment () {
    balance++;
}
```


> **一旦线程进入到这个方法，就必须确保在其他线程可以进入这个方法之前，其中所有步骤执行完毕（作为一个原子过程）**。


## 答疑

- **听起来把万物都同步化是个不错的注意，因为这样就可以实现线程安全了（Sounds like it's a good idea to synchronize everything, just to be thread-safe）。**

> 不是这样的，这不是个好主意。同步化并非没有代价。首先，同步方法是有确切开销的。也就是说，在代码遇到同步方法时，就会存在性能问题（虽然通常不会注意到这样的性能问题），这是由于解决 “对象锁的钥匙是否可用” 的这个问题需要时间（Nope, it's not a good idea. Synchronization doesn't come for free. First, a synchronized method has a certain amount of overhead. In other words, when code hit a synchronized method, there's going to be a performance hit(although typically, you'd never notice it) while the matter of "is the key available?" is resolved）。
>
> 其次，由于同步化操作对并发有限制，因此同步方法会减慢程序运行速度。换句话说，同步方法会强制其他线程排队等待他们的运行轮次。这可能在你编写的代码中不成其为问题，但必须考虑到这个问题（Second, a synchronzied method can slow your program down because synchronization restricts concurrency. In other words, a synchronzied method forces other threads to get in line and wait their turn. This might not be a problem in your code, but you have to consider it）。
>
> 第三，也是最可怕的，就是同步方法会导致死锁！（请参阅后面的部分，Third, and most frightening, synchronized methods can lead to deadlock）!
>
> 有一条良好的经验法则，就是仅做最低限度、对那些确实应该进行同步的方法进行同步化。并且事实上，可以在较方法更小的级别的粒度上，进行同步化操作。虽然在本书中没有用到，但仍然可以使用 `synchronize` 关键字，在一个或多个语句级别，而不是整个方法层面，进行更细粒度的同步化操作（A good rule of thumb is to synchronize only the bare minimum that should be synchronized. And in fact, you can synchronize at a granularity that's even smaller than a method. We don't use it in the book, but you can use the `synchronized` keyword to synchronize at the more fine-grained level of one or more statements, rather than at the whole-method level）。


```java
// go() 方法中的 doStuff() 不需要被同步化，因此这里就
// 没有将整个的 go() 方法同步化。
//
// doStuff() doesn't need to be synchronized, so we don't
// synchronize the whole method.
public void go () {
    doStuff();

    // 此时，就只有这其中的两个方法调用被编为一个原子化
    // 单元了。当在某个方法中使用 synchronized 关键字，而
    // 不是在方法声明中使用整个关键字时，就必须给提供一个
    // 参数，这个参数即为线程需要获得对象锁钥匙所对应的对象。
    //
    // Now, only these two method calls are grouped into
    // one atomic unit. When you use the synchronized keyword
    // WITHIN a method, rather than in a method declaration, you
    // have to provide an argument that is the object whose key
    // the thread needs to get.
    //
    // 尽管也有其他实现方式，但几乎总是会在当前对象（this）上
    // 进行同步化。那正是在整个方法被同步时，所要锁上的同一个
    // 对象。
    //
    // Although there are other ways to do it, you will almost
    // always synchronize on the current object(this). That's the
    // same object you'd lock if the whole method were synchronized.
    synchronized (this) {
        criticalStuff();
        moreCriticalStuff();
    }
}
```

1) **线程 A 运行一会儿**


    ```pseudocode
    尝试进入这个 increment() 方法；

    这个方法是同步的，因此就要 获取到此对象锁的钥匙
    把余额的取值，放入变量 i 中；
    余额是 0，因此 i 现在就是 0；
    将余额的至，设置为 i+1 的运算结果；
    现在余额就是 1；
    交回这把钥匙（由于线程A已经完成了 increment() 方法的执行）；
    再次进入 increment() 方法并获取到钥匙；
    将余额的值放入变量 i 中；
    余额为 1，因此 i 现在就是 1；

    [现在线程 A 被送回到可运行状态，不过由于他尚未完成这个同步方法的
    执行，因此线程 A 仍然拿着那把钥匙]
    ```

2) **线程 B 被（线程调度器）选中去运行**


    ```psedudocode
    尝试进入这个 increment() 方法。该方法是同步的，因此这里就需要获取到锁的钥匙；

    钥匙不可用。

    [现在线程 B 被送进一个 ‘对象锁不可用’ 类似休息室的
    地方（now thread B is sent into a 'object lock not available' lounge）]
    ```

3) **线程 A 再度运行，从他上次离开的地方继续运行（请记住，他仍然持有那把对象锁的钥匙）**


    **Thread A runs again, picking up where it left off(remember, it still has the key)**

    ```pseudocode
    将余额的值设置为 i+1 的运算结果；
    现在余额为 2；

    线程 A 退回对象锁的钥匙；

    [现在线程 A 被送回到可运行状态，但由于他已经完成了这个 increment()
    方法的运行，这个线程就不再持有那把对象锁了]
    ```

4) **线程 B 被（线程调度器）选中去运行**


    ```pseudocode
    线程 B 尝试进入这个 increment() 方法。该方法是同步的，因此这里
    就需要获取到对象锁的钥匙；

    这次这把钥匙是可用的，那么就获取到该钥匙了；

    将余额的值放入到变量 i 中；

    [继续运行......]
    ```

### 同步化的致命一面

**The deadly side of synchronization**

在运用同步代码时要谨慎，这是因为没有什么东西会像线程死锁那样，将程序拖入到动弹不得的地步。在有着同时持有对方所需对象锁钥匙的两个进程时候，就会发生线程死锁。这种情形无可救药，没有解决办法，因此这两个线程就只有坐在那里静静等待。再等待。再等待（Be careful when you use synchronized code, because nothing will bring your program to its knees like thread deadlock. Thread deadlock happens when you have two threads, both of which are holding a key the other thread wants. There's no way out of this scenario, so the two threads will simply sit and wait. And wait. And wait）。

若熟悉数据库或其他一些应用服务器，那么就会明白这个问题；数据库通常有着类似同步化这样的机制。不过一种真正的事务管理系统，一些时候是可以解决死锁问题的。这样的系统会作出假设，比如在两个事务耗费过长时间完成时，就相当于发生了死锁。然而与Java不同，应用服务器是可以执行一次 “事务回滚”，从而将被回滚事务的状态，退回到这个事务（所谓事务，即是指那个原子部分）开始之前（If you're familiar with databases or other application servers, you might recognize the problem; databases often have a locking mechanism somewhat like synchronizaion. But a real transaction management system can sometimes deal with deadlock. It might assume, for example, that deadlock might have occured when two transactions are taking too long to complete. But unlike Java, the application server can do a "transaction rollback" that returns the state of the rolled-back transaction to where it was before the transaction(the atomic part) began）。

Java 不具备处理死锁的机制。他甚至不会 *获悉到* 死锁的发生。那么就要依赖程序员来精心设计。在发现正写着为数不少的多线程代码时，那么大概就要去研修一下 Scott Oaks 和 Henry Wong 合著的 《Java 线程》（"Java Threads"）一书，以掌握避开死锁的一些设计技巧。这些技巧中最常见的一个，就是要留意涉及到的那些线程启动顺序（Java has no mechanism to handle deadlock. It won't even *know* deadlock occured. So it's up to you to design carefully. If you find yourself writing much multithreaded code, you might want to study "Java Threads" by Scott Oaks and Henry Wong for design tips on avoiding deadlock. One of the most common tips is to pay attention to the order in which your threads are started）。

> **死锁发生的必要条件即是，存在两个对象与两个线程**。
>
> **All it takes for deadlock are two objects and two threads**.

**一个简单的死锁场景**：

1) 线程 A 进入到对象 `foo` 的某个同步方法，同时获取到了该对象锁的钥匙；


    然后线程 A 拿着 `foo` 对象锁的钥匙睡了过去。


2) 线程 B 进入到对象 `bar` 的某个同步方法，同时获取到该 `bar` 对象锁的钥匙；


    线程 `B` 尝试进入 `foo` 对象的某个同步方法，然而线程 B 无法获取到那把钥匙（由于线程 A 持有着）。这时线程 B 就会去到等待室，知道那把 `foo` 的钥匙可用。这时 `bar` 的钥匙是由线程 B 保留着的。

3) 线程 A 醒了过来（仍就持有着 `foo` 的钥匙），并尝试进入到对象 `bar` 的某个同步方法，然而线程 A 没法获取到那把钥匙，这时因为线程 B 持有着那把钥匙。这个时候线程 A 就会进入等待室，直到 `bar` 的钥匙可用（不过这钥匙将永远不会可用！）


    线程 A 在获取到 `bar` 的钥匙之前无法运行，然而这个时候`bar`的钥匙是在线程 B 手里，同时线程 B 又由于线程 A 持有着 `foo` 的钥匙也无法运行......

## 重点

- 静态方法 `Thread.sleep()` 会强制线程，在传递给这个 `sleep` 方法的参数时长之内，离开运行状态。比如`Thread.sleep(200)`会将线程置于睡眠状态 200 毫秒；
- `sleep()`方法会抛出一个受检查的异常（`InterruptedException`），因此所以对 `sleep()`方法的调用，都必须封装在 `try/catch`代码块中，或是被声明出来；
- 尽管不能保证某个线程在醒来时可以到达可运行行程队列的末尾，但为了有助于确保全部线程都有机会运行，仍就可使用 `sleep()` 方法。醒来的线程，依然可能会回到可运行队列的前端。在多数情况下，为保持手头的线程良好切换，只需编写一些时间设置恰当的 `sleep()` 方法即可（You can use `sleep()` to help make sure all threads get a chance to run, although there's no guarantee that when a thread wakes up it'll go to the end of the runnable line. It might, for example, go right back to the front. In most cases, approciately-timed `sleep()` calls are all you need to keep your threads switching nicely）；
- 使用 `setName()` 方法（是的，又一个惊喜），就可以给某个线程取名字。虽然所有线程都有默认名字，不过给他们一个显式的名字，可以有助于对这些线程加以追踪，尤其是在使用 `print` 语句进行程序调试的时候；
- 在运用线程时，而又存在两个或多个线程都对内存堆上的某个对象进行访问时，就会遇到严重问题；
- 在两个或多个线程同时访问同一对象时，若一个线程在仍处于对某个对象的重要状态进行操作的过程中，就离开了他的正在运行状态，这样就会导致数据损坏（Two or more threads accessing the same object can lead to data corruption if one thread, for example, leaves the running state while still in the middle of manipulating an object's critical state）；
- 要让对象成为线程安全，就要找出哪些语句应作为原子过程加以对待。也就是说，要找出（线程中的）哪些方法，必须在其他线程进入到这些同一对象上的相同方法之前，就要运行完毕（To make your objects thread-safe, decide which statements should be treated as one atomic process. In other words, decide which methods must run to completion before another thread enters the same method on the same object）；
- 在要防止有两个线程进入到某个方法时，就要使用关键字 `synchronized` 来修正这个方法的声明；
- 每个对象都有唯一一把锁，那把锁的钥匙也只有一把。多数时候不需关心那把锁；只有在对象有着同步方法时，那把锁才会生效；
- 在线程尝试进入某个同步方法时，该线程必须获取到其所操作对象（线程的方法将尝试操作的对象）的钥匙。在钥匙不可用时（由于另一线程已持有该钥匙），那么线程就会进入到类似等待室的某个地方，直到那把钥匙可用为止；
- 即使某个对象有着多个同步方法，他仍然只有一把钥匙。一旦有线程进入到那个对象的某个同步方法，就不会有其他线程可以进入到这个对象的其他任何同步方法。这样的限制，实现了通过将那些对该对象数据进行操作的方法，进行同步化而对数据进行保护的目的（Even if an object has more than one synchronized method, there is still only one key. Once any thread has entered a synchronized method on that object, no thread can enter any other synchronized method on the sam object. This restriction lets you protect your data by synchronizing any method that manipulates the data）。

## 新的改进过的 `SimpleChatClient`

回到本章刚开始的时候，那里构建了可将外送消息发送到服务器，但无法收到任何东西的 `SimpleChatClient`。还记得吗？由于在那里需要一次干两件事的方法：在将消息发送给服务器（用户与GUI进行交互）的同时，要从服务器读取传入的消息，进而将这些传入消息显式在那个滚动文本区；因此那正是首次涉及到这整个线程问题的地方。

> 是的，本章确实有这么一个结尾。然而现在还不是......
>
> Yes, there really IS an end to this chapter. But not yet...


```java
package com.xfoss.SimpleChat;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient extends JFrame {
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public static void main (String[] args) {
        new SimpleChatClient("简单的聊天客户端");
    }

    // 这里是 SimpleChatClient 的构造函数，是些已经见到过的 GUI
    // 代码。只是其中增加了两句构造新的 readerThread 并启动这个
    // 线程的语句。
    //
    // This is mostly GUI code you've seen before. Nothing special
    // except the highlighted part where we start the new 'reader'
    // thread.
    public SimpleChatClient (String winTitle) {
        super(winTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(20);

        JButton sendBtn = new JButton("发送");
        sendBtn.addActionListener(new SendBtnListener());

        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendBtn);

        setUpNetworking();

        // 这里使用一个新的内部类，作为新线程的 Runnable(作业)，启动
        // 了一个新线程。该线程的作业，是从服务器的套接字流进行读取，之后
        // 在滚动文本区显示出全部传入消息。
        //
        // We're starting a new thread, using a new inner class as the 
        // Runnable(job) for the thread. The thread's job is to read 
        // from the server's socket stream, displaying any incoming
        // messages in the scrolling text area.
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        getContentPane().add(BorderLayout.CENTER, mainPanel);
        setSize(800, 600);
        setVisible(true);
    }

    class SendBtnListener implements ActionListener {
        // 这里没有什么新东西。在用户点击了发送按钮时，这个方法就会
        // 把文本字段的内容，发送到服务器。
        //
        // Nothing new here. When the user clicks the send button, this
        // method sends the contents of the text field to the server.
        public void actionPerformed(ActionEvent ev){
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception ex){ ex.printStackTrace(); }

            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    // 这就是新线程完成的那些操作！！
    //
    // 在这个 run() 方法中，操作位处一个循环里（只要从服务器获取到的东西不是
    // null），一次读取一行文本，并将读取到的各行，添加到那个滚动文本区（再
    // 加上一个新行字符）。
    //
    // This is what the thread does!!
    //
    // In the run() method, it stays in a loop(as long as what it gets from
    // the server is not null), reading a line at a time and adding each line
    // to the scrolling text area(along with a new line characer).
    class IncomingReader implements Runnable {
        public void run () {
            String msg;

            try{
                while ((msg = reader.readLine()) != null) {
                    System.out.format("读取到消息 %s\n", msg);
                    incoming.append(String.format("%s \n", msg));
                }
            } catch (Exception ex){ex.printStackTrace();}
        }
    }

    public void setUpNetworking () {
        // 这里使用套接字，还获取到输入与输出流. 这个输出流在之前就已被用于往服务器
        // 发送消息了，但现在这里要使用输入流，这样新的 ‘reader’ 线程，才能从服务器
        // 获取到消息。
        //
        // We're using the socket to get the input and output streams. We were already
        // using the output stream to send to the server, but now we're using the input
        // stream so that the new 'reader' thread can get messages from the server.
        try {
            sock = new Socket("192.168.153.134", 15000);

            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream(), "UTF-8");
            reader = new BufferedReader (streamReader);

            writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));

            System.out.println("网络通信已建立");
        } catch (IOException ex){ex.printStackTrace();}
    }
}
```

### 那个相当相当简单的聊天服务器

**The really really simple Chat Server**

对于之前两个版本的 `ChatClient`，都可以使用下面这个服务器代码。所宣称过的全部服务器应答，在这里都是有效的。为了将代码裁剪到最基本的功能，这里拿掉了很多令其成一个真正服务器的部分。也就是说，虽然这个服务器可以运行，但至少有上千种方式可以破坏掉他。若在结束本教程后想要一个真正良好的服务器程序，那么请回到这里进行修改，让这个服务器代码更为鲁棒（You can use this server code for both versions of the `ChatClient`. Every possible disclainer ever disclaimed is in effect here. To keep the code stripped down to the bare essentials, we took out a lot of parts that you'd need to make this a real server. In other words, it works, but there are at least a hundred ways to break it. If you want a Really Good Sharpen Your Pencil for after you've finished this book, come back and make this server code more robust）。

而另一个可能的动手机会，且是一个现在就可以进行的，就是自己来注释这个服务器代码。这样在由自己搞清楚发生了什么的情况下，与由编者来解释相比，可以更好地掌握。还有就是，这是已经编写好的代码，因此真的没有必要掌握他的全部。这个服务器代码只是为了支持上面讲到的两个版本的`ChatClient`而已。

> **要运行 `ChatClient` 程序，就需要两个终端。首先在一个终端中启动这个服务器，随后在另一终端中启动客户端**。

```java
package com.xfoss.SimpleChat;

import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer {
    ArrayList clientOutputStreams;

    public VerySimpleChatServer () {
        clientOutputStreams = new ArrayList();

        try {
            ServerSocket serverSock = new ServerSocket(15000);

            System.out.println("聊天服务器已建立");
            while(true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("已获取到一个连接");
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler (Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream(), "UTF-8");
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void run () {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.format("读取到：%s\n", message);
                    tellEveryOne(message);
                }
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }
    
    public void tellEveryOne(String message) {
        Iterator it = clientOutputStreams.iterator();

        while(it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public static void main(String[] args) {
        new VerySimpleChatServer();
    }
}
```

## 答疑（同步的问题）

- **对于静态变量状态的保护，又是怎样的呢？在有着修改静态变量状态的静态方法时，还仍然可以使用同步化吗（What about protecting static variable state？If you have static methods that chagne the static variable state, can still use synchronization）**？

> 是的！请记住那些静态方法，是对类运行而非对该类运行的。那么就会想要知道，到底哪些对象的锁，将用在静态方法上呢？毕竟，可能还不曾有那个类的一个实例存在。而幸运的是，就如同每个 *类* 都有其自己的锁一样，每个已加载的 *类*，也有着一把锁。那就意味着，在内存堆上存在三个 `Dog` 对象时，就会有总共四把 `Dog`相关的锁。其中三把属于这个三个`Dog`实例，同时有一把属于`Dog`类本身。在对某个静态方法进行同步化时，Java就会使用类本身的那把锁。因此在对某个类的两个静态方法进行同步化时，线程就需要类的锁，来进入这两个方法（Yes! Remember that static methods run against the class and not against an individual instance of the class. So you might wonder whose object's lock would be used on a static method? After all, there might not even *be* any instances of that class. Fortunately, just as each *object* has its own lock, each loaded *class* has a lock. That means that if you have three `Dog` objects on your heap, you have a total of four `Dog`-related locks. Three belonging to the three `Dog` instances, and one belonging to the `Dog` class itself. When you synchronize a static method, Java uses the lock of the class itself. So if you synchronize two static methods in a single class, a thread will need the class lock to enter *either* of the methods）。

- **那什么是线程优先级呢？我曾听说那是一种可以对调度施加控制的方式（What are thread priorities? I've heard that's a way you can control scheduling）**。

> 线程优先级 *可能* 有助于对调度器施加影响，但他们仍就不会提供到任何的保证。线程优先级是开发者就某个线程的重要性，而向调度器进行通知（也仅是在调度器确实有线程优先级实现的情况下）的一些数字。大体上，在某个具有较高优先级的线程，突然变成可运行状态时，调度器会将较低优先级的线程踢出正在运行状态。然而......再次声明，“并无保证”。建议只有在打算对 *性能* 施加影响时，才使用优先级的设置，但绝不要依赖线程优先级去保证程序正确（Thread priorities *might* help you influence the schedular, but they still don't offer any guarantee. Thread priorities are numerical values that tell the scheduler（if it cares） how important a thread is to you. In general, the scheduler will kick a lower priority thread out of the running state if a higher priority thread suddenly becomes runnable. But...one more time, say it with me now, "there is no guarantee." We recommend that you use priorities only if you want to influence *performance*, but never, ever rely on them for program correctness）。

- **为何不是仅仅将要对数据加以保护的类中所有读取器与设置器进行同步化呢？比如对于类 `BankAccount`，为何不可以只同步 `checkBalance()` 和 `withdraw()`，而是同步了 `Runnable` 类的 `makeWithdrawal()` 方法呢（Why don't you just synchronize all the getters and setters from the class with the data you're trying to protect? Like, why couldn't we have synchronized just the `checkBalance()` and `withdraw()` methods from class `BankAccount`, instead of synchronizing the `makeWithdrawal()` method from the `Runnable`s class）**？


> 事实上，这里就 *应该* 把这些方法同步化，以阻止其他线程以别的方式来访问这些方法。由于这里的示例并没有其他代码会读写到银行账户，因此就没有那样做（Actually, we *should* have synchronized those methods, to prevent other threads from accessing those methods in other ways. We didn't bother, because our example didn't have any other code accessing the account）。
>
> 不过需要补充的是，对读取器与设置器（或者说这个示例中的 `checkBalance()` 与 `withdraw()`）的同步化，是不充分的。请记住，同步化的点位，是要令到某个特定代码部分以 **原子方式** 工作。也就是说，同步化不光是所要关注的一些单个方法，而是那些要求 **多个步骤去完成** 的方法！请稍加思考。假如这里没有将 `makeWithdrawl()`方法同步化，Ryan 就会检查完账户余额（通过调用同步化的 `checkBalance()` 方法），然后立即退出该方法并交回那把钥匙（But synchronizing the getters and setters(or in this case the `checkBalance()` and `withdraw()`) isn't enough. Remember, the point of sysnchronization is to make a specific section of code work ATOMICALLY. In other words, it's not just the individual methods we care about, it's methods that requires ***more than one step to complete***! Think about it. If we had not synchronized the `makeWithdrawal()` method, Ryan would have checked the balance(by calling the synchronized `checkBalance()`), and then immediately exited the method and returned the key）！
> 
> 当然他就在他醒来后，再度取得那把钥匙，这样他就可以调用那个同步的 `withdraw()`方法，然而这样做仍留下了在同步化之前曾遇到过的同样问题！Ryan能够检查余额、睡过去，然后 Monica 就可以进入并在 Ryan 有机会醒来且完成他的提取之前，对余额进行检查（Of course he would grad the key again, after he wakes up, so that he can call the synchronized `withdraw()` method, but this still leaves us with the same problem we had before synchronization! Ryan can check the balance, go to sleep, and Monica can come in and also check the balance before Ryan has a chance to wakes up and completes his withdrawal）。
>
>因此将全部存取方法进行同步化，从而阻止其他线程介入，可能是个不错的主意，但仍需把那些具有必须以一个原子单元方式执行语句的方法，加以同步化（So synchronizing all the access methods is probably a good idea, to prevent other threads from getting in, but you still need to synchronizing the methods that have statements that must execute as one atomic unit）。

## 代码厨房（Code Kitchen）

**这是最后一版的 `BeatBox`了**！

**他将连接到一个简单的 `MusicServer`，从而就能与其他 `BeatBox`客户端，相互发送和接收节拍编排**。

**代码是相当长的，因此完整代码清单实际上是在附录A里头**。

### 最终的 `BeatBox`客户端程序

此代码的绝大部分，与先前几章的代码厨房的代码是一样的，因此就不再全部注释了。新的几个部分包括：

- 图形用户界面GUI -- 增加了显示传入消息（实际上是个滚动清单）文本区组件和一个文本字段组件；

- 网络通信NETWORKING -- 与本章中的 `SimpleChatClient` 一样，`BeatBox`现在会连接到服务器，并获取到一个输入与输出流；

- 线程THREADS -- 又一次与 `SimpleChatClient`类似，这里启动了一个 `reader` 类来保持对来自服务器的传入消息的找寻。不过这里的传入消息不光是文本，而是包含了 **两个** 对象：`String`的消息和序列化的`ArrayList`（the serialized `ArrayList`即保存所有勾选框状态的那个东西。）


```java
package com.xfoss.BeatBox;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.event.*;
import com.xfoss.Utils.XPlatformThings;

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
            Socket sock = new Socket("127.0.0.1", 14242);
            out = new ObjectOutputStream(sock.getOutputStream());
            in = new ObjectInputStream(sock.getInputStream());
            Thread remote = new Thread(new RemoteReader());
            remote.start();
        } catch (Exception ex) {
            System.out.println("无法连接 -- 你只能自己一个人玩了。");
            ex.printStackTrace();
        }

        setUpMidi();

        // 下面这些是 GUI 代码，没什么新东西
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
```


**自己动手写代码**

关于这个程序，有些什么方式可以加以改进呢？

可从下面的几个方面着手：

1) 现在这个程序，一旦点选了某个编排，那么不管正在演奏的当前编排是什么，都会消失不见。那么如果正在制作的编排是个新的（或者是对另一编排的修改），这就不好了。这里或许应该弹出一个对话框，询问用户是否想要保存当前的编排；

2) 在没有输入命令行参数时，那么运行这个程序就会收到一个异常。那么就应当在那个检查是否传入一个命令行参数的 `main` 方法里，放入一些代码。在用户没有提供参数时，要么选择一个默认的用户名，要么打印一条消息出来，告诉用户需要再次提供一个用于显示名字的参数，运行此程序。

3) 若能够提供一个按钮，在点击这个按钮时程序会生成一个随机编排，这样的特性一定大受欢迎。这样生成的随机编排，说不定就是用户喜欢的呢。更为可取的是，若能让用户加载一些既有的“基础性”编排，比如一些 Jazz、Rock、Reggae等的编排，用户可在这些基础性编排上再添加。那就更好了。

在 Head First Java 网站上，就能找到这样的基础性编排。

### 最终的`BeatBox`服务器程序

**Final `BeatBox` server program**

下面的代码，绝大部分都跟在 “网络通信及线程” 那一章中构造的相同。事实上唯一区别在于，这个服务器程序会接收、并重新发送两个序列化对象，而不是之前的普通字符串了（尽管其中一个序列化对象是个字符串）。

```java
package com.xfoss.BeatBox;

import java.io.*;
import java.net.*;
import java.util.*;

public class MusicServer {

    ArrayList<ObjectOutputStream> clientOutputStreams;

    public MusicServer () {
        clientOutputStreams = new ArrayList<ObjectOutputStream> ();

        try {
            ServerSocket serverSock = new ServerSocket(14242);

            while(true) {
                Socket clientSock = serverSock.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSock.getOutputStream());
                clientOutputStreams.add(out);

                Thread t = new Thread(new ClientHandler(clientSock));
                t.start();

                System.out.println("已获取到一个连接");
            }
        } catch(Exception ex) {ex.printStackTrace();}
    }

    public class ClientHandler implements Runnable {

        ObjectInputStream in;
        Socket clientSock;

        public ClientHandler (Socket s) {
            try {
                clientSock = s;
                in = new ObjectInputStream(clientSock.getInputStream());
            } catch (Exception ex) {ex.printStackTrace();}
        }

        public void run () {
            Object o2 = null;
            Object o1 = null;

            try {
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    System.out.println("已读取到两个对象");
                    tellEveryone(o1, o2);
                }
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public void tellEveryone(Object o1, Object o2) {
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(o1);
                out.writeObject(o2);
            } catch (Exception ex) {ex.printStackTrace();}
        }
    }

    public static void main (String[] args) {
        new MusicServer();
    }
}
```


（End）


