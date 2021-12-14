# 数字与静态值：数字为要

**Numbers and Statics: Numbers Matter**

**做运算（Do the Math）**。除了原生算术运算，数字方面还有更多的要做。可能要获取某个数字的绝对值，或对某个数字四舍五入，或者找出两个数中较大的等等。还可能希望只打印某个数的两位小数，或者要在大数中放进逗号，从而让大数更易于阅读。日期又该怎样处理呢？或许要以各种方式来打印日期，甚至要对日期进行 *操作（manipulate）*，比如“把今天的日期加上三周”。还有怎样把字符串解析到数字呢？或是把数字转换成字符串？有幸的是，Java API 提供了很多易于上手的数字处理方法（full of handy number-tweaking methods）。但这些方法大多是 **静态的（`static`）**，因此先要了解某个变量或方法为静态时，以及 Java 中的常量 -- 静态最终变量，是什么意思（But most of them are `static`, so we'll start by learning what it means for a variable or method to be static, including constants in Java -- `static` `final` variables）。

## 数学方法：接近全局方法

**MATH methods: as close as you'll ever get to a `global` method**

但 Java 中不存在全局的 *任何东西*。但请想想：有个不依赖实例变量值的方法会怎样。就拿类 `Math` 中的 `round()` 方法来说。他会每次都会执行相同的操作 -- 将浮点数（该方法的参数）四舍五入到最接近的整数。每次都这样的。就是有一万个类 `Math` 的实例，都运行 `round(42.2)` 方法，得到的结果都是 `42`。每次都是。也就是说，方法在参数上执行，而绝不会受某个实例变量状态影响。改变方法 `round()`运行方式的唯一值，就是传递给该方法的参数！

看起来为了运行 `round()` 方法而构造一个类 `Math` 的实例，确实是浪费了很多高价值的内存堆空间吧？对于 *其他* 一些 `Math` 的方法，比如接收两个数字原生值并返回二者中较小的值的 `min()`，或 `max()`，或者返回某个数的绝对值的 `abs()`等等，又会怎样呢？

***这些方法绝不会用到实例变量值***。事实上类 `Math` 是没有任何实例变量的。那么构造一个类 `Math` 的实例就没有任何价值。所以猜猜会怎样？所以就不必构造类 `Math` 的对象。事实上也无法构造类`Math`的实例。

**在尝试构造类 `Math` 的实例时**：

```java
Math mathObj = new Math();
```

**将得到这个错误消息**：

```console
Math() has private access in java.lang.Math
```

> 该错误消息显示，`Math` 构造器是被标记为 `private` 的！那就是说，**绝** 不可以在类 `Math` 上写 `new` 来构造一个新的 `Math` 对象。


![类`Math`的方法](images/Ch10_01.png)


*图 1 - 类`Math`的方法*



### 常规（非静态）方法与静态方法的区别

虽然Java作为面向对象语言，不过后面就会发现一个特殊案例，尤其是那些工具方法（就像这些`Math`的方法），在这些地方就没有必要用到类的实例。关键字 `static` 让方法在 ***没有任何其所属类的实例*** 情况下，就可以运行。静态方法就是指 “其行为不依赖实例变量，因此不需要实例或对象。只需要类就行”。

![常规方法与静态方法的区别](images/Ch10_02.png)


*图 2 - 常规方法与静态方法的区别*


![静态方法与非静态方法的调用](images/Ch10_03.png)


*图 3 - 静态方法与非静态方法的调用*

### 让类具有静态方法有什么意义？

通常（尽管不总是这样），有着静态方法的类，都是些不打算被实例化的类（Often(although not always), a class with static methods is not meant to be instantiated）。在第8章就谈到过抽象类，以及如何使用 `abstract` 关键字类对类进行标记，从而使得任何人都不可以在那个类类型上写 `new`。也就是说，***绝无可能对抽象类进行实例化***。

到这里，还可以通过将构造器标记为 `private`，来阻止其他代码对 *非*抽象类进行实例化了。请记住，标记为 `private` 的方法意味着只有类中的代码才可以运行那个方法。标记成 `private` 的构造器，基本与标记为 `private` 的方法一样 -- 只有类中的代码才可以运行构造器。那么在类 *外部* 就没有人可以对类写 `new` 了。这就是类 `Math` 的运作方式。他的构造器是私有的，就不能构造出 `Math` 的实例。编译器了解你的代码是不能访问私有构造器的。

这又并不是说，有着一个或多个静态方法的类，绝对不会被实例化。事实上，只要放入了一个 `main()` 方法，那个类中就有了一个静态方法！

通常是要构造一个 `main()` 方法来启动或测试另一个类的，几乎总是要在 `main()` 方法中去实例化某个类，且随后会在构造的新实例上运行某个方法。

因此虽然存在一个非静态方法，就意味着一定有某种构造类实例的方式，但在类中结合静态和非静态方法一起使用，是自由的。获取新对象的唯二方式，是通过关键字 `new`，或者逆序列化（deserialization, 或称为Java Reflection API，附录会讲到）。此外并无他法。不过还有一个非常有趣的问题，那就是到底是 *谁* 来写下 `new`，后面会讨论这个问题。


### 静态方法不能使用非静态（实例）变量！

**Static methods can't use non-static (instance) variable**!

静态方法的运行，无需对其所属类的任何特定实例有所了解。就如同前面所说的，静态方法所属类甚至没有实例变量。因为静态方法是使用 *类*（`Math.random()`），而非 *实例引用变量* （`t2.play()`）调用，所以静态方法就不能引用其所属类的任何实例变量。

**若尝试编译这样的代码：**

```java
class Duck {
    private int size;

    public static void main (String [] args) {
        System.out.format("Size of duck is %s\n", size);    // 哪个 Duck? 谁的 size ?
    }                                                       // 若在内存堆上的某处有个 Duck, 这里也是对其一无所知的

    public void setSize (int s) {
        size = s;
    }

    public int getSize () {
        return size;
    }
}
```

**将收到这样的错误信息**：

```console
non-static variable size cannot be reference from a static context
```

> **在某个静态方法中尝试使用实例变量时，编译器就会想 “我不知道你讲的是哪个对象的实例变量”！就算在内存堆上有 10 只鸭子，静态方法也对他们一无所知**。
