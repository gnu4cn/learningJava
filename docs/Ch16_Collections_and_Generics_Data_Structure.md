# 集合与泛型：数据结构

**Collections and Generics: Data Structure**

**在 Java 里头，排序就是小菜一碟**。无需编写自己的排序算法，就有了收集与操纵数据的全部工具（除非在阅读本章时，正坐在计算机科学101, CS 101 课程的教室里，那么在我们Java程序员简单地调用一个Java API中方法时，你就要去编写那个排序方法了）。Java 的集合框架，有着一种几乎全部需要完成事情的数据结构（The Java Collections Framework has a data structure that should work for virtually anything you'll ever need to do）。想要有个可轻易持续地往其上添加的清单吗？想要通过名字来找到某个物件吗？想要创建一个可自动排除所有重复项目的清单吗？要按照对你背后捅刀次数，对你的同事们排个序吗？把你的宠物，按照他们掌握的把戏排个序怎么样？本章就是关于这些的......

## 追踪自动唱机上歌曲流行度

**Tracking song popularity on your jukebox**

恭喜你获得了新的任务--管理楼氏餐厅的自动唱机系统。这自动唱机里头，本身并没有Java，不过在每次有人点播了一首歌时，歌曲数据就会被追加到一个简单的文本文件。

你的任务，就是对这个数据进行管理，从而跟踪到歌曲流行度，生成一些报告，进而修改那些播放清单。这里并非要编写整个的 app -- 别的一些软件开放者/侍应生也会参与进来，你所要负责的，仅是对整个Java app内的数据加以管理和排序。而由于楼老板抵触数据库，因此数据是严格来说是个内存中的数据集（an in-memory data collection）。所得到的全部，就是那个自动唱机持续添加数据的文件。你的任务，就是从那里取得数据。

先前我们已经掌握了怎样读取和解析该文件，并且到目前位置，都是将数据保管在一个 `ArrayList` 中的。

**#1 挑战**

**对这些歌曲，按照字母顺序排序**


在某个文件中，有着一个歌曲的清单，其中各行分别表示一首歌曲，且歌曲标题与艺术家，是以正斜杠分开的。那么对这样的行进行解析，进而把全部歌曲放入到一个 `ArrayList` 里头就简单了。

![SongList.txt](images/Ch16_01.png)

*图 1 - SongList.txt*

> *这就是那个自动点唱机设备所写入的文件。这里的代码必须读取整个文件，随后对歌曲数据进行操作*。

老板只对歌曲标题感兴趣，因此现在就可以简单地构造一个只有歌曲标题的清单就行。

然而会发现整个清单不是以字母顺序的......这里可以做点什么呢？

我们知道对于一个 `ArrayList` 来说，那些元素保持着将其插入到清单中的顺序，那么把这些元素放入到 `ArrayList` 中，就不会留意到他们的字母排序的，除非......`ArrayList`类中，有着一个`sort()`方法。

**下面就是到目前为止，不带排序的样子**：

```java
package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;
import com.xfoss.Utils.XPlatformThings;

public class JukeBox1 {

    // 这里将把那些歌曲标题保管在一个字符串的 ArrayList 中。
    ArrayList<String> songList = new ArrayList<String> ();

    String wDir = XPlatformThings.getWorkingDir("learningJava");

    // 启动加载文件并打印那个 songList 的 ArrayList 的构造函数。
    public JukeBox1 () {
        getSongs();
        System.out.println(songList);
    }

    public static void main(String[] args){
        new JukeBox1();
    }

    // 这里并无什么特别之处......只是读取文件并针对各行调用 addSong() 方法。
    void getSongs() {
        try {
            File file = new File(String.format("%s/SongList.txt", wDir));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                addSong(line);
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }

    // 这个 addSong 方法就如同 I/O 章中 QuizCard 一样 -- 运用 split() 方法
    // 将行（有着歌曲标题与艺术家）拆开为两个片段（令牌）。
    void addSong(String lineToParse) {
        String [] tokens = lineToParse.split("/");
        // 这里只要歌曲标题，因此只将第一个令牌添加到 songList（即那个 ArrayList）。
        songList.add(tokens[0]);
    }
}
```


![JukeBox1](images/Ch16_02.png)

