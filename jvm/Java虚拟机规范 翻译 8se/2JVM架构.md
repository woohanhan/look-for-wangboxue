# 2.5.Run-time data areas
一部分在虚拟机启动时创建，在退出时销毁。除此之外则为每个线程私有，在线程创建时创建，在线程退出时销毁。<br>
## 2.5.1 the pc register
JVM支持多线程，每个线程拥有独立的pc register。在任一时刻线程总是正在执行一个方法的代码，我们称这个方法为当前方法。如果这个方法不属于native型，则pc register记录了当前正在被JVM执行的指令地址。如果属于native类型，则pc register 处于undefined状态。<br>
## 2.5.2 Java virtual machine stacks
在线程被创建时，随着线程一起创建的还有jvm stack。这个jvm stack存储线程的frames，它持有局部变量和部分结果，在函数的调用和返回中起到重要作用。jvm stack并不直接操作frames的push和pop,frames可能是由堆分配。所以，jvm stack并不需要一块连续分配的内存区域。<br>
关于jvm stack可能会出现以下几种异常情况:<br>
- 如果一个线程运行时，需要的jvm stack比允许的要更大，则会出现SOF。
- 当然如果jvm stack 被设计为动态扩展，但是可用的内存无法满足当前线程的动态扩展需求，那么会出现OOM。<br>
## 2.5.3 Heap
JVM拥有heap,heap属于所有的线程，所有类的实例和数组在heap上分配。<br>
heap在jvm启动时就被创建，heap上那些对象被gc进行管理，而不会显式的被回收。同样的heap也仅仅只是逻辑概念，而无需在内存中连续。<br>
关于jvm heap可能会出现以下异常情况:<br>
- 如果一个computation需要从heap获取更多的空间，但是当前gc无法满足其需求，则会出现OOM.
## 2.5.4 Method Area
JVM用于method area，method area属于所有的线程，method area类似于编译后的代码所在内存