# 对象序列化与文件 `I/O`: 对象的保存

**serialization and file `I/O`: Saving Objects**

![第14章题图](images/Ch14_01.png)

*图  1 - 第14章题图*

**对象可被放气或充气（Objects can be flattened and inflated）**。对象具有状态和行为。关于对象的那些 *行为*，是存活在 *类* 中，而 *状态* 则是存活在各个单独 *对象* 中的（*Behavior* lives in the *class*, but *state* lives within each individual *object*）。那么在对对象状态进行保存时，会发生什么呢？比如在编写某个游戏时，就会需要一个特性来保存/恢复游戏。又比如在编写某个创建图表的app时，就需要一个保存/打开文件的特性。在程序需要保存状态时，*可以笨办法来完成*，对各个对象进行询问，然后费力地将各个示例变量的值，以某种自创格式，写到一个文件。或者，**可以轻松的面向对象方式完成** -- 简单地将对象本身冻干/压平/持久化/脱水，然后再通过重组/填充/恢复/注水，来取回对象。不过 *某些时候* 仍然需要以笨办法来完成对象状态的保存，尤其是app保存的文件必定会被其他非Java语言的 app 读取的时候，所以本章会对这两种保存对象状态的方式加以审视（If your program needs to save state, *you can do it the hard way*, interrogating each object, then painstakingly writing the value of each instance variable to a file, in a format you create. Or, **you can do it the easy OO way** -- you simply freeze-dry/flatten/persist/dehydrate the object itself, and reconstitue/inflate/restore/rehydrate it to get it back. But you'll still have to do it the hard way *sometimes*, especially when the file your app saves has to be read by some other non-Java application, so we'll look at both in this chapter）。

## 对节拍进行捕获

**Capture the Beat**

现在 *构造* 好了完美的音乐曲目。那么就要把这个曲目 *存储* 起来。当然可以拿张纸然后摘抄下来，不过这里是要做成点击 ***保存*** 按钮的方式（或者从“文件”菜单中选择“保存”）。接着就要提供一个名称，选取某个目录，并确保这首优美的曲子，不会因为系统蓝屏司机而丢失（You could grab a piece of paper and start scribbling it down, but instead you hit the ***Save*** button(or choose Save from the File menu). Then you give it a name, pick a directory, and exhale knowing that your masterpiece won't go out the window with the blue screen of death）。

对于保存Java 程序状态来说，有很多个选项，至于具体选用何种选项，将取决于计划怎样去 *使用* 所保存的状态（You have lots of options for how to save the state of your Java program, and what you choose will probably depend on how you plan to *use* the saved state）。本章要检视的几种选项如下所示。

**在数据只会被生成数据的 Java 程式使用到时（If your data will be used by only the Java program that generated it）**：

1) **运用对象序列化**

**Use *serialization***

对留存了被压扁（序列化的）对象的一个文件进行写入。随后程序就从这个文件读取这些序列化的对象，并将他们充实回到活生生的、有呼吸的、存在于内存堆的对象（Write a file that holds flattened(serialized) objects. Then have your program read the serialized objects from the file and inflate them back into living, breathing, heap-inhabiting objects）。

**在数据会被其他程序使用到时**：

2) **写入 *普通文本* 文件**

**Write *a plain text* file**

写入到一个带有分隔符，从而可被其他程序解析的文件。比如就可以写入到一个由制表符分隔的、可被电子表格或数据库应用使用的文件（Write a file, with delimiters that other programs can parse. For example, a tab-delimited file that a spreadsheet or database application can use）。

当然并非只有这两个选项。可以所选的任意格式来保存数据。除了往文件中写入字符，还可以将数据写为字节（Instead of writing characters, for example, you can write your data as bytes）。还可以把那些 Java 原生值当作 Java 原生值来写入文件 -- 对于整数、长整数、布尔值等原生值类型，是有相应方法来写入文件的。但不管使用何种方法来保存数据，文件`I/O`的基本技巧总是不变的：把一些数据写入到 *某个东西（something）*，而通常这某个东西要么是磁盘上的文件、要么就是来自网络连接的流（But regardless of the method you use, the fundamental `I/O` techniques are pretty much the same: write some data to *something*, and usually that something is either a file on disk or a stream coming from a network connection）。读取数据则是同样的过程，只是反过来而已：从磁盘上的文件或某个网络连接，读取到一些数据。同时本章所讨论的内容，是在未使用到某种具体数据库时，所涉及的数据保存。

## 对状态进行保存

**Saving State**