*图 2 - JukeBox1*

> *songList 会以这些歌曲标题被添加到这个 `ArrayList` （其中的顺序与这些歌曲在原始文本文件中的顺序相同）的顺序，打印出这些歌曲标题*。
>
> 显然这不是以字母顺序排序的！

### 然而类 `ArrayList` 并没有`sort()` 方法！

在检视 `ArrayList` 时，看起来那里是没有任何有关排序的方法。即便往上检索他的继承树也毫无助益--明显 ***在 `ArrayList` 上是无法调用到某个排序方法的***。

![Java API - ArrayList](images/Ch16_03.png)

*图 3 - Java API - ArrayList*

> *`ArrayList` 有着很多方法，不过却没有可用于排序的*......


### `ArrayList` 并非唯一的集合

**`ArrayList` is not the only collection**

> *我的确见到过一个名为 `TreeSet` 的集合类......并且文档讲到这个集合类保持着排序后的数据。我就想知道是不是应该使用`TreeSet`而非`ArrayList`......（I do see a collection class called `TreeSet`...and the docs say that it keeps your data sorted. I wonder if I should be using a `TreeSet` instead of an `ArrayList`...）*


虽然`ArrayList`是在今后会用到最多的集合类，对于特殊情形，还是有一些其他的集合类。其中一些关键集合类，包括以下几个：

> *请不要急于去了解别的那些集合类。稍后会涉及到更多的细节*。


- **`TreeSet`**

    让元素保持排序并防止重复（Keeps the elements sorted and prevents duplicates）。

- **`HashMap`**

    实现了名称/值对方式的元素存储与访问（Let you store and access elements as name/value pairs）。

- **`LinkedList`**

    令到诸如栈与队列等数据结构的创建容易起来（Make it easy to create structures like stacks or queues）。

- **`HashSet`**

    去除集合中的重复，同时对于给定元素，可快速在集合中找出来（Prevents duplicates in the collection, and given an element, can find that element in the collection quickly）。

- **`LinkedHashMap`**

    与常规 `HashMap` 类似，但他可以记住其中元素（名/值对）插入的顺序，也可以被配置为记住那些最近被访问过元素的顺序（Like a regular `HashMap`, except it can remember the order in which elements(name/value pairs) were inserted, or it can be configured to remember the order in which elements were last accessed）。 


### 可以使用`TreeSet`......，也可以使用`Collections.sort()方法`


在将全部字符串（即那些歌曲标题）放入到一个 **`TreeSet`** 而非 `ArrayList`中时，这些字符串就会自动以正确位置，即字母顺序着地。之后无论何时打印出这个清单，这些元素都会始终以字母顺序输出。

在需要一个 *集合（set）*（接下来就会讲到什么是集合），或可以肯定清单必须 *始终* 保持字母排序时，这样处理是相当不错的。

不过在别的情况下，在不需要清单保持排序时，相比这样的需求，使用`TreeSet`就显得有些代价高昂 -- ***在每次往`TreeSet`插入元素时，`TreeSet`都必须花时间去找出应在何处插入这个元素***。而使用 `ArrayList`，由于新元素只会在清单末尾加入，那么元素插入就可以快得让人窒息。

![Java API `java.util.Collections`](images/Ch16_04.png)

*图 4 - Java API `java.util.Collections`*

> *嗯......在 `Collections` 类中确实有个 `sort()` 方法。他会取得一个 `List`，同时由于`ArrayList`实现了 `List` 接口，因此 `ArrayList` `IS-A` `List`。归功于多态机制，就可以将`ArrayList`传递给声明了取得 `List` 的方法*。
>
> *请注意*：这并非是一个真正的 `Collections` 类的API文档；这里通过省略有关泛型（the generic type, 将在本章后面讲到）的信息，而对其进行了简化。


- **不是可以把元素添加到`ArrayList`的特定索引处，而不是他的末尾的吗 -- 确实有一个过载的`add()`方法，连同要添加的元素一道，还取得一个整型参数呢。那么这样就不会比直接插入到清单末尾更慢吗**？

> 是的，在 `ArrayList` 末尾插入元素，在其他地方是要慢一些的。因此使用过载的`add(index, element)` 方法，就不如调用 `add(element)` -- 这会把添加的元素放在清单末尾，这样来得快。然而在大部分用到`ArrayList`的时候，是无需将某个元素放在指定索引处的。


