# 对象序列化与文件 `I/O`: 对象的保存

**serialization and file `I/O`: Saving Objects**

![第14章题图](images/Ch14_01.png)

*图  1 - 第14章题图*

**对象可被放气或充气（Objects can be flattened and inflated）**。对象具有状态和行为。关于对象的那些 *行为*，是存活在 *类* 中，而 *状态* 则是存活在各个单独 *对象* 中的（*Behavior* lives in the *class*, but *state* lives within each individual *object*）。那么在对对象状态进行保存时，会发生什么呢？比如在编写某个游戏时，就会需要一个特性来保存/恢复游戏。又比如在编写某个创建图表的app时，就需要一个保存/打开文件的特性。在程序需要保存状态时，*可以笨办法来完成*，对各个对象进行询问，然后费力地将各个示例变量的值，以某种自创格式，写到一个文件。或者，**可以轻松的面向对象方式完成** -- 简单地将对象本身冻干/压平/持久化/脱水，然后再通过重组/填充/恢复/注水，来取回对象。不过 *某些时候* 仍然需要以笨办法来完成对象状态的保存，尤其是app保存的文件必定会被其他非Java语言的 app 读取的时候，所以本章会对这两种保存对象状态的方式加以审视（If your program needs to save state, *you can do it the hard way*, interrogating each object, then painstakingly writing the value of each instance variable to a file, in a format you create. Or, **you can do it the easy OO way** -- you simply freeze-dry/flatten/persist/dehydrate the object itself, and reconstitue/inflate/restore/rehydrate it to get it back. But you'll still have to do it the hard way *sometimes*, especially when the file your app saves has to be read by some other non-Java application, so we'll look at both in this chapter）。

## 对节拍进行捕获

**Capture the Beat**


