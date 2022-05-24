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

### <a id="annotations-basics"></a>注解语法基础

**Annotations Basics**

#### 注解语法的形式

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

#### <a id="where"></a>哪些地方可以使用注解语法

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

### 注解类型的声明

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

### <a id="predefined_annotation_types"></a>Java中预定义的注解类型

**Predefined Annotation Types**

在 Java SE API 中，预先定义了一些注解类型。其中一些为Java编译器使用到，另一些是应用到别的注解的。

#### Java语言用到的注解类型

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

所有编译器告警，都是属于某个类别的。Java 语言规范（The Java Language Specification）列出了两个类别：`deprecation` 与 `unchecked`。在碰到那些引入 [泛型](Ch16_Collections_and_Generics_Data_Structure.md) 之前所编写的老旧代码时，其中的 `unchecked` 告警便会出现。要将这多个类别的告警给抑制掉，就要使用下面的语法：

```java
@SuppressWarnings({"unchecked", "deprecation"})
```

- **`@SafeVarargs`** 在对某个方法或构造器应用 `@SafeVarargs` 注解时，就假定了代码不会在其 `varargs` 实参上，执行潜在不安全的操作。在使用了此注解类型时，那些未受检查的、与 `varargs` 有关的告警，就会被抑制（`@SafeVarargs` annotation, when applied to a method or constructor, asserts that the code does not perform potentially unsafe oprations on its `varargs` parameter. When this annotation type is used, unchecked warnings relating to `varargs` are suppressed）。
- **`@FunctionalInterface`** `@FunctionalInterface` 注解，是在 Java SE 8 中引入的，表示其所注解的类型声明，是计划作为Java语言规范中所定义的功能性接口（`@FunctionalInterface` annotation, introduced in Java SE 8, indicates that the type declaration is intended to be a functional interface, as defined by the Java Language Specification）。

#### 适用于其他注解的注解

**Annotations That Apply to Other Annotations**

适用于其他注解的注解，叫做 *元注解（meta-annotations）*。在 `java.lang.annotation` 包中，定义了若干元注解类型。