设想有这么一个程序，比方说，一个奇幻冒险游戏，需要与玩家进行多次会话才能完成。随着游戏的进展，游戏中的角色变得愈加强大、弱小、灵巧等等，并会收集（或丢失）各种武器。那么就肯定不愿意每次启动游戏都又从头开始玩 -- 那样你的游戏角色就永远不会在某次激烈战斗中有最佳状态。那么就需要一种保存游戏角色状态的方式，以及在继续游戏时对状态进行恢复的方式。而由于作为游戏程序的编写者，就会希望整个保存与恢复，应该尽可能是容易实现（且简单明了）。

1) 选项一

**把这三个序列化的游戏角色写到某个文件**

创建出一个文件，并写入三个序列化角色对象。这个文件在作为文本进行读取时，并无任何意义：

```console
 ̈ÌsrGameCharacter 
 ̈%gê8MÛIpowerLjava/lang/
 String;[weaponst[Ljava/lang/
 String;xp2tlfur[Ljava.lang.String;≠“VÁ
 È{Gxptbowtswordtdustsq~»tTrolluq~tb
 are handstbig axsq~xtMagicianuq~tspe
 llstinvisibility
 ```

 2) 选项二

 **写入普通文本文件**

 创建出一个文件，并写入三行文本，每行一个游戏角色，用逗号分隔角色状态的各个部分：

 ```console
 50,Elf,bow, sword,dust
 200,Troll,bare hands,big ax
 120,Magician,spells,invisibility
 ```

 ![对象存储图解](images/Ch14_02.png)


*图 2 - 对象存储图解*

## 把序列化对象写到某个文件

以下就是将对象进行序列化（保存）的步骤（Here are the steps for serializing(saving) an object）。不用纠结于把这些步骤都记住；本章后续会深入讲解。

1) **构造一个 *`FileOutputStream`* 对象**

```java
// 构造出一个 FileOutputStream 对象。FileOutputStream 知道怎样去
// 连接（并创建出）一个文件。
// 
// 若这个 “MyGame.ser” 文件不存在，那么他就会被自动创建出来。
FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
```

2) **构造一个 *`ObjectOutputStream` 对象***

```java
// ObjectOutputStream 实现对象写到文件，但他无法直接连接到
// 文件。他需要喂入一个“helper”。这实际上就是把一个流“链接”
// 到另一个上（This is actually called 'chaining' one stream
// to another）。
ObjectOutputStream oStream = new ObjectOutputStream(fileStream);
```

3) **写入对象**

```java
// 对这些由 characterOne, Two, Three 所表示的对象进行序列化操作
// 并将他们写到文件 “MyGame.ser” 中。
oStream.writeObject(characterOne);
oStream.writeObject(characterTwo);
oStream.writeObject(characterThree);
```

4) **关闭 `ObjectOutputStream`**

```java
// 关闭了顶部的流，就会关闭其下所有的其他流，因此 `FileOutputStream` 
// （及那个文件）就会自动关闭。
oStream.close();
```

**在各种流中，数据从一处往另一处移动（Data moves in streams from one place to another）**。

Java 的 `I/O` API，有着各种表示到诸如文件或网络套接字这类目的与源的连接，以及将那些只有被链接到其他流才会工作的流 ***链接*** 起来的 *连接性* 流（The Java `I/O` API has ***connection*** streams, that represent connections to destinations and sources such as files or network sockets, and ***chain*** streams that work only if chained to other streams）。

通常，要至少同时钩起两个流流，才能完成有用的事情 -- *一个* 表示连接，而 *另一个* 则是要调用到他的方法。为什么是两个呢？因为 *连接* 流通常都是很低级别的。就拿 `FileOutputStream`（就是一个连接流）来说，就有写入 *字节* 的一些方法。但这里并不想要写入 *字节*！这里要的是写入 *对象*，因此就需要一个高级别的 *链接* 流（Often, it takes at least two streams hooked together to do something useful -- *one* to represent the connection and *another* to call methods on. Why two? Because *connection* streams are usually too low-level. `FileOutputStream`(a connection stream), for example, has methods for writing *bytes*. But we don't want to write *bytes*! We want to write *objects*, so we need a higher-level *chain* stream）。

好，那么又为什么不只要精准完成所需的单个流呢？一个实现对象写入并同时将对象转换成字节的流？这就要考虑良好的面向对象了。每个类做好 *一件* 事情。`FileOutputStream` 就是把字节写入到文件。`ObjectOutputStream` 就是把对象转换成可写入到流的数据。所以这里构造一个 `FileOutputStream` 来实现到文件的写入，并在 `FileOutputStream` 末尾钩起一个 `ObjectOutputStream`（一个链接流）。在调用`ObjectOutputStream`上的 `writeObject()` 方法时，对象就被泵入到流中，随后就移动到 `FileOutputStream`，在那里最终被作为一些字节，写到某个文件。

