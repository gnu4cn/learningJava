# 附录B：其他特性

## 注释语法

**Annotations**

*注释*，是元数据的一种形式，提供了不作为程序本身部分的、有关程序的一些数据。注释对其所注释代码运作，并未直接影响（*Annotations*, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate）。

注释有着多种用途，这些用途中：

- **为编译器提供信息** -- 编译器可使用注释来探测错误或抑制告警信息（**Information for the compiler** -- Annotations can be used by the compiler to detect errors or suppress warnings）；
- **编译时与部署时的处理** -- 软件工具可对注释信息加以处理，从而生成代码、XML文件等等（**Compile-time and deployment-time processing** -- Software tools can process annotation information to generate code, XML files, and so forth）;
- **运行时的处理** -- 在运行时，可对一些注释进行检查（**Runtime processing** -- Some annotations are available to be examined at runtime）。

本课程解释了：

- 哪些地方可以使用注释语法、
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
