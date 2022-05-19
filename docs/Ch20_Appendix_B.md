# 附录B：其他特性

## 注释语法

**Annotations**

*注释*，是元数据的一种形式，提供了不作为程序本身部分的、有关程序的一些数据。注释对其所注释代码运作，并未直接影响（*Annotations*, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate）。

注释有着多种用途，这些用途中：

- **为编译器提供信息** -- 编译器可使用注释来探测错误或抑制告警信息（**Information for the compiler** -- Annotations can be used by the compiler to detect errors or suppress warnings）；
- **编译时与部署时的处理** -- 软件工具可对注释信息加以处理，从而生成代码、XML文件等等（**Compile-time and deployment-time processing** -- Software tools can process annotation information to generate code, XML files, and so forth）;
- **运行时的处理** -- 在运行时，可对一些注释进行检查（**Runtime processing** -- Some annotations are available to be examined at runtime）。
