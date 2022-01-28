# 原生与参考：熟知你的变量

变量有两种：**原生变量** （**primitive**）和 **参考变量** （**reference**）。到现在为止，我们在两个地方用到了变量：作为对象状态（实例变量），以及作为本地变量（在方法中声明的变量）。后面会把变量用作 **参数**（**arguments**, 由调用代码发送给方法的值），同时作为返回类型(**return types**，返回到方法调用者的值)。在前面已经见到了声明为简单的 **原生** 整数值（比如 `int` 类型）。也见到过声明为更为复杂的诸如字符串（`String`）或数组（`array`）变量。但在现实生活中，有着远比整数、字符串以及数组复杂的东西，比如一个有着 `Dog` 实例变量的 `PetOwner` 对象，或者有着 `Engine` 的 `Car` 对象，本章将揭开 Java 类型谜题，看看可以 **声明** （__declare__）出什么样的变量，在变量中可以 **放入** （**put in**）些什么东西，以及对变量可以进行什么操作。还会揭示 **垃圾回收堆**（**garbage-collectible heap**）上，所发生的事情。


## 声明一个变量

**Java 是类型敏感的** （**Java cares about type**）。他不会让你去干那些诡异又危险的事情，比如把一头长颈鹿当成兔子来加以引用，就如同某人要让长颈鹿去像兔子那样蹦跳时会怎样呢？Java也不会让你把一个浮点数放到整数变量里，除非你 **告知了编译器**，你知道那样做可能会失去精度（也就是小数点后面的所有数字）。

编译器（`javac`）会发现大部分问题：

```java
Rabbit hopper = new Giraffe();
```

别指望这代码会被编译出来。拜托!

为了能让 **类型安全** （**type-safty**）发挥作用，就要声明变量的类型。变量是个整数？还是 `Dog`? 还是是单个的字符。变量类型有两种：**原生** （**primitive**）与 **对象引用**（**object reference**）。原生变量保存的是一些基础类型的值（可以理解为简单的位模式，simple bit patterns），包括整数、逻辑值以及浮点数。对象参考变量，则是保存了对对象的参考（_references to objects_）。

__所有变量，都必须有一个类型__。

除了类型，变量还必须有个名称，如此就可以在代码中，使用他的名称了。

__变量必须有个名称__。

```java
int count;
```

`int`是类型（`type`），`count`是名称（`name`）。

> 注意：如果你遇到这样一个语句时：`an object of type X`，就把 `type`和`class`当成同义词吧。

**变量是一个容器，用于_保存_ 某个事物。（A variable is just a cup. A container. It _holds_ something）**

__原生类型__

| 类型  | 位深度（Bit Depth） | 取值范围 |
| :-: | :-: | :-: |
| 逻辑值与字符 | --- | --- |
| 逻辑值（`boolean`） | 特定于 JVM | `true`或`false` |
| 字符（`char`） | 16位 | `0` 到 `65535` |
| 数值（都是有符号的） |  --- | --- |
| 字节（`byte`） | 8位 | `-128` 到 `127` |
| 短整数（`short`）  | 16位 | `-32,768` 到 `32,767` |
| 整数（`int`） | 32位 | `-2,147,483,648` 到 `2,147,483,647` |
| 长整数 （`long`） | 64位 | `-9,223,372,036,854,775,808` (`-2^63`) 到 `9,223,372,036,854,775,807` (`2^63 – 1`) |
| 浮点数 | --- | --- |
| 单精度浮点数（`float`） | 32位 | 根据JVM的不同而不同 |
| 双精度浮点数（`double`） | 64位 | 根据JVM的不同而不同 |

**带赋值的原生变量声明**：

```java
int x;
x = 234;
byte b = 89;
boolean isFun = true;
double d = 3456.98;
char c = 'f';
int z = x;
boolean isPunkRock;
isPunkRock = false;
boolean powerOn;
powerOn = isFun;
long big = 3456789;
float f = 32.5f;
```

