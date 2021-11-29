# 了解Java API：使用Java的库

Java语言本身，就带有多达数百个的预构建类。若掌握了如何从 Java 库，也就是常说的 **Java API**中找到你所需要的功能，那么就不必去重新发明轮子了（You don't have to reinvent the wheel if you know how to find what you need in the Java library, known as the **Java API**）。_还有更好的事情等着你去做_。若要编写代码，也只是要去编写对于你的应用来说，真正特有的代码。你知道那些下午5点就下班，早上10点都还没到公司的程序员吗？__他们就是善于使用 Java API__。Java的核心库，有着大量的类，就等着你去使用，可以像使用砖块一样，从大量预构建的代码，组装出你自己的程序。本书中用到的写好Java程序示例，就是无需从头再编写的代码，但还是必须输入到你的程序中去。而Java API则是些甚至不用输入的代码。你只需要学会怎样去使用他们就可以了。

Java标准版带有数百个预构建类（除非使用的是针对小型设备的微型版本，那么Java标准版就是你手头的版本，The Java Standard Edition，which is what you have unless you're working on the Micro Edition for small devices and believe me, you'd know）。他们就和本书中这些编写好的代码一样，只是这些内建的类，时已经编译好的。

_这就是说这些内建类无需再次输入了__。直接使用他们就好。

`ArrayList` 是Java库中无数内建类之一。你可以在自己的代码中，如同是你自己编写的`ArrayList`一样使用他。

_我该怎么来知道，Java API里有些什么呢？_

这正是如何成为一名正式的Java程序员的关键所在。这并不是说在构建软件时尽可能的懒，而是说在有人已经完成了这门语言中大多数最重要的部分后，你可以节省那么多的时间，而你只需要花点时间去了解这些API，去完成你的软件中有趣的部分即可。

有点离题了，这个问题的答案，往短了说，就是你得花点时间来学习，在Java核心API中有些什么。往大了说，就是在完成本章学习后，就知道该怎么去获知Java API中有些什么了。

_怎么才知道我要用到Java API的某个类，该如何从“要完成某个功能”过渡到“找到实现某个功能的方法”？_

你已经发现了问题的本质了。在学完本书后，你将会对Java这门语言有很好的掌握。之后你的学习曲线，就是了解如何从找到问题，到通过编写尽可能少的代码，来得到问题的解决方案了。随后将讨论这个问题。

## `ArrayList` 与 `array` 的不同

`ArrayList` 是一个对象。`array`也是对象，与其他对象一样，也是存活在内存堆（heap）上的。但 `array`仍然只是`array`，不会是`ArrayList`，只是个冒牌货的。对象是同时有 __状态__ 和 __行为__ 的（state and behavior）。`array`就没有可调用的方法。Java 中的 `array` 一旦初始化，就再也不能追加或移除其中的元素了。`ArrayList`作为头等对象，就有着移除其中元素的能力，可以动态地改变大小。`ArrayList`有着极大的灵活性。__不可以直接将原生类型变量放入到 `ArrayList`中__ 。但将原生类型变量，封装到一个原生类型封装器类中之后，就可以将原生变量放入到 `ArrayList`中了（You can put a primitives in an `ArrayList`, as long as it's wrapped in a primitive wrapper class）。自 Java 5.0 开始，原生类型变量的封装（以及在取出原生类型变量时的解封装），就已经是自动的了。可以确定的说，在运用由原生变量构成的 `ArrayList`时，可能比使用`array`还要快，因为所有的原生变量封装与解封装，都是........话又说回来，如今谁还会用到原生类型变量呢？

对于 `ArrayList`，只是在操作一个普通的 `ArrayList` 类型的对象，只是调用普通对象上的方法，使用普通对象上的 `.` 运算符。而对于一个 `array`，就要使用 _特殊的数组语法_ （比如 `myList[0] = foo`），这些语法只能在数组上使用，其他地方都用不到。就算数组也是对象，但数组是存在于他自己的世界中，你无法调用他上面的任何方法。可以访问到的，也只是他唯一的实例变量 `length`。

1. 普通的老式 `array`，在创建出来时就必须知道大小。

但对于 `ArrayList` 来说，就只需要生成一个类型为 `ArrayList`的对象就行。因为随着有对象加入或移除，`ArrayList`对象的大小会增加或收缩。

```java
String[] a = new String[2];
ArrayList<String> al = new ArrayList<String> ();
```

2. 要将某个对象放入到常规数组中，就必须为其指定一个特定位置。

（而且这个位置，必须要是从 `0` 到小于该数组长度的一个值。）

```java
myList[1] = b;
```

在指定的索引位置超出了该数组的边界时（比如对于一个声明了大小为2的数组，在尝试将某个值赋给索引3时），运行时就会报错（`java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3`）。但对于 `ArrayList`，就可以使用 `add(anInt, anObject)` 方法，或是只写 `add(anObject)`，就可以为新加入的对象，分配到空间。

```java
myList.add(b);
```

3. 数组所使用的语法，在Java中的其他任何地方都不会用到。

但 `ArrayList` 就是普通的 Java 对象，因此没有特殊的语法。

```java
myList[1];
```

4. 自 Java 5.0 开始，`ArrayList` 就已经是参数化的了（parameterized）。

```java
ArrayList<String>
```

这里的在尖括号中的 `String` 是一个 “类型参数”（`type parameter`）。 `ArrayList<String>` 的意思是，“一个字符串清单”，以示与 `ArrayList<Dog>` 表示“一个 Dog 的清单”的不同。

如今使用 `<type>` 这样的语法，就可以声明并创建 `ArrayList` 变量所能保存的对象类型。在“集合”章节将会仔细审视 `ArrayList` 中的参数化类型语法。现在只需要知道，这种语法是一种强制编译器，只允许特定类型的对象，放入到 `ArrayList` 中的办法。