不同连接与链接流组合的混搭能力，赋予到我们惊人的灵活性！若强制要求使用仅仅 *单个* 的流类，那么就会受 API 设计者们的支配，就会希望他们能考虑到咱们所期望的所有功能。然而有了流链接特性，就可以组装出自己 *定制* 的各种流链（The ability to mix and match different combinations of connection and chain streams gives you tremendous flexibility! If you were forced to use only a *single* stream class, you'd be at the mercy of the API designers, hoping they'd thought of *everything* you might ever want to do. But with chaining, you can patch together your own *custom* chains）。

![Java对象写入文件过程](images/Ch14_03.png)

*图 3 - Java对象写入文件过程*

## 在对象被序列化时，到底发生了什么？

**What really happens to an object when it's serialized**?

![图解存活对象与序列化对象的区别](images/Ch14_04.png)

*图 4 - 图解存活对象与序列化对象的区别*

![Java保存对象实例](images/Ch14_05.png)

*图 5 - Java保存对象实例*

### 然而对象状态 *究竟是* 个什么呢？到底需要保存什么呢？

**But what exactly *IS* an object's state? What needs to be saved**?

现在就开始变得有趣起来了。要保存 *原生* 值 `37` 与 `70` 是相当容易。但如果对象有着一个是对象 *引用* 的实例变量呢？某个对象有着五个的对象引用的实例变量又会怎样呢？如果这些对象实例变量本身又有实例变量的话，又会怎样呢（Easy enough to save the *primitive* values `37` and `70`. But what if an object has an instance variable that's an object *reference*? What about an object that has five instance variables that are object references? What if those object instance variables themselves have instance variables）？

请想想。对象的什么部分，是潜在唯一的？试想一下为了获得一个与所保存对象一致的对象，究竟需要恢复的是什么。当然对象将有着不同的内存位置，然而对于这一点我们并不关心。这里所关心的，全是在内存堆上得出的结果，将获取到一个与对象被保存时有着相同状态的一个内存堆上的存活对象。

## 脑力锻炼

![如何保存带有到其他对象引用变量的对象](images/Ch14_06.png)

*图 6 - 如何保存带有到其他对象引用变量的对象*

必须要怎样保存 `Car` 对象，才能从 `Car` 副本中恢复到其原先的状态？

试想一下要保存 `Car` 对象会需要些什么 -- 以及怎样去保存他。

同时在 `Engine` 对象又有着到 `Carburetor` 对象的引用时，会怎样呢？以及在 `Tire []` 这个数组对象中又有着什么呢？

**在某个对象被序列化时，自其实例变量引用到的全部对象同时被实例化。这些被引用对象引用到的对象亦被实例化。这些被引用到的对象所引用的对象，亦被实例化......而最妙的地方就是，这些都是自动发生的**！

这个 `Kennel` 对象有着一个到 `Dog []` 数组对象的引用。而 `Dog []` 中留存了对两个 `Dog` 对象的引用。每个 `Dog` 对象留存了到一个字符串及一个 `Collar` 对象的引用。字符串对象有着一个字符集合，同时 `Collar` 对象有着一个整数。

![对象保存时涉及实例变量为对象引用的情形](images/Ch14_07.png)


*图 7 - 对象保存时涉及实例变量为对象引用的情形*

> **序列化保存了完整的 *对象图面（object graph）*。自所保存对象开始，全部的实例变量引用到对象，都会被序列化**。

## 在希望所编写的类可被序列化时，就要实现 `Serializable` 接口

**If you want your class to be serializable, implement `Serializable`**

由于 `Serializable` 接口没有要实现的方法，因此又被称作 *标记* 或者说 *标签* 接口（The `Serializable` interface is known as a *marker* or *tag* interface, because the interface doesn't have any methods to implement）。 其唯一目的，就是通告实现了他的类，是 *可被序列化的（serializable）*。也就是说，`Serializable` 类型的类，可通过序列化机制加以保存（In other words, objects of that type are saveable through the serialization mechanism）。在有超类是可序列化的类时，那么其子类将自动成为可序列化类，即使其子类并未显示声明 *`implements Serializable`*。（这也是接口一贯的 *运作* 方式。在某个类的超类 `IS-A` `Serializable`时，那么这个类同样是 `Serializable`的）。

```java
// 这里传递给 writeObject() 方法的参数，必须 实现了 Serializable
// 接口，否则在运行时就会失败
objectOutputStream.writeObject(myBox);
```

--