- **`@Retention`** `@Retention` 注解指定了所标记的那些注解该如何存储（`Retention` annotation specifies how the marked annotation is stored）:
- **`@Documented`** `@Documented` 注解表示不论指定注解在何时被使用到，那些元素都应使用 `Javadoc` 工具以文档化处理。（默认情况下，注解是没有包含在 `Javadoc` 中的。`@Documented` annotation indicates that whenever the specified annotation is used those elements should be documented using the `Javadoc` tool）。有关 `Javadoc` 的更多信息，请参考 [`Javadoc` 工具部分](#javadoc-tool)。
+ **`Target`** `Target` 注解对另一注解进行标记，以限制所标记的注解可适用的Java元素种类。`@Target` 注解，会将以下的一些元素类型，指定为他的取值：

    - `ElementType.ANNOTATION_TYPE` - 被 `@Target` 注解标记的注解可适用于另一注解类型；
    - `ElementType.CONSTRUCTOR` - 被其标记的注解可适用于构造器；
    - `ElementType.FIELD` - 可适用于字段或属性；
    - `ElementType.LOCAL_VARIABLE` - 可适用于本地变量；
    - `ElementType.METHOD` - 可适用于方法级别的注解；
    - `ElementType.PACKAGE` - 可适用于包的声明；
    - `ElementType.PARAMETER` - 可适用于方法的参数；
    - `ElementType.Type` - 可适用于类的任意元素。

- **`@Inherited`** `@Inherited` 注解表示其所标记的注解类型，可从超类继承到（`@Inherited` annotation indicates that the annotation type can be inherited from the super class）。（默认是不可以从超类继承的。）在用户查询某个注解类型，而该类又没有这个类型的注解时，那么就会对这个类的超类查询此注解类型。此注解仅适用于类的声明。
- **`@Repeatable`** `@Repeatable` 注解，是在Java SE 8 中引入的，表示所其所标记的注解，可多次应用到同意声明或类型用途。更多的有关信息，请参阅 [重复注解（Repeating Annotations）](#repeating-annotations)。

### 类型注解与可插拔类型系统

**Type Annotations and Pluggable Type Systems**

在 Java SE 8 发布之前，注解只能适用于声明。而自Java SE 8 发布开始，注解就还可以适用到任何 *类型用途（type use）* 了。这意味着在所有用到类型的地方，都可以使用注解。有用到类型之处的一些示例，包括类实例创建表达式（`new`）、类型转换（casts）、`implements` 子语句，以及 `throws` 子语句等。这种形式的注解，被称为 *类型注解（type annotation）*，在之前的 [注解基础](#annations-basics)那里，给出了几个示例。

之所以创建出这些类型注解，是为了以确保以更强大的类型检查方式，支持对Java程序的分析。Java SE 8发布，并未提供类型检查框架，不过Java SE 8 是允许编写（或下载）作为一个或多个可插拔式模组实现的、与Java 编译器结合使用的类型检查框架的（Type annotations were created to support improved analysis of Java programs way of ensuring stronger type checking. The Java SE 8 release does not provide a type checking framework, but it allows you to write(or download) a type checking framework that is implemented as one or more pluggable modules that are used in conjunction with the Java compiler）。

比如，在想要程序中某个特定变量绝不被赋值 `null`，以期望避免触发 `NullPointerException`。那么就可以编写一个定制的插件，来对此进行检查。随后就要对代码进行修改，来对那个特定变量进行注解，从而表明其绝不会被赋值 `null`。此变量声明看起来将像这样：

```java
@NonNull String str;
```

在编译此代码时，就要在命令行包含进 `NonNull` 模组，那么编译器在探测到潜在问题时，就会打印出告警信息，进而允许对代码加以修改，以避免错误。在纠正了代码而消除了全部告警之后，在程序运行期间这特定错误便不再会出现了。

可使用多个类型检查模组（multiple type-checking modules），其中各个模组对不同类别多维进行检查。这样的话，就可以在Java类型系统之上进行构建，在需要这些特定类型检查的各个时间点、位置，按需添加这些类型检查。

明智地使用类型注解，加上可插拔类型检查器（pluggable type checkers）的存在，那么就可以编写出更加强大又不会出错的代码了。

在许多情形中，是不必编写自己的类型检查模组的。有很多第三方以及完成了这方面的工作。比如，或许会利用到华盛顿大学所创建的检查器框架 `Checker Framework`。该框架就包含了`NonNull` 模组，还有正则表达式模组（a regular module），以及互斥锁（a mutex lock module）。更多有关该检查器框架的信息，请参考 [检查器框架](https://checkerframework.org/)。

### <a id="repeating-annotations"></a>重复注解

**Repeating Annotations**

有些情况下，会希望将同一注解应用到声明或类型运用（a declaration or type use）。自Java SE 8 发布开始，*重复注解* 才支持该特性。

比如正在编写要用到定时器服务，来实现在给定时间或以某个特定时间表，去运行某个方法，类似于 UNIX 的 `cron` 服务的代码。那么就要设置一个定时器，来在一个月的最后一天，及每个周五晚上11点运行一个方法，`doPeriodicCleanup`。那么就要设置定时器运行，创建一个 `@Schedule` 注解，并将其两次应用到 `doPeriodicCleanup` 方法。第一次的使用，指定一个月的最后一天，同时第二次指定周五晚上11点，就跟下面的代码示例一样：

```java
@Schedule(dayOfMoth="last")
@Schedule(dayOfWeek="Fri", hours="23")
public void doPeriodicCleanup() {...}
```

上面这个示例，是将注解应用到方法。可在任何会用到标志注解的地方，对某个注解加以重复。比如，有着一个处理未授权访问异常的类。就可以对这个类注解上一个针对那些管理者的 `@Alert` 的注解，和一个针对系统管理员的 `@Alert`：

```java
@Alert(role="Manager")
@Alert(role="Administrator")
public class UnauthorizedAccessException extends SecurityException {...}
```

由于兼容性的原因，重复注解是被保存在由Java编译器自动生成的 *容器注解（container annotation）* 中的。为了让编译器完成此操作，就要求在代码中包含下面这两个声明。

#### 步骤一、声明一个 `Repeatable` 类型的注解类型

**Step 1: Declare a `Repeatable` Annotation Type**

该注解类型必须被 `@Repeatable` 元注解（the `@Repeatable` meta-annotation）标记。下面的示例定义了一个定制的 `@Schedule` 可重复注解类型：

```java
import java.lang.annotation.Repeatable;

@Repeatable(Schedules.class)
public @interface Schedule {
    String dayOfMonth() default "first";
    String dayOfWeek() default "Mon";
    int hour() default 12;
}
```

那个 `@Repeatable` 元注解的取值，即在括号里的那个，即为Java编译器生成的、用于存储重复性注解的容器注解的类型（The value of the `@Repeatable` meta-annotation, in parentheses, is the type of the container annotation that the Java compiler generates to store repeating annotations）。在此示例中，那个容纳注解类型，即是 `Schedules`，那么重复性的 `@Schedule` 注解，就是存储在一个 `@Schedules` 的注解中的。

在没有首先将某个注解声明未可重复注解的情况下，将同一注解应用到某个声明，就会导致一个编译器错误。

#### 步骤二、声明出那个容纳注解类型

**Step 2: Declare the Containing Annotation Type**

容纳性注解类型，必须有着一个数组类型的 `value` 元素。该数组类型的组件类型，则必须时那个可重复注解类型。这个 `@Schedules` 容纳性类型的声明，就是下面这样：

```java
public @interface Schedules {
    Schedule[] value();
}
```

#### 注解的获取

**Retrieving Annotations**

反射式API中，有着多个可用于获取到注解的方法。这些方法返回单个的、譬如`AnnotatedElement.getAnnotation(Class<T>)`这样的注解的表现，与存在 *一个* 这些方法所请求类型注解，而仅返回返回单个注解相比，是不会发生变化的（The behavior of the methods that return a single annotation, such as `AnnotatedElement.getAnnotation(Class<T>)`, are unchanged in that they only return a single annotation if *one* annotation of the requested type is present）。但若存在多个的所请求类型的注解时，那么就可以首先获取到这些注解的容器。这样的话，那些老旧代码会继续工作。在Java SE 8中引入的其他一些方法，则会对容器注解进行全面扫描，从而一次性返回多个注解，就如同 `AnnotatedElement.getAnnotationsByType(Class<T>)`那样。请参考 [`AnnotatedElement`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/AnnotatedElement.html) 类规格，以了解有关全部方法的信息。

#### 设计上的考量

**Design Considerations**

在设计某个注解类型时，必须考虑到那个类型下注释的 *基数（cardinality）*。目前会用到这个注解 0 次、1 次都是可能的，而在这个注释被标记为了 `@Repeatable` 时，那么就可以多次使用了。同时通过使用 `@Target` 元注解，还可以限制注解可用在何处。比如，即可创建一个可重复的、仅可用在方法及字段上的注解类型。为确保 *用到* 你所设计注解类型注解的程序员，发现他是灵活且强大的，那么就要仔细的加以设计（It is important to design your annotation type carefully to ensure the programmer *using* the annotation finds it to be as flexible and powerful as posssible）。

## 提问与练习：注解

### 问题

1. 下面这个接口什么错了？

```java
public interface House {
}