- **我看见那里有一个 `LinkedList` 类，那么是不是使用 `LinkedList`，就可以更好地实现在清单中间插入元素呢？至少我还记得大学时学过的数据结构课**......

> 是的，讲得不错。在从清单中中间插入或移除元素时，`LinkedList`要快一些，然而对于大多数应用，除非是在处理 *巨量* 的元素，那么往 `LinkedList` 与 `ArrayList` 的中间插入的区别，通常不足以纳入考量。后面很快就会深入了解 `LinkedList` 类。

### 把`Collections.sort()` 方法添加到 `JukeBox` 代码


> **`Collections.sort()` 方法，会将字符串清单，以字母顺序进行排序**。

```java
package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;
import com.xfoss.Utils.XPlatformThings;

public class JukeBox1 {
    ArrayList<String> songList = new ArrayList<String> ();
    String wDir = XPlatformThings.getWorkingDir("learningJava");

    public JukeBox1 () {
        getSongs();
        System.out.println(songList);

        // 对静态类 `Collections` 的 `sort()` 方法进行调用，并
        // 再次打印处这个清单。这第二个打印输出，就是以字母顺序的了！
        Collections.sort(songList);
        System.out.println(songList);
    }

    public static void main(String[] args){
        new JukeBox1();
    }

    void getSongs() {
        try {
            File file = new File(String.format("%s/SongList.txt", wDir));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                addSong(line);
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }

    void addSong(String lineToParse) {
        String [] tokens = lineToParse.split("/");
        songList.add(tokens[0]);
    }
}
```

![加入了 `Collections.sort()` 方法后的 `JukeBox1` 程序](images/Ch16_05.png)


*图 5 - 加入了 `Collections.sort()` 方法后的 `JukeBox1` 程序*


### 然而现在需要的是 `Song` 对象，而不是那些简单的字符串

现在老板想要清单中的那些具体 `Song` 类实例，而不仅仅是一些字符串，那么每个 `Song` 就要有更多的数据了。新的自动点唱机装置输出了更多的信息，那么这次这个文件就会有 *四个* 片段（令牌），而不再仅仅两个了。

类 `Song` 是相当简单的，仅有一个感兴趣的特性 -- 一个重写了的 `toString()` 方法。请记住，这个 `toString()`方法，是在类 `Object` 中定义的，因此Java 中的每个类，都继承了这个方法。同事由于在打印某个对象（`System.out.println(anObject)`）时，会调用到这个对象上的 `toString()` 方法，因此就应该对其进行重写，来打印出一些比起默认唯一识别符代码，更具可读性的东西。在打印某个歌曲对象清单时，将调用到各个 `Song` 对象上的这个 `toString()` 方法。

```java
class Song {

    // 这四个实例变量表示文件中的四个歌曲属性。
    private String title;
    private String artist;
    private String rating;
    private String bpm;

    // 这些变量都是在新的 Song 对象被创建时，在构造器中设置的。
    Song (String t, String a, String r, String b) {
        title = t;
        artist = a;
        rating = r;
        bpm = b;
    }

    // 这些是四个属性的获取器方法。
    public String getTitle () {
        return title;
    }

    public String getArtist () {
        return artist;
    }

    public String getRating () {
        return rating;
    }

    public String getBpm () {
        return bpm;
    }

    // 由于在执行 System.out.println(aSongObject)时，希望看到歌曲标题，因此
    //  这里重写了 toString() 方法。在执行 System.out.println(aListOfSongs) 
    //  时，就会调用清单中各个元素的这个 toString() 方法。
    public String toString () {
        return title;
    }
}
```

### 将 `JukeBox` 代码修改为使用 `Song` 对象而非那些字符串

代码只会修改很少 -- 文件 I/O 代码还是一样，且解析代码也一样（`String.split()`静态方法），这次不一样的，是每行/每首歌曲将有 *四个* 令牌，同时全部四个都将用于构造一个新的 `Song` 对象。同时理所应当的这个 `ArrayList` 将是类型 `<Song>` 而非 `<String>` 了。