```java
package com.xfoss.learningJava;

// 接口 Serializable 是在 java.io 包中，因此要需要这个 import 语句
import java.io.*;

// 虽然没有要实现的方法，但在写下这里的 "implements Serializable"
// 时，就等于告诉了JVM，“可对这种类型的对象进行序列化。”
public class Box implements Serializable {

    // 这两个值将会被保存起来
    private int width;
    private int height;

    public void setWidth (int w) {
        width = w;
    }

    public void setHeight (int h) {
        height = h;
    }

    public static void main (String[] args) {
        Box box = new Box();
        box.setWidth(50);
        box.setHeight(20);

        // I/O 操作可能抛出异常。
        try {
            // 若存在一个名为“Box.ser”的文件，那么就连接到这个文件。而若不
            // 存在，就构造一个新的名为 “Box.ser” 的文件。
            FileOutputStream fStream = new FileOutputStream("Box.ser");
            // 构造一个链接到连接流 FileOutputStream 的 ObjectOutputStream 对象
            // 让他写入对象 box。
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);
            oStream.writeObject(box);
            oStream.close();
        } catch (Exception ex) {ex.printStackTrace();}
    }
}
```

运行这段代码将创建出一个 `Box.ser` 的文件，其内容为：

```console
¬í sr com.xfoss.learningJava.Boxý	¼%~FH I heightI widthxp      2
```

以后不管运行多少次，`Box.ser` 的内容都不会改变。

## 序列化就是全部，或者全无。

**Serialization is all or nothing**.

**设想一下如果对象的某些状态没有正确保存，会发生什么呢**？

![序列化对象恢复的风险](images/Ch14_08.png)

*图 8 - 序列化对象恢复的风险*

**要么整个的对象图面被正确序列化，但凡有一个差错，那么就算作序列化失败（Either the entire object graph is serialized correctly or serialization fails）**。

**在对一个 `Pond` 对象进行序列化时，若他 `Duck` 实例变量拒绝被实例化（因为没有对 `Serializable` 接口进行实现），那么就无法实例化这个 `Pond` 对象**。

```java
package com.xfoss.learningJava;

import java.io.*;

// Pond 的对象，可被序列化
public class Pond implements Serializable {
    // 类 Pond 有一个实例变量，是到一个 Duck 
    // 对象的引用。
    private Duck duck = new Duck();
    
    public static void main (String[] args) {
        Pond pond = new Pond();

        try {
            FileOutputStream fStream = new FileOutputStream("Pond.ser");
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);

            // 在对 pond （一个 Pond 的对象）进行序列化时，该对象
            // 的 Duck 实例变量会自动被序列化。
            oStream.writeObject(pond);
            oStream.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}

// 然而！！Duck（在 com.xfoss.learningJava.UseADuck中声明） 并
// 不是可序列化的！Duck 类并没有实现 Serializable, 因此在对
// Pond 对象进行序列化时，由于该 Pond 对象的 Duck 实例变量
// 无法被保存，而失败。
```

将报出以下错误：

```console
Quack... My size is 32, my number is 1
java.io.NotSerializableException: com.xfoss.learningJava.Duck
        at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1185)
        at java.base/java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1553)
        at java.base/java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1510)
        at java.base/java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1433)
        at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1179)
        at java.base/java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:349)
        at com.xfoss.learningJava.Pond.main(Pond.java:15)
```

![非 `Serializable` 对象无法被序列化的麻烦](images/Ch14_09.png)

*图 9 - 非 `Serializable` 对象无法被序列化的麻烦*

**在实例变量无法（或不应）被保存时，就要把他标记为 *瞬态* 实例变量（Mark an instance variable as *transient* if it can't (or shouldn't) be saved）**。

若希望某个实例变量跳过对象的序列化过程，那么就要使用 `transient` 关键字，对该变量进行标记。

```java
import java.net.*;

class Chat implements Serializable {
    // 关键字 transient, 说的是 “在序列化期间不要
    // 保存这个变量，只要跳过他就好。”
    transient String currentID;

    // 在序列化期间，变量 userName 将被保存为该对象
    // 的一部分。
    String userName;

    // 其他代码
}
```

在有着一个因为本身不是可序列化，而无法被保存的实例变量时，就可以使用 `transient` 关键字对那个变量进行标记，从而序列化过程就会精准地跳过他。

**然而一个变量无法被序列化的原因又究竟为何呢**？其中可能简单地就是，那个类的设计者仅仅是 *忘了* 把其构造为对 `Serializable` 接口进行实现。或者由于对象依赖于一些特定于运行时信息，因此就直接无法保存（So why would a variable not be seralizable? It could be that the class designer simply *forgot* to make the class implement `Serializable`. Or it might be because the object relies on runtime-specific information that simply can't be saved）。
