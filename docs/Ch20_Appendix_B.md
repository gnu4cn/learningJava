# 附录B：其他特性

## 注释语法

**Annotations**

*注释*，是元数据的一种形式，提供了不作为程序本身部分的、有关程序的一些数据。注释对其所注释代码运作，并未直接影响（*Annotations*, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate）。

注释有着多种用途，这些用途中：

- **为编译器提供信息** -- 编译器可使用注释来探测错误或抑制告警信息（**Information for the compiler** -- Annotations can be used by the compiler to detect errors or suppress warnings）；
- **编译时与部署时的处理** -- 软件工具可对注释信息加以处理，从而生成代码、XML文件等等（**Compile-time and deployment-time processing** -- Software tools can process annotation information to generate code, XML files, and so forth）;
- **运行时的处理** -- 在运行时，可对一些注释进行检查（**Runtime processing** -- Some annotations are available to be examined at runtime）。

本课程解释了：

- [哪些地方可以使用注释语法](#where)、
- 如何运用注释语法、
- Java 平台以及标准版（Standard Edtion, Java SE API）中有哪些可用的预定义好的注释类型（annotation types）、
- 类型注释如何与可插拔类型系统结合以编写出有着更强类型检查的代码，
- 以及怎样去实现重复性注释语法。

（This lesson explains where annotations can be used, how to apply annotations, what predefined annotation types are available in the Java Platform, Standard Edition(Java SE API), how type annotations can be used in conjuncton with pluggable type systems to write stronger type checking, and how to implement repeating annotations.）

## 注释语法基础

**Annotations Basics**

### 注释语法的形式

**The Format of an Annotation**

注释语法的最简单形式，看起来像下面这样：

```java
@Entity
```

这个所在符号字符（`@`），是给编译器表明，接下来的是个注释。在下面的示例中，那个注释的名称为 `Override`:

```java
@Override
void mySuperMethod() {...}
```

注释可以包含 *元素（elements）*，元素可以是具名的或不具名的，同时存在着这些元素的值：

```java
@Author (
    name = "Benjamin Franklin",
    date = "3/27/2003"
)
class myClass {...}
```

或者：

```java
@SuppressWarnings (value = "unchecked")
void myMethod() {...}
```

在仅有一个名为 `value` 的元素时，那么元素名称是可以省略的，就像这样：

```java
@SuppressWarnings ("unchecked")
void myMethod () {...}
```

在注释没有元素时，那么那对括号是可以省略的，就如同在上面的 `Override` 示例中那样。

在同一声明上，使用多个注释也是可能的：

```java
@Author (name = "Jane Doe")
@EBook
class MyClass {...}
```

在若干注释有着同样类型时，此时就叫重复注释（a repeating annotation）：

```java
@Author(name = "Jane Doe")
@Author(name = "John Smith")
class MyClass {...}
```

Java SE 8 发布才开始支持重复注释。更多的有关情况，请参考 [重复注释](#repeating_annotations)

注释类型可以是定义在 Java SE API 的 `java.lang` 或 `java.lang.annotation` 包中类型之一。在上面的示例中，`Override`与 `SuppressWarnings`，就是 [预定义的Java 注释（predefined Java annotations）](#predefined_java_annotations)。定义自己的注释类型，也是可能的。前面示例中的 `Author` 与 `EBook` 就属于自定义注释类型。

### <a id="where"></a>哪些地方可以使用注释语法

可在声明上应用注释：类、字段、方法，以及其他一些程序元素的声明上。当在某个声明上运用了注释语法时，依照约定，每条注释常常占据自己的一行。

自Java SE 8 发布开始，注释语法还可应用到类型的 *使用*。下面是一些示例：

- 应用到类实例的创建表达式：

```java
new @interned MyObject();
```

- 类型强制转换：

```java
myString = (@NonNull String) str;
```

- `implements` 子语句（`implements` clause）：

```java
class UnmodifioableList<T> implements
    @Readonly List<@Readonly T> {...}
```

- 抛出异常的声明（Thrown exception declarations）：

```java
void monitorTemperature() throws
    @Critical TemperatureException {...}
```

这种形式的注释，叫做 *类型注释（a type annotation）*。有关类型注释的更多信息，请参考 [类型注释与可插拔类型系统（Type Annotations and Pluggable Type Systems）](#type_annotations_and_pluggable_type_systems)。

## 注释类型的声明

**Declaring a Annotation Type**


