# Java 编程学习项目

这是一个学习 Java 的项目。使用 Eclipse IDE、Maven 构建工具。后期会涉及到 NPM 的使用。主要学习 Java 的基础知识，Java 本地应用程序的编写。

## 1. 关于文件/目录结构

在编写 Java 程序时，使用 Maven 构建，必须以这样的 `src/main/java/com/xfoss/learningJava/*.java` 的文件/目录结构，否则会报错：

```bash
peng@uDesktop:~/eclipse-workspace/learningJava$ java -jar target/com.xfoss.learningJava-0.0.1.jar
Error: Could not find or load main class com.xfoss.learningJava.PhraseOMatic
Caused by: java.lang.ClassNotFoundException: com.xfoss.learningJava.PhraseOMatic
```

> 同时，`packege com.xfoss.com.learningJava;` 对应的包 `com.xfoss.learningJava` 必须以这个名字命名，这也是上面目录中 `/com/xfoss/learningJava` 的来源。

## 2. 使用 `exec-maven-plugin`

使用这个插件，可以开启 `mvn exec:java` 命令，实时运行（正在）编写的 Java 程序。该插件支持两种运行模式，一种是在 Maven 所在线程里运行（需要修改 MAVEN 运行参数 `export MAVEN_OPTS=-Xmx1024m`，以增加JVM虚拟机的运行内存）；另一种以独立线程运行（尚需测试）。

## 3. `pom.xml` 语法


- 可以在 `properties` tag里定义一些变量，在后面通过 `${x.y}` 的方式引用

## 目录

- [对象村之旅](docs/Ch02_A_Trip_to_Objectville.md)
- [获得GUI：一个甚为形象的故事](docs/Ch12_Getting_GUI_A_Very_Graphic_Story.md)