```java
package com.xfoss.CollectionAndGenerics;

import java.util.*;
import java.io.*;
import com.xfoss.Utils.XPlatformThings;

public class JukeBox3 {

    // 这里将这个 ArrayList 从 String 修改为了 Song 对象。
    ArrayList<Song> songList = new ArrayList<Song> ();
    String wDir = XPlatformThings.getWorkingDir("learningJava");

    public JukeBox3 () {
        getSongs();
        System.out.println(songList);

        Collections.sort(songList);
        System.out.println(songList);
    }

    public static void main(String[] args){
        new JukeBox3();
    }

    void getSongs() {
        try {
            File file = new File(String.format("%s/SongListMore.txt", wDir));
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                addSong(line);
            }
        } catch (IOException ex) {ex.printStackTrace();}
    }

    void addSong(String lineToParse) {

        // 这里使用四个令牌（也就是歌曲文件中这一行的四个信息片段）创建
        // 出一个新的 Song 对象，并将该 Song 对象添加到那个清单。
        String [] tokens = lineToParse.split("/");

        Song nextSong = new Song(tokens[0], tokens[1], tokens[2], tokens[3]);
        songList.add(nextSong);
    }
}
```


### 然而这代码不会被编译出来！


```console
/home/peng/eclipse-workspace/learningJava/src/main/java/com/xfoss/CollectionAndGenerics/JukeBox3.java:17:
    error: no suitable method found for sort(ArrayList<Song>)
           Collections.sort(songList);
                      ^
       method Collections.<T#1>sort(List<T#1>) is not applicable
         (inference variable T#1 has incompatible bounds
           equality constraints: Song
           lower bounds: Comparable<? super T#1>)
       method Collections.<T#2>sort(List<T#2>,Comparator<? super T#2>) is not applicable
         (cannot infer type-variable(s) T#2
           (actual and formal argument lists differ in length))
  where T#1,T#2 are type-variables:
    T#1 extends Comparable<? super T#1> declared in method <T#1>sort(List<T#1>)
    T#2 extends Object declared in method <T#2>sort(List<T#2>,Comparator<? super T#2>)
Note: Some input files use unchecked or unsafe operations.
```

某个地方出错了......那个 `Collections` 类明明是有个取`List` 做参数的 `sort()` 方法的。

`ArrayList` 确实是一个 `List`，因为 `ArrayList` 实现了接口 `List`，那么......这代码 *应该* 跑起来的。

***然而这代码并没有跑起来***!

编译器说，他无法找到适当的、取一个 `ArrayList<Song>` 作参数的 `sort` 方法，那么或许是这个 `sort()` 方法不喜欢一个 `Song`对象构成的 `ArrayList`？不过这个 `Collections.sort()` 方法又并不介意一个 `ArrayList<String>`，那么 `Song` 与 `String` 之间，到底有什么重要的区别呢？是什么差异导致了编译器的失败呢？

当然你或许已经想到，“排序的 *依据* 到底是什么？” 这个 `sort` 方法是怎样知道，是什么造成一个 `Song` 大于 另一`Song` 对象的？显然在希望歌曲标题作为判断这些歌曲排序方式时，就需要某种方式，来告知这个 `sort` 方法，他需要使用标题，而非比如说每分钟的拍数（the beats per minute, bpm）。

关于这点，正是这里接下来几页要讨论的问题，不过首先，让我们搞清楚，为何编译器甚至不会允许将一个 `Song` 的 `ArrayList` 传递给这个 `sort()` 方法。


### `Collections.sort()` 方法的声明

![Java API `Collections.sort()`](images/Ch16_06.png)


*图 6 - Java API `Collections.sort()`*

> *WTF? 我都不知道该怎么去看这个方法声明。他讲了 `sort()` 要取一个 `List<T>` 的参数，然而 `T` 是个啥？还有在返回值类型前那一大坨又是啥*？


从 API 文档（找找 `java.util.Collections` 类，然后滚动 `sort()` 方法），似乎这个 `sort()` 方法声明得...... *有些奇怪*。或者至少不同于先前见到的任何一个。

