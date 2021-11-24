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
