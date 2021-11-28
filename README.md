# Java 编程学习项目

这是一个学习 Java 的项目。使用 Eclipse IDE/VIM、Maven/NPM 构建工具。后期会涉及到 NPM 的使用。主要学习 Java 的基础知识，Java 本地应用程序的编写。 __项目使用 JDK `11.0.12`__ 语言环境。

## 0. 环境搭建

本项目是在 Ubuntu Linux 20.04 LTS 上进行的。


- 安装JDK
    JDK的当前LTS版是 `JDK 17.0.1`（Oracle, 2021-11），你可以直接从 [Java Downloads | Oracle](https://www.oracle.com/java/technologies/downloads/) 下载，本项目所使用的 `JDK 11.0.12` 需要在 Oracle 网站注册才能下载。`jdk-11.0.12_linux-x64_bin.tar.gz` 压缩包有 183M 大小。

    运行下面的命令，就安装好 Oracle 的 JDK 了。

```bash
$sudo tar xf /path/to/jdk-11.0.12_linux-x64_bin.tar.gz -C /opt/
$sudo ln -s /opt/jdk-11.0.12 /opt/jdk
$sudo update-alternatives --install "/usr/bin/java" "java" "/opt/jdk/bin/java" 1
$sudo update-alternatives --install "/usr/bin/javac" "javac" "/opt/jdk/bin/javac" 1
```

运行 `$java -version` 与 `$javac -version` 测试 JDK 是否安装妥当。

- 安装Maven
    [Apache Maven](http://maven.apache.org/) 可以直接下载，下载到的 `apache-maven-3.8.4-bin.tar.gz`压缩包，大小约 9M，安装步骤与JDK的略有不同：

```bash
$sudo tar xf /path/to/apache-maven-3.8.4-bin.tar.gz -C /opt/
$sudo ln -s /opt/apache-maven-3.8.4 /opt/mvn
$sudo vim.gtk /etc/profile.d/mvn.sh
$sudo chmod +x /etc/profile.d/mvn.sh
```

其中建立了一个 `/etc/profile.d/mvn/sh` 文件，并修改为了可执行文件。该文件的内容为：

```sh
export JAVA_HOME=/opt/jdk
export M2_HOME=/opt/mvn
export MAVEN_HOME=/opt/mvn
export PATH=${M2_HOME}/bin:${PATH}
```

随后可将国内的华为 MAVEN 代码仓库，设置为MAVEN的默认代码仓库。运行如下命令：

```bash
$if [ -d $HOME"/.m2" ]; then :; else echo "~/.abc not exist. Now make this directory"; mkdir $HOME"/.m2"; fi
$wget -qO ~/.m2/settings.xml https://mirrors.huaweicloud.com/api/v1/configurations/maven?
```

- 安装NVM
- 配置 `~/.vimrc`

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

- [类与对象：对象村之旅](docs/Ch02_Class_and_Object_A_Trip_to_Objectville.md)
- [原生与引用：熟知你的变量](docs/Ch03_Primitives_and_References_Know_Your_Variables.md)
- [方法对实例变量的使用：对象的行为方式](docs/Ch04_Methods_Use_Instance_Variables_How_Objects_Behave.md)
- [实战编程：给方法赋能](docs/Ch05_Writing_a_Program_Extra-Strength_Methods.md)
- [了解Java API：使用Java的库](docs/Ch06_Get_to_Know_The_Java_API_Using_the_Java_Library.md)
- [获得GUI：一个甚为形象的故事](docs/Ch12_Getting_GUI_A_Very_Graphic_Story.md)