那是因为，这个 `sort()` 方法（连同Java中整个集合框架中的其他东西），重度运用了 *泛型（generics）*。不论何时，但凡在 Java 源码或文档中，见到某个东西带有尖括号，那就意味着泛型 -- 一个添加到 Java 5.0 版本中的特性。因此在搞清楚为何可以对 `ArrayList` 中的字符串对象进行排序，而无法对 `Song` 对象的`ArrayList` 进行排序之前，这里就不得不掌握如何去理解文档。

## 泛型意味着更好的类型安全

**Generics means more type-safety**


这里就会讲到这个 -- *我们所写的全部涉及到泛型的代码，实际上都将是有关集合的代码（virtually all of the code you write that deals with generics will be collection-related code）*。虽然泛型可以其他一些方式使用，但泛型的要点，是可以写出类型安全的集合。也就是那些可以令到编译器阻止我们把一个 `Dog` 对象，放入到一个 `Duck` 清单里去的代码。

> **注**：*关于 Collection 与 Set 的区别，请参考 [What is the difference between a set and a collection in Java?](https://www.quora.com/What-is-the-difference-between-a-set-and-a-collection-in-Java)*

在泛型之前（即在 Java 5.0 之前），由于所有集合实现，都被声明为保存类型 `Object`，因此编译器不会在乎放入到某个集合的为何物。那时可将 *任何东西* 都放入到所有 `ArrayList`；这就好比所有 `ArrayList` 都被声明为了 `ArrayList<Object>`。

> **有了泛型，就可以创建类型安全的集合，其中在编译时，而不是运行时就可以发现更多的问题**。
>
> **若没有泛型，那么编译器会痛快地允许将 `Pumpkin` 放入到本被假定为仅保管 `Cat` 对象的 `ArrayList`**。

![没有泛型与带有泛型的对比](images/Ch16_07.png)

*图 7 - 没有泛型与带有泛型的对比*

> *现在有了泛型，就只能将 `Fish` 对象放入到这个 `ArrayList<Fish>` 中，因此那些从这个清单中取出的对象，就是些`Fish` 引用变量了。不必担心有人会把一个 `Volkswagen` 放在那里面，或者担心从那里取出的对象，不会确实是个兼容 `Fish` 的引用变量*。

### 了解泛型

**Learning generics**

关于泛型，有很多需要掌握的东西，对于大多数程序员，则只需要了解下面三个：

1) 创建一些泛化类的实例（creating instances of generified classess, 比如 `ArrayList`）

`new ArrayList<Song> ()`

在构造一个 `ArrayList` 时，就必须告诉这个 `ArrayList` 类型的变量，这个清单中将允许的对象类型，这就跟原先那些普通数组一样。

2) 声明并赋值一些泛型的变量（declaring and assigning variables of generic types）

`List<Song> songList = new ArrayList<Song> ()`

多态机制与泛型到底是怎样一起生效的呢？在有着一个 `ArrayList<Animal>` 的引用变量时，可以将一个 `ArrayList<Dog>` 的变量赋值给他吗？对于一个 `List<Animal>` 的引用变量又如何呢？可以将一个 `ArrayList<Animal>` 对象，赋值给他吗？接下来就会看到.......

3) 声明（及触发）那些取泛型作参数的方法（declaring(and invoking) methods that take generic types）

```java
void foo(List<Song> list)
x.foo(songList)
```

> ***注***：关于 call 与 invoke 的区别，请参考：[what is the difference between 'call' and 'invoke'?](https://www.quora.com/What-is-the-difference-between-call-and-invoke)

在有着某个取一个参数，即 `Animal` 类型对象的 `ArrayList` 的方法时，到底那意味着什么呢？可否传递给这个方法一个 `Dog` 类型对象的 `ArrayList` 呢？后面就会讨论到与先前那种取老式普通数组作参数的方法相比，一些微妙而棘手的问题。

（这实际上与上面第二点相同，但这正好说明多态与泛型在一起时的重要性。）


### 运用泛型类（using generic CLASSES）

由于 `ArrayList` 是这里最常用到的泛化类型，那么这里将以他的文档开始。要注意到泛化类的以下两个关键点：

1) 这个 *类* 的声明（The *class* declaration）

2) 那些实现元素添加的 *方法* 的声明（The *method* declarations that let you add elements）


![Java API - ArrayList](images/Ch16_08.png)

*图 8 - Java API - ArrayList*
