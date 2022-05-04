# 附录：十个未列入到本书正式章节的题目

**Appendix: Ten Topics that almost made it into the Real Book**...

> 这里的意思，是还有更多的内容？这本书还没完？


这本书涵盖了很多基础知识，而你也快要学完这本书了。感谢你认真学完本书，不过在你投身于Java编程之前，我们还是希望给你更多的一些准备。对于这个附录，也不可能给到你想要的全部，原本这里通过将字型像素点大小缩减到 `.00003`，而尽力将所有需要掌握的Java知识都包含进来（不是之前那些章节中包含的）。这样是可行的，但那样就没有人能够阅读了。就这样，这里抛弃了很多内容，仅保留了是个最优的方面作为了附录（We covered a lot of ground, and you're almost finished with this book. We'll miss you, but before we let you go, we wouldn't feel right about sending you out into JavaLand without a little more preparation. We can't possibly fit everything you'll need to know into this relatively small appendix. Actually, we *did* originally include everything you need to know about Java(not already covered by the other chapters), by reducing the type point size to `.00003`. It all fit, but nobody could read it. So, we threw most of it away, but kept the best bits for this Top Ten appendix）。

这是本书 *真正的* 结尾了。当然还有一个索引（那也是必读部分）！

## 第10名：位操作

**#10 Bit Manipulation**

### 为何要关注？

**Why do you care**?

前面提到过，一个字节中有8个二进制位，短整型则有16个等待的事实。而在某些场合，可能需要对这些单独的二进制位，进行翻转。比如就可能出现给一台新的、启用了Java的烤面包机编写代码，并意识到由于严重的内存限制，一些确切的烤面包机设置，要在二进制位级别上加以控制。为了易于阅读，下面的代码注释中，仅展示了整数的后八位，而不是其整个的32位。

### 按位非运算符：`~`

**Bitwise NOT operator: `~`**


此运算符对原生值进行 “所有二进制位的翻转”：

```java
int x = 10;     // 其二进制位为 00001010
x = ~x;         // 现在的二进制位为 11110101
```

> **注意**：只有能转化为整型的原生类型，才能做二进制位翻转的非运算。经测试，整型、字符等都可以进行非运算；而逻辑值、浮点数等其他类型则不行（`error: bad operand type float for unary operator '~'`）

接下来的三个运算符，是对两个原生值，在位的基础上按位进行比较，进而返回一个基于这些位的比较的结果（The next three operators compare two primitives on a bit by bit basis, and return a result based on comparing these bits）。后面三个运算符都将用到下面整个示例：

```java
int x = 10;
int y = 6;
```

### 按位与运算符：`&`

**Bitwise AND Operator: `&`**

此运算符会返回一个仅在原先原本的两个二进制位 ***均为*** 开启时，二进制位才开启的值（This operator returns a value whose bits are turned on only if ***both*** bits are turned on）。

```java
int a = x & y; // x 的二进制位为：0000 0010
```

### 按位或运算符：`|`

**Bitwise OR Operator: `|`**

此运算符返回一个仅在两个原本两个二进制位 ***其一*** 为开启时，二进制位才开启的值（This operator returns a value whose bits are turned on only if ***either*** of the original bits are turned on）。

```java
int a = x | y;  // x 的二进制位为：0000 1110
```

### 按位异或运算符：`^`

**Bitwise XOR(exclusive OR) Operator: `^`**

此运算符返回一个仅在原本两个二进制位 ***只有其一*** 开启时，二进制位才开启的值（This operator returns a value whose bits are turned on only if ***exactly one*** of the original bits are turned on）。

```java
int a = x ^ y;  // x 的二进制位为：0000 1100
```

### 移位运算符

**The Shift Operators**

这些运算符取单个的整型原生变量，并对其全部二进制位在某个方向或另一方向上进行移位（或者说滑动）。只要稍加磨砺一下二进制数学技能，就应该知道，*往左* 对二进制位进行移位，就相当于把一个数 *乘以* 了某个二的某个幂次方，而 *往右* 移动二进制位，则相当于把一个数 *除以* 了一个二的某个幂次方（These operators take a single integer primitive and shift(or slide) all of its bits in one direction or another. If you want to dust off your binary math skills, you might realize that shifting bits *left* effectively *multiplies* a number by a power of two, and shifting *right* effectively *divides* a number by a power of two）。

这里将使用下面的示例，来讲解接下来的三个运算符：

```java
int x = -11;    // 二进制位为：1111 1111 1111 1111 1111 1111 1111 0101
```

好吧，好吧，之前一直回避了整个问题，那么现在就来以世界上最简短形式，讲一下负数存储，与 *二进制补码（two's complement）* 的问题。请记住，整型数最左边的那一位，被叫做 *符号位（sign bit）*。Java中负整型数的符号位始终是开启的（即被设置为 `1`）。而正整型数的符号位，则始终是关闭的（`0`）。Java 使用了 *二进制* 补码公式（二补公式）来存储负数。为了采用二进制补码，去改变某个数的符号，就要将这个数的的所有二进制位加以翻转，然后加上 `1`（对于一个字节表示整数来讲，这就意味着要将 `0000 0001`加到翻转后的值），这就是所谓的“二补公式”。

### 向右移位运算符：`>>`

**Right Shift Operator: `>>`**

此运算符将某个数的所有二进制位，往右移动某个确切数目，对于移动后左侧留下的那些空白二进制位，将以原先二进制数最左边的位填充。因此**符号位是不会改变的**：

```java
int y = x >> 2; // 此时二进制位为：1111 1111 1111 1111 1111 1111 1111 1101 -> -3
```

### 无符号向右移位运算符：`>>>`

此运算符与上面的右移运算符类似，但这个运算符会始终以零填充最左边的那些二进制位。因此 **符号就可能会改变**：

```java
int y = x >>> 2;    // 此时二进制位为：0011 1111 1111 1111 1111 1111 1111 1101 -> 1,073,741,821
```

### 向左移位运算符：`<<`

**Left Shift Operator: `<<`**

此运算符与上面的无符号右移运算符类似，不过是往另一方向而已；最右边的那些二进制位是以全零填充的。因此 **符号位可能改变**。

```java
int y = x << 2; // 此时二进制位为：1111 1111 1111 1111 1111 1111 1101 0100 -> -44
```

## 
