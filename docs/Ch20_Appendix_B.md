# 附录B：其他特性

## 注解语法

**Annotations**

*注解*，是元数据的一种形式，提供了不作为程序本身部分的、有关程序的一些数据。注解对其所注解代码运作，并未直接影响（*Annotations*, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate）。

注解有着多种用途，这些用途中：

- **为编译器提供信息** -- 编译器可使用注解来探测错误或抑制告警信息（**Information for the compiler** -- Annotations can be used by the compiler to detect errors or suppress warnings）；
- **编译时与部署时的处理** -- 软件工具可对注解信息加以处理，从而生成代码、XML文件等等（**Compile-time and deployment-time processing** -- Software tools can process annotation information to generate code, XML files, and so forth）;
- **运行时的处理** -- 在运行时，可对一些注解进行检查（**Runtime processing** -- Some annotations are available to be examined at runtime）。

本课程解释了：

- [哪些地方可以使用注解语法](#where)、
- 如何运用注解语法、
- [Java 平台以及标准版（Standard Edtion, Java SE API）中有哪些可用的预定义好的注解类型（annotation types）](#predefined_annotation_types)、
- 类型注解如何与可插拔类型系统结合以编写出有着更强类型检查的代码，
- 以及怎样去实现重复性注解语法。

（This lesson explains where annotations can be used, how to apply annotations, what predefined annotation types are available in the Java Platform, Standard Edition(Java SE API), how type annotations can be used in conjuncton with pluggable type systems to write stronger type checking, and how to implement repeating annotations.）

## 注解语法基础

**Annotations Basics**

### 注解语法的形式

**The Format of an Annotation**

注解语法的最简单形式，看起来像下面这样：

```java
@Entity
```

这个所在符号字符（`@`），是给编译器表明，接下来的是个注解。在下面的示例中，那个注解的名称为 `Override`:

```java
@Override
void mySuperMethod() {...}
```

注解可以包含 *元素（elements）*，元素可以是具名的或不具名的，同时存在着这些元素的值：

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

在注解没有元素时，那么那对括号是可以省略的，就如同在上面的 `Override` 示例中那样。

在同一声明上，使用多个注解也是可能的：

```java
@Author (name = "Jane Doe")
@EBook
class MyClass {...}
```

在若干注解有着同样类型时，此时就叫重复注解（a repeating annotation）：

```java
@Author(name = "Jane Doe")
@Author(name = "John Smith")
class MyClass {...}
```

从 Java SE 8 发布才开始支持重复注解。更多的有关情况，请参考 [重复注解](#repeating_annotations)

注解类型可以是定义在 Java SE API 的 `java.lang` 或 `java.lang.annotation` 包中类型之一。在上面的示例中，`Override`与 `SuppressWarnings`，就是 [预定义的Java 注解（predefined Java annotations）](#predefined_java_annotations)。定义自己的注解类型，也是可能的。前面示例中的 `Author` 与 `EBook` 就属于自定义注解类型。

### <a id="where"></a>哪些地方可以使用注解语法

可在声明上应用注解：类、字段、方法，以及其他一些程序元素的声明上。当在某个声明上运用了注解语法时，依照约定，每条注解常常占据自己的一行。

自Java SE 8 发布开始，注解语法还可应用到类型的 *使用*。下面是一些示例：

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

这种形式的注解，叫做 *类型注解（a type annotation）*。有关类型注解的更多信息，请参考 [类型注解与可插拔类型系统（Type Annotations and Pluggable Type Systems）](#type_annotations_and_pluggable_type_systems)。

## 注解类型的声明

**Declaring a Annotation Type**

许多的注解，在代码中都是起到替代注释的作用。

设想某个软件团队，他们在编写所有类的代码体时，传统上都是以提供这些类重要信息的注释开始的：

```java
public class Generation3List extends Generation2List {
    // Author: John Doe
    // Date: 3/17/2002
    // Current revision: 6
    // Last modified: 4/12/2004
    // By: Jane Doe
    // Reviewers: Alice, Bill, Cindy
    
    // 真正的类代码从这里开始
}
```

而要以注解来加入这些同样元数据，就必须首先定义出这个 *注解类型（annotation type）*。定义此注解类型的语法为：

```java
@interface ClassPreamble {
    String author();
    String date();
    int currentVersion() default 1;
    String lastModified() default "N/A";
    String lastModifiedBy() default "N/A";
    // 注意下面这个使用到数组
    String[] reviewers();
}
```

这个注解类型定义，看起来类似于接口定义，其中关键字 `interface` 前面冠以了位处符号（`@`）（当这个位处符号是在注解类型中时，就等于位处`AT`）。所有注解类型，都属于 *接口* 的一种形式，[本课程稍后](Ch08_Interfaces_and_Abstract_Classes.md#interface_rescue)会讲到这一点。此刻还不需要掌握什么是接口。

上面注解定义的代码体，包含了 *注解类型元素（annotation type element）* 的一些声明，这些什么看起来很像是一些方法。请留意这些注解类型元素，可定义一些可选的默认值。

在注解类型定义好之后，带上填入的各个取值，就可以使用那种类型的注解了，如同下面这样：

```java
@ClassPreamble {
    author = "John Doe",
    date = "3/17/2002",
    currentRevision = 6,
    lastModified = "4/12/2004",
    lastModifiedBy = "Jane Doe",
    // 请注意这里的数组注解
    reviewers = {"Alice", "Bob", "Cindy"}
}
public class Generation3List extends Generation2List {
    // 类的代码从这里开始
}
```

> **注意**：为了让 `@ClassPreamble` 中的信息，在 `Javadoc` 所生成的文档中出现，那么就必须以 `@Documented` 注解，来对 `@ClassPreamble` 的定义进行注解（To make the information in `@ClassPreamble` appear in `Javadoc-generated` documentation, you must annote the `@ClassPreamble` definition with the `@Documented` annotation）：

```java
// 为使用 @Documented 注解类型，就要导入该包
import java.lang.annotation.*;

@Documented
@interface ClassPreamble {
    // 那些注解元素的定义
}
```

## <a id="predefined_annotation_types"></a>Java中预定义的注解类型

**Predefined Annotation Types**

在 Java SE API 中，预先定义了一些注解类型。其中一些为Java编译器使用到，另一些是应用到别的注解的。

### Java语言用到的注解类型

**Annotation Types Used by the Java Language**

在包 `java.lang` 中预定义的注解类型为：`@Deprecated`、`@Override`及 `SuppressWarnings`。

- **`@Deprecated`** `@Deprecated` 注解表明其所标记的元素，是 *已弃用的* 且不应再被使用。在程序使用了带有 `@Deprecated` 注解的方法、类或字段（实例变量）时，编译器就会生成一条告警信息。而在某个元素为已被弃用时，就应像下面这个示例一样，使用 `Javadoc` 的 `@deprecated` 标签，将其在文档中记录下来。在`Javadoc`的注释中，和在注解语法中同时使用位处符号（`@`）的做法，并非巧合：`Javadoc`与注解语法，在概念上是有关联的。同时，请留意`Javadoc`的标签是以小写的 *`d`* 打头的，而注解语法是以大写的 `D` 打头的。

```java
    // 接下来是 Javadoc 的注释
     /**
      * @deprecated
      * 给出了为何这个方法被弃用的解释
      */
     @Deprecated
     static void deprecatedMethod() {}
}
```

- **`@Override`** `@Override` 注解告诉编译器，该元素是要重写在某个超类中声明的元素。在 [继承与多态机制](Ch07_Inheritance_and_Polymorphism_Better_Living_in_Objectville.md) 中讨论了方法的重写。

```java
// 将方法标记为一个已被重写的超类方法
@Override
int overriddenMethod () {}
```

尽管在重写某个方法时，并不要求使用这个注解，不过这样做可以防止错误发生。在某个以 `@Override` 标记的方法，未能正确重写其超类的某个方法时，编译器就会生成一个错误消息。

- **`@SuppressWarnings`** `@SuppressWarnings` 注解，告诉编译器要抑制一些本来会生成的告警信息。在下面的示例中，使用了某个已被弃用的方法，进而编译器一般会生成一条告警信息。不过在此实例中，由于这个注解，而导致该告警信息被抑制下来了。

```java
// 这里使用了一个已被弃用的方法，并告诉编译器不要生成一条告警消息
@SuppressWarnings ("deprecation")
void useDeprecatedMethod () {
    // 这里的已被弃用告警，就被抑制了
    objectOne.deprecatedMethod();
}
```