> 注意最后一个中的 `f`，因为Java会将所有带着小数点的没有 `f` 的数，看着是双精度浮点数，因此要在后面加上 `f` 来表示单精度浮点数。

**将值赋给变量的方式有以下三种**：

* 在等号之后键入一个 _字面值_ （比如： `x=12`, `isGood=true`）
* 将一个变量的值赋给另一个变量 （比如 `x=y`）
* 使用结合了变量与字面值的表达式 （比如 `x = y + 43`）

> Java中声明了原生变量后需要初始化，若未初始化，将报出以下错误：

```java
int y;
System.out.println(y);
```

```bash
java.lang.Error: Unresolved compilation problem:
        The local variable y may not have been initialized
```

__而对象中的变量，则不存在这样的问题，其在对象初始化时，会被自动赋值。__


**变量命名规则**

* 必须以字母、下划线（`_`）或美元符号（`$`）开头。那么就不能以数字开头。
* 后面就可以使用数字了。只是不能以数字开头。
* 可以是你所喜爱的任何字符串，除开上面两条规则外，还要不能是 Java 的保留字。


__Java的保留字__

| `boolean` | `byte` | `char` | `double` | `float` | `int` | `long` | `short` | `public` | `private` |
| :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| `protected` | `abstract` | `final` | `native` | `static` | `strictfp` | `syschronized` | `transient` | `volatile` | `if` |
| `else` | `do` | `while` | `switch` | `case` | `default` | `for` | `break` | `continue` | `assert` |
| `class` | `extends` | `implements` | `import` | `instanceof` | `interface` | `new` | `package` | `super` | `this` |
| `catch` | `finally` | `try` | `throw` | `throws` | `return` | `void` | `const` | `goto` | `enum` |

**关于参考变量**

* 实际上是不存在 **对象** 变量的（There is actually no such thing as an **object** variable）
* 有的只是对象 **参考** 变量（There's only an object **reference** variable）
* 对象参考变量所存储的，是表示一种访问对象的方式的数据位（An object reference variable holds bits that represent a way to access an object）
* 对象参考变量，并非保存的是对象本身，而是类似于指针的东西，或者说是一个内存地址。就算在不知道某个参考变量里头具体是什么，我们也明白他到底是个什么，参考变量代表着唯一的对象。同时JVM也知道如何使用这个引用，来获取到某个对象。

__对象引用__ 只是另一个变量值（An object reference is just another variable value）。

**对象声明、创建与赋值三步骤**

| `Dog myDog` | `=` | `new Dog();` |
| :-: | :-: | :-: |
| 1 | 3 | 2 |


1) 声明一个引用变量（Declare a reference variable）

告诉JVM为某个引用变量分配空间，同时给变量命名。引用变量的类型，就固定下来了（`Dog`）。

2) 创建一个对象

告诉虚拟机给新的对象，在内存堆上分配空间。

3) 将对象和对象引用连接起来（Link the object and the reference）

将新的对象赋值给引用变量（实际上是把新对象在内存堆上的地址，赋值给对象的引用）。


**引用变量的大小**

跟JVM相关，是内存堆的编址/寻址方式决定的。对象引用本质上是一些指针，但不能访问到他们。也不需要访问他们。可以把这些对象引用看着是一个64位的地址。

不管对象有多大，各种对象的引用，都是一样大的（64位）。对于给定的JVM，所有对象引用变量的大小都是一样的。各个JVM在表示对象引用变量上可能有所不同。

不能对引用变量做C语言中，指针那样的前后移动的算术运算，**Java 不是 C**。

* 对象引用是一个到对象的指针（存储对象在内存堆中的地址），可以是同一类型下不同对象的引用
* 被 `final` 关键字修饰的对象引用，就只能是一个对象的引用了，不能再被赋值其他对象
* `null` 是一个值，表示空指针，在某个对象被赋值了空指针后，就失去了对原来对象的引用，原来那个对象，就再也访问不到了，将被垃圾回收
* 两个对象引用变量，可以指向同一个对象
* 失去了对象引用变量的对象，将被垃圾回收
